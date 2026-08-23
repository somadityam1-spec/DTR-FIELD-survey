package com.example.ui.theme

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
    primary = Color(0xFF86A87D),
    onPrimary = Color(0xFF10241A),
    primaryContainer = Color(0xFF385131),
    onPrimaryContainer = Color(0xFFD3EADC),
    secondary = Color(0xFFF4B296),
    onSecondary = Color(0xFF311104),
    secondaryContainer = Color(0xFF5A2C18),
    onSecondaryContainer = Color(0xFFFFDBCB),
    tertiary = Color(0xFFA6C3E8),
    onTertiary = Color(0xFF001D35),
    background = Color(0xFF1C1917),
    surface = Color(0xFF292524),
    onBackground = Color(0xFFFDF8F6),
    onSurface = Color(0xFFFDF8F6),
    surfaceVariant = Color(0xFF383330),
    onSurfaceVariant = Color(0xFFD7CCC8),
    outline = Color(0xFF57534E)
)

private val LightColorScheme = lightColorScheme(
    primary = NaturalGreenPrimary,
    onPrimary = NaturalGreenOnPrimary,
    primaryContainer = NaturalGreenContainer,
    onPrimaryContainer = NaturalGreenOnContainer,
    secondary = StatusModerateAmber,
    onSecondary = Color.White,
    secondaryContainer = StatusModerateContainer,
    onSecondaryContainer = StatusModerateOnContainer,
    tertiary = ToneSkyBorder,
    onTertiary = ToneSkyText,
    background = BackgroundWarmLinen,
    surface = CardSurfaceLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF7F1EE),
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep high contrast brand colors consistent for field visibility
    content: @Composable () -> Unit,
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
