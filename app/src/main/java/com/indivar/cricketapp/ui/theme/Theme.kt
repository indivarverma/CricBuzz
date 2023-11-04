package com.indivar.cricketapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


/*private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)*/

private val DarkColorScheme = darkColorScheme(
    primary = Color(0XFFa6c8ff),
    onPrimary = Color(0XFF003060),
    primaryContainer = Color(0XFF004787),
    onPrimaryContainer = Color(0XFFd5e3ff),
    secondary = Color(0XFFbdc7dc),
    onSecondary = Color(0XFF273141),
    secondaryContainer = Color(0XFF3d4758),
    onSecondaryContainer = Color(0XFFd9e3f8),
    tertiary = Color(0XFFb0c6ff),
    onTertiary = Color(0XFF002d6f),
    tertiaryContainer = Color(0XFF1a438f),
    onTertiaryContainer = Color(0XFFd9e2ff),
    error = Color(0XFFffb4ab),
    onError = Color(0XFF690005),
    errorContainer = Color(0XFF93000a),
    onErrorContainer = Color(0XFFffdad6),
    background = Color(0XFF1a1c1e),
    onBackground = Color(0XFFe3e2e6),
    surface = Color(0XFF1a1c1e),
    onSurface = Color(0XFFe3e2e6),
    outline = Color(0xFF8d9199),
    surfaceVariant = Color(0xFF43474e),
    onSurfaceVariant = Color(0XFFc4c6cf),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0XFF225fa6),
    onPrimary = Color(0XFFffffff),
    primaryContainer = Color(0XFFd5e3ff),
    onPrimaryContainer = Color(0XFF001c3b),
    secondary = Color(0XFF555f71),
    onSecondary = Color(0XFFffffff),
    secondaryContainer = Color(0XFFd9e3f8),
    onSecondaryContainer = Color(0XFF121c2b),
    tertiary = Color(0XFF375ca9),
    onTertiary = Color(0XFFffffff),
    tertiaryContainer = Color(0XFFd9e2ff),
    onTertiaryContainer = Color(0XFF001945),
    error = Color(0XFFba1a1a),
    onError = Color(0XFFffffff),
    errorContainer = Color(0XFFffdad6),
    onErrorContainer = Color(0XFF410002),
    background = Color(0XFFfdfbff),
    onBackground = Color(0XFF1a1c1e),
    surface = Color(0XFFfdfbff),
    onSurface = Color(0XFF1a1c1e),
    outline = Color(0XFF74777f),
    surfaceVariant = Color(0XFFe0e2ec),
    onSurfaceVariant = Color(0XFF43474e),

    )

@Composable
fun CricketAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}