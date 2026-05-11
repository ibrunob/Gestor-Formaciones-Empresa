package dev.brunob.appfe.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.brunob.appfe.data.model.DayEntry
import dev.brunob.appfe.data.model.DayStatus
import dev.brunob.appfe.data.model.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.UUID

private val Context.dataStore by preferencesDataStore(name = "appfe_store")

class AppRepository private constructor(private val appContext: Context) {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private val USERS_KEY = stringPreferencesKey("users_json")
    private val SESSION_KEY = stringPreferencesKey("session_user_id")
    private val DAYS_KEY = stringPreferencesKey("days_json") // userId -> List<DayEntry>

    private val usersSerializer = MapSerializer(String.serializer(), Student.serializer())
    private val daysSerializer = MapSerializer(
        String.serializer(),
        ListSerializer(DayEntry.serializer())
    )

    private val _currentStudent = MutableStateFlow<Student?>(null)
    val currentStudent: StateFlow<Student?> = _currentStudent.asStateFlow()

    private val _days = MutableStateFlow<List<DayEntry>>(emptyList())
    val days: StateFlow<List<DayEntry>> = _days.asStateFlow()

    suspend fun init() {
        val prefs = appContext.dataStore.data.first()
        val sessionId = prefs[SESSION_KEY]
        if (sessionId != null) {
            val users = readUsers()
            _currentStudent.value = users[sessionId]
            _days.value = readDays()[sessionId].orEmpty()
        }
    }

    private suspend fun readUsers(): Map<String, Student> {
        val raw = appContext.dataStore.data.first()[USERS_KEY] ?: return emptyMap()
        return runCatching { json.decodeFromString(usersSerializer, raw) }.getOrDefault(emptyMap())
    }

    private suspend fun writeUsers(users: Map<String, Student>) {
        appContext.dataStore.edit { it[USERS_KEY] = json.encodeToString(usersSerializer, users) }
    }

    private suspend fun readDays(): Map<String, List<DayEntry>> {
        val raw = appContext.dataStore.data.first()[DAYS_KEY] ?: return emptyMap()
        return runCatching { json.decodeFromString(daysSerializer, raw) }.getOrDefault(emptyMap())
    }

    private suspend fun writeDays(map: Map<String, List<DayEntry>>) {
        appContext.dataStore.edit { it[DAYS_KEY] = json.encodeToString(daysSerializer, map) }
    }

    suspend fun register(
        email: String,
        password: String,
        nombre: String,
        apellidos: String
    ): Result<Student> {
        val users = readUsers().toMutableMap()
        if (users.values.any { it.email.equals(email, ignoreCase = true) }) {
            return Result.failure(IllegalStateException("Ya existe un usuario con ese correo"))
        }
        val student = Student(
            id = UUID.randomUUID().toString(),
            email = email.trim(),
            password = password,
            nombre = nombre.trim(),
            apellidos = apellidos.trim()
        )
        users[student.id] = student
        writeUsers(users)
        startSession(student)
        return Result.success(student)
    }

    suspend fun login(email: String, password: String): Result<Student> {
        val users = readUsers()
        val student = users.values.firstOrNull {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        } ?: return Result.failure(IllegalStateException("Credenciales no válidas"))
        startSession(student)
        return Result.success(student)
    }

    private suspend fun startSession(student: Student) {
        appContext.dataStore.edit { it[SESSION_KEY] = student.id }
        _currentStudent.value = student
        _days.value = readDays()[student.id].orEmpty()
    }

    suspend fun logout() {
        appContext.dataStore.edit { it.remove(SESSION_KEY) }
        _currentStudent.value = null
        _days.value = emptyList()
    }

    suspend fun updateProfile(updated: Student) {
        val users = readUsers().toMutableMap()
        users[updated.id] = updated
        writeUsers(users)
        _currentStudent.value = updated
    }

    suspend fun upsertDay(entry: DayEntry) {
        val student = _currentStudent.value ?: return
        val all = readDays().toMutableMap()
        val list = all[student.id].orEmpty().toMutableList()
        val idx = list.indexOfFirst { it.date == entry.date }
        if (idx >= 0) list[idx] = entry else list.add(entry)
        all[student.id] = list
        writeDays(all)
        _days.value = list
    }

    fun getDay(date: LocalDate): DayEntry? {
        val key = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        return _days.value.firstOrNull { it.date == key }
    }

    /** Días gestionados (asistencia o ausencia) en el mes. */
    fun cotizacionDelMes(month: YearMonth): Int {
        val prefix = "%04d-%02d".format(month.year, month.monthValue)
        return _days.value.count {
            it.date.startsWith(prefix) && it.status == DayStatus.ASISTENCIA
        }
    }

    /** Devuelve los días hábiles (L-V) ya pasados que siguen pendientes en la semana actual. */
    fun pendientesSemanaActual(today: LocalDate = LocalDate.now()): List<LocalDate> {
        val monday = today.with(DayOfWeek.MONDAY)
        val result = mutableListOf<LocalDate>()
        var d = monday
        while (!d.isAfter(today) && d.dayOfWeek <= DayOfWeek.FRIDAY) {
            val entry = getDay(d)
            val status = entry?.status ?: DayStatus.PENDIENTE
            if (status == DayStatus.PENDIENTE) result.add(d)
            d = d.plusDays(1)
        }
        return result
    }

    companion object {
        @Volatile private var INSTANCE: AppRepository? = null
        fun get(context: Context): AppRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
