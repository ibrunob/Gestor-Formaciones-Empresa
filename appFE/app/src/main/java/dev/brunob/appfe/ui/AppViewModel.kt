package dev.brunob.appfe.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.brunob.appfe.AppFEApplication
import dev.brunob.appfe.data.AppRepository
import dev.brunob.appfe.data.model.DayEntry
import dev.brunob.appfe.data.model.DayStatus
import dev.brunob.appfe.data.model.Student
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: AppRepository = (app as AppFEApplication).repository

    val currentStudent: StateFlow<Student?> = repo.currentStudent
    val days: StateFlow<List<DayEntry>> = repo.days

    fun register(
        email: String,
        password: String,
        nombre: String,
        apellidos: String,
        onResult: (Result<Student>) -> Unit
    ) {
        viewModelScope.launch {
            onResult(repo.register(email, password, nombre, apellidos))
        }
    }

    fun login(email: String, password: String, onResult: (Result<Student>) -> Unit) {
        viewModelScope.launch { onResult(repo.login(email, password)) }
    }

    fun logout() = viewModelScope.launch { repo.logout() }

    fun updateProfile(student: Student) =
        viewModelScope.launch { repo.updateProfile(student) }

    fun marcarAsistencia(date: LocalDate) {
        val key = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        viewModelScope.launch {
            repo.upsertDay(DayEntry(date = key, status = DayStatus.ASISTENCIA))
        }
    }

    fun marcarAusencia(date: LocalDate, motivo: String, justificanteUri: String?, notas: String?) {
        val key = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        viewModelScope.launch {
            repo.upsertDay(
                DayEntry(
                    date = key,
                    status = DayStatus.AUSENCIA,
                    absenceReason = motivo,
                    justificanteUri = justificanteUri,
                    notes = notas
                )
            )
        }
    }

    fun marcarNoLectivo(date: LocalDate) {
        val key = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        viewModelScope.launch {
            repo.upsertDay(DayEntry(date = key, status = DayStatus.NO_LECTIVO))
        }
    }

    fun limpiar(date: LocalDate) {
        val key = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        viewModelScope.launch {
            repo.upsertDay(DayEntry(date = key, status = DayStatus.PENDIENTE))
        }
    }

    fun getDay(date: LocalDate): DayEntry? = repo.getDay(date)
    fun cotizacionMes(mes: YearMonth): Int = repo.cotizacionDelMes(mes)
}
