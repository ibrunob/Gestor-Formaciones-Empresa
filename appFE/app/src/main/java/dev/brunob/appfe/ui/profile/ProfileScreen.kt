package dev.brunob.appfe.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.brunob.appfe.ui.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    vm: AppViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val student by vm.currentStudent.collectAsState()
    val s = student ?: return

    var nombre by remember { mutableStateOf(s.nombre) }
    var apellidos by remember { mutableStateOf(s.apellidos) }
    var tutorEmpresa by remember { mutableStateOf(s.tutorEmpresa) }
    var tutorEmpresaEmail by remember { mutableStateOf(s.tutorEmpresaEmail) }
    var tutorDocente by remember { mutableStateOf(s.tutorDocente) }
    var tutorDocenteEmail by remember { mutableStateOf(s.tutorDocenteEmail) }
    var empresa by remember { mutableStateOf(s.empresa) }
    var horario by remember { mutableStateOf(s.horario) }
    var fechaInicio by remember { mutableStateOf(s.fechaInicio) }
    var fechaFin by remember { mutableStateOf(s.fechaFin) }
    var planFormacion by remember { mutableStateOf(s.planFormacion) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Datos personales", style = MaterialTheme.typography.titleMedium)
            Field("Nombre", nombre) { nombre = it }
            Field("Apellidos", apellidos) { apellidos = it }
            Text("Correo: ${s.email}", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(8.dp))
            Text("Formación", style = MaterialTheme.typography.titleMedium)
            Field("Empresa", empresa) { empresa = it }
            Field("Horario en la empresa", horario) { horario = it }
            Field("Fecha inicio (YYYY-MM-DD)", fechaInicio) { fechaInicio = it }
            Field("Fecha fin (YYYY-MM-DD)", fechaFin) { fechaFin = it }
            Field("Plan de formación", planFormacion, singleLine = false) { planFormacion = it }

            Spacer(Modifier.height(8.dp))
            Text("Tutores", style = MaterialTheme.typography.titleMedium)
            Field("Tutor de empresa", tutorEmpresa) { tutorEmpresa = it }
            Field("Email tutor empresa", tutorEmpresaEmail) { tutorEmpresaEmail = it }
            Field("Tutor docente", tutorDocente) { tutorDocente = it }
            Field("Email tutor docente", tutorDocenteEmail) { tutorDocenteEmail = it }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    vm.updateProfile(
                        s.copy(
                            nombre = nombre, apellidos = apellidos,
                            tutorEmpresa = tutorEmpresa, tutorEmpresaEmail = tutorEmpresaEmail,
                            tutorDocente = tutorDocente, tutorDocenteEmail = tutorDocenteEmail,
                            empresa = empresa, horario = horario,
                            fechaInicio = fechaInicio, fechaFin = fechaFin,
                            planFormacion = planFormacion
                        )
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar cambios") }
            TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
        }
    }
}

@Composable
private fun Field(label: String, value: String, singleLine: Boolean = true, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = singleLine,
        modifier = Modifier.fillMaxWidth()
    )
}
