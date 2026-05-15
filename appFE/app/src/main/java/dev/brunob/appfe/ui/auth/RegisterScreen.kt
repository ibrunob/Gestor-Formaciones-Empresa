package dev.brunob.appfe.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.brunob.appfe.R
import dev.brunob.appfe.ui.AppViewModel
import dev.brunob.appfe.ui.theme.DesktopControlShape
import dev.brunob.appfe.ui.theme.LoginBlueBottom
import dev.brunob.appfe.ui.theme.LoginBlueTop
import dev.brunob.appfe.ui.theme.desktopAuthTextFieldColors
import dev.brunob.appfe.ui.theme.desktopSuccessButtonColors

@Composable
fun RegisterScreen(
    vm: AppViewModel,
    onRegistered: () -> Unit,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val fieldModifier = Modifier.widthIn(max = 360.dp).fillMaxWidth()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LoginBlueTop, LoginBlueBottom)))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_logo_foreground),
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )
        Spacer(Modifier.height(14.dp))
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = fieldModifier,
            shape = DesktopControlShape,
            colors = desktopAuthTextFieldColors()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            singleLine = true,
            modifier = fieldModifier,
            shape = DesktopControlShape,
            colors = desktopAuthTextFieldColors()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = fieldModifier,
            shape = DesktopControlShape,
            colors = desktopAuthTextFieldColors()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = fieldModifier,
            shape = DesktopControlShape,
            colors = desktopAuthTextFieldColors()
        )
        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(error!!, color = Color(0xFFFFCDD2))
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                error = null
                vm.register(email, password, nombre, apellidos) { result ->
                    result.onSuccess { onRegistered() }
                        .onFailure { error = it.message }
                }
            },
            enabled = listOf(nombre, apellidos, email, password).all { it.isNotBlank() },
            modifier = fieldModifier.height(46.dp),
            shape = DesktopControlShape,
            colors = desktopSuccessButtonColors()
        ) { Text("Registrarme") }
        TextButton(
            onClick = onBack,
            colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
        ) { Text("Volver a iniciar sesión") }
    }
}
