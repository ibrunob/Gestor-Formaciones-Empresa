package dev.brunob.appfe.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.brunob.appfe.data.model.DayEntry
import dev.brunob.appfe.data.model.DayStatus
import dev.brunob.appfe.ui.AppViewModel
import dev.brunob.appfe.ui.theme.DesktopBlue
import dev.brunob.appfe.ui.theme.DesktopDanger
import dev.brunob.appfe.ui.theme.DesktopMuted
import dev.brunob.appfe.ui.theme.DesktopSuccess
import dev.brunob.appfe.ui.theme.desktopTopAppBarColors
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val Asistencia = DesktopSuccess
private val Ausencia = DesktopDanger
private val NoLectivo = Color(0xFFBDC3C7)
private val Pendiente = Color(0xFFF1C40F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    vm: AppViewModel,
    onBack: () -> Unit
) {
    val days by vm.days.collectAsState()
    var mes by remember { mutableStateOf(YearMonth.now()) }
    var dialogDate by remember { mutableStateOf<LocalDate?>(null) }

    val byDate = remember(days) { days.associateBy { it.date } }
    val cot = days.count {
        it.date.startsWith("%04d-%02d".format(mes.year, mes.monthValue)) &&
            it.status == DayStatus.ASISTENCIA
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Calendario") },
                colors = desktopTopAppBarColors(),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { mes = mes.minusMonths(1) }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Mes anterior")
                }
                Text(
                    text = mes.month.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                        .replaceFirstChar { it.uppercase() } + " " + mes.year,
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = { mes = mes.plusMonths(1) }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Mes siguiente")
                }
            }
            Text(
                "Días cotizados este mes: $cot",
                style = MaterialTheme.typography.bodyMedium,
                color = DesktopMuted
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
            WeekHeader()
            MonthGrid(
                mes = mes,
                byDate = byDate,
                onDayClick = { dialogDate = it }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
            Legend()
        }
    }

    val date = dialogDate
    if (date != null) {
        DayActionDialog(
            date = date,
            existing = vm.getDay(date),
            onDismiss = { dialogDate = null },
            onAsistencia = {
                vm.marcarAsistencia(date)
                dialogDate = null
            },
            onAusencia = { motivo, uri, notas ->
                vm.marcarAusencia(date, motivo, uri, notas)
                dialogDate = null
            },
            onNoLectivo = {
                vm.marcarNoLectivo(date)
                dialogDate = null
            },
            onLimpiar = {
                vm.limpiar(date)
                dialogDate = null
            }
        )
    }
}

@Composable
private fun WeekHeader() {
    val days = listOf("L", "M", "X", "J", "V", "S", "D")
    Row(modifier = Modifier.fillMaxWidth()) {
        days.forEach { d ->
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(d, fontWeight = FontWeight.Bold, color = DesktopMuted)
            }
        }
    }
}

@Composable
private fun MonthGrid(
    mes: YearMonth,
    byDate: Map<String, DayEntry>,
    onDayClick: (LocalDate) -> Unit
) {
    val firstDay = mes.atDay(1)
    val offset = (firstDay.dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
    val totalCells = offset + mes.lengthOfMonth()
    val rows = (totalCells + 6) / 7
    val cellsCount = rows * 7

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(cellsCount) { index ->
            val dayNumber = index - offset + 1
            if (dayNumber in 1..mes.lengthOfMonth()) {
                val date = mes.atDay(dayNumber)
                val entry = byDate[date.format(DateTimeFormatter.ISO_LOCAL_DATE)]
                val isWeekend = date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
                DayCell(
                    date = date,
                    entry = entry,
                    weekend = isWeekend,
                    onClick = { onDayClick(date) }
                )
            } else {
                Box(modifier = Modifier.aspectRatio(1f))
            }
        }
    }
}

@Composable
private fun DayCell(date: LocalDate, entry: DayEntry?, weekend: Boolean, onClick: () -> Unit) {
    val status = entry?.status ?: if (weekend) DayStatus.NO_LECTIVO else DayStatus.PENDIENTE
    val color = when (status) {
        DayStatus.ASISTENCIA -> Asistencia
        DayStatus.AUSENCIA -> Ausencia
        DayStatus.NO_LECTIVO -> NoLectivo
        DayStatus.PENDIENTE -> MaterialTheme.colorScheme.surface
    }
    val today = date == LocalDate.now()
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(color.copy(alpha = if (status == DayStatus.PENDIENTE) 1f else 0.9f))
            .border(
                width = if (today) 2.dp else 1.dp,
                color = if (today) DesktopBlue else MaterialTheme.colorScheme.outline.copy(alpha = 0.42f),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable(enabled = !weekend || entry != null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = if (status == DayStatus.PENDIENTE) MaterialTheme.colorScheme.onSurface else Color.White,
            fontWeight = if (today) FontWeight.Bold else FontWeight.Normal
        )
        if (status == DayStatus.PENDIENTE && !weekend && !date.isAfter(LocalDate.now())) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Pendiente)
            )
        }
    }
}

@Composable
private fun Legend(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        LegendRow(Asistencia, "Asistencia (suma cotización)")
        LegendRow(Ausencia, "Ausencia justificada")
        LegendRow(NoLectivo, "No lectivo (no cuenta)")
        LegendRow(Pendiente, "Pendiente de gestionar")
    }
}

@Composable
private fun LegendRow(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(Modifier.size(8.dp))
        Text(label)
    }
}
