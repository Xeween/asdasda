package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val HubDarkColorScheme = darkColorScheme(
    primary = CoralAccent,
    onPrimary = CoralText,
    primaryContainer = CoralDark,
    onPrimaryContainer = CoralAccent,
    secondary = VioletBright,
    onSecondary = TextPrimary,
    secondaryContainer = VioletBg,
    onSecondaryContainer = VioletBright,
    tertiary = CyanAccent,
    onTertiary = ObsidianBg,
    tertiaryContainer = CyanBg,
    onTertiaryContainer = CyanAccent,
    background = ObsidianBg,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderBright,
    error = CrimsonRank,
    onError = TextPrimary
)

val HubShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun HubOperatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HubDarkColorScheme,
        typography = Typography,
        shapes = HubShapes,
        content = content
    )
}
