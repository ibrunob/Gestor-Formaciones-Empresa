package dev.brunob.appfe.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val DesktopControlShape = RoundedCornerShape(8.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun desktopTopAppBarColors(): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
    containerColor = DesktopNavy,
    titleContentColor = Color.White,
    navigationIconContentColor = Color.White,
    actionIconContentColor = Color.White
)

@Composable
fun desktopButtonColors(): ButtonColors = ButtonDefaults.buttonColors(
    containerColor = DesktopBlue,
    contentColor = Color.White,
    disabledContainerColor = DesktopBorder,
    disabledContentColor = DesktopMuted
)

@Composable
fun desktopSuccessButtonColors(): ButtonColors = ButtonDefaults.buttonColors(
    containerColor = DesktopSuccess,
    contentColor = Color.White,
    disabledContainerColor = DesktopBorder,
    disabledContentColor = DesktopMuted
)

@Composable
fun desktopAuthTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = DesktopNavy,
    unfocusedTextColor = DesktopNavy,
    focusedContainerColor = Color.White.copy(alpha = 0.94f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.94f),
    disabledContainerColor = Color.White.copy(alpha = 0.78f),
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.White.copy(alpha = 0.72f),
    focusedLabelColor = DesktopNavy,
    unfocusedLabelColor = DesktopMuted,
    focusedPlaceholderColor = DesktopMuted,
    unfocusedPlaceholderColor = DesktopMuted,
    cursorColor = DesktopNavy
)
