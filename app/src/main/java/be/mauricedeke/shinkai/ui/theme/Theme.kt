package be.mauricedeke.shinkai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ShinkaiLightColorScheme = lightColorScheme(
    primary = ShinkaiRed,
    onPrimary = ShinkaiWhite,
    primaryContainer = ShinkaiRedAccent,
    onPrimaryContainer = ShinkaiWhite,
    secondary = ShinkaiDark,
    onSecondary = ShinkaiWhite,
    background = ShinkaiBackground,
    onBackground = ShinkaiText,
    surface = ShinkaiCardBg,
    onSurface = ShinkaiText,
    surfaceVariant = ShinkaiPressed,
    onSurfaceVariant = ShinkaiTextGray,
    outline = ShinkaiTextGray,
)

private val ShinkaiDarkColorScheme = darkColorScheme(
    primary = BrightRed,
    onPrimary = ShinkaiWhite,
    primaryContainer = ShinkaiRedAccent,
    onPrimaryContainer = ShinkaiWhite,
    secondary = ShinkaiWhite,
    onSecondary = ShinkaiBlack,
    background = ShinkaiBlack,
    onBackground = ShinkaiWhite,
    surface = ShinkaiDark,
    onSurface = ShinkaiWhite,
    surfaceVariant = ShinkaiBlack,
    onSurfaceVariant = ShinkaiTextGray,
    outline = ShinkaiTextGray,
)

@Composable
fun ShinkaikarateappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) ShinkaiDarkColorScheme else ShinkaiLightColorScheme,
        typography = Typography,
        content = content
    )
}
