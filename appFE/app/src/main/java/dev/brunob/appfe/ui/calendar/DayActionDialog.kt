package dev.brunob.appfe.ui.calendar

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.brunob.appfe.data.model.AbsenceReason
import dev.brunob.appfe.data.model.DayEntry
import dev.brunob.appfe.data.model.DayStatus
import dev.brunob.appfe.ui.theme.DesktopControlShape
import dev.brunob.appfe.ui.theme.desktopButtonColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DayActionDialog(
    date: LocalDate,
    existing: DayEntry?,
    onDismiss: () -> Unit,
    onAsistencia: () -> Unit,
    onAusencia: (motivo: String, justificanteUri: String?, notas: String?) -> Unit,
    onNoLectivo: () -> Unit,
    onLimpiar: () -> Unit
) {
    var mostrandoAusencia by remember { mutableStateOf(false) }

    if (mostrandoAusencia) {
        AusenciaDialog(
            date = date,
            existing = existing,
            onDismiss = { mostrandoAusencia = false },
            onConfirm = { motivo, uri, notas ->
                mostrandoAusencia = false
                onAusencia(motivo, uri, notas)
            }
        )
        return
    }

    val fechaTxt = date.format(
        DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    ).replaceFirstChar { it.uppercase() }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = DesktopControlShape,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text(fechaTxt) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val estadoTxt = when (existing?.status) {
                    DayStatus.ASISTENCIA -> "Estado actual: Asistencia"
                    DayStatus.AUSENCIA -> "Estado actual: Ausencia (${existing.absenceReason ?: ""})"
                    DayStatus.NO_LECTIVO -> "Estado actual: No lectivo"
                    else -> "Estado actual: Pendiente"
                }
                Text(estadoTxt, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text("Selecciona qué hacer con este día:")
            }
        },
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(
                    onClick = onAsistencia,
                    modifier = Modifier.fillMaxWidth(),
                    shape = DesktopControlShape,
                    colors = desktopButtonColors()
                ) {
                    Text("Marcar asistencia")
                }
                OutlinedButton(
                    onClick = { mostrandoAusencia = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = DesktopControlShape
                ) { Text("Marcar ausencia") }
                OutlinedButton(
                    onClick = onNoLectivo,
                    modifier = Modifier.fillMaxWidth(),
                    shape = DesktopControlShape
                ) {
                    Text("Marcar como no lectivo")
                }
                if (existing != null && existing.status != DayStatus.PENDIENTE) {
                    TextButton(onClick = onLimpiar, modifier = Modifier.fillMaxWidth()) {
                        Text("Quitar marca")
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun AusenciaDialog(
    date: LocalDate,
    existing: DayEntry?,
    onDismiss: () -> Unit,
    onConfirm: (motivo: String, justificanteUri: String?, notas: String?) -> Unit
) {
    val context = LocalContext.current
    val initialMotivo = AbsenceReason.entries.firstOrNull { it.name == existing?.absenceReason }
        ?: AbsenceReason.ENFERMEDAD
    var motivo by remember { mutableStateOf(initialMotivo) }
    var notas by remember { mutableStateOf(existing?.notes.orEmpty()) }
    var uri by remember { mutableStateOf<String?>(existing?.justificanteUri) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { result ->
        if (result != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    result, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) { /* ignore */ }
            uri = result.toString()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = DesktopControlShape,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Ausencia · ${date.format(DateTimeFormatter.ISO_LOCAL_DATE)}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Motivo:", style = MaterialTheme.typography.labelLarge)
                AbsenceReason.entries.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = motivo == reason,
                                onClick = { motivo = reason }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = motivo == reason, onClick = { motivo = reason })
                        Text(reason.label)
                    }
                }
                OutlinedTextField(
                    value = notas,
                    onValueChange = { notas = it },
                    label = { Text("Notas (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = DesktopControlShape
                )
                OutlinedButton(
                    onClick = { launcher.launch(arrayOf("*/*")) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = DesktopControlShape
                ) {
                    Text(if (uri == null) "Adjuntar justificante" else "Cambiar justificante")
                }
                if (uri != null) {
                    Text(
                        text = "Adjunto: ${Uri.parse(uri).lastPathSegment.orEmpty()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(motivo.name, uri, notas.takeIf { it.isNotBlank() }) },
                shape = DesktopControlShape,
                colors = desktopButtonColors()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
