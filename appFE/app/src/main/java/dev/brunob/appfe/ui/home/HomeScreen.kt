package dev.brunob.appfe.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.brunob.appfe.ui.AppViewModel
import dev.brunob.appfe.ui.theme.DesktopControlShape
import dev.brunob.appfe.ui.theme.DesktopMuted
import dev.brunob.appfe.ui.theme.desktopButtonColors
import dev.brunob.appfe.ui.theme.desktopTopAppBarColors
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: AppViewModel,
    onOpenProfile: () -> Unit,
    onOpenCalendar: () -> Unit
) {
    val student by vm.currentStudent.collectAsState()
    val days by vm.days.collectAsState()
    val mes = YearMonth.now()
    val cotMes = days.count {
        it.date.startsWith("%04d-%02d".format(mes.year, mes.monthValue)) &&
            it.status.name == "ASISTENCIA"
    }
    val nombreMes = mes.month.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
        .replaceFirstChar { it.uppercase() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Hola, ${student?.nombre.orEmpty()}") },
                colors = desktopTopAppBarColors(),
                actions = {
                    IconButton(onClick = onOpenProfile) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Días cotizados en $nombreMes", style = MaterialTheme.typography.titleMedium)
            Text(
                "$cotMes",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))

            Text("Empresa", style = MaterialTheme.typography.titleMedium)
            Text(student?.empresa?.ifBlank { "Sin asignar" } ?: "—")
            Spacer(Modifier.height(2.dp))
            Text("Tutor de empresa", style = MaterialTheme.typography.titleSmall, color = DesktopMuted)
            Text(student?.tutorEmpresa?.ifBlank { "—" } ?: "—")
            Text("Tutor docente", style = MaterialTheme.typography.titleSmall, color = DesktopMuted)
            Text(student?.tutorDocente?.ifBlank { "—" } ?: "—")

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onOpenCalendar,
                modifier = Modifier.fillMaxWidth(),
                shape = DesktopControlShape,
                colors = desktopButtonColors()
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Abrir calendario")
            }
        }
    }
}
