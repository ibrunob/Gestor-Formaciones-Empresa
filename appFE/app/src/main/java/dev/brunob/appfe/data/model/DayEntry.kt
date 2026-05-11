package dev.brunob.appfe.data.model

import kotlinx.serialization.Serializable

enum class DayStatus {
    PENDIENTE,
    ASISTENCIA,
    AUSENCIA,
    NO_LECTIVO
}

enum class AbsenceReason(val label: String) {
    ENFERMEDAD("Enfermedad"),
    EXAMEN("Asistencia a examen"),
    DEBER_INEXCUSABLE("Deber inexcusable"),
    OTRO("Otro")
}

@Serializable
data class DayEntry(
    val date: String,                       // ISO yyyy-MM-dd
    val status: DayStatus = DayStatus.PENDIENTE,
    val absenceReason: String? = null,      // AbsenceReason.name
    val justificanteUri: String? = null,    // content:// URI persistido
    val notes: String? = null
)
