package dev.brunob.appfe.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DesktopBlue,
    onPrimary = DesktopSurface,
    secondary = DesktopNavyLight,
    background = DesktopNavy,
    surface = DesktopNavyLight,
    onBackground = DesktopSurface,
    onSurface = DesktopSurface,
    error = DesktopDanger
)

private val LightColorScheme = lightColorScheme(
    primary = DesktopBlue,
    onPrimary = DesktopSurface,
    secondary = DesktopNavy,
    onSecondary = DesktopSurface,
    tertiary = DesktopSuccess,
    background = DesktopBackground,
    onBackground = DesktopNavy,
    surface = DesktopSurface,
    onSurface = DesktopNavy,
    surfaceVariant = Color(0xFFECF0F1),
    onSurfaceVariant = DesktopMuted,
    outline = DesktopBorder,
    error = DesktopDanger
)

@Composable
fun AppFETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}