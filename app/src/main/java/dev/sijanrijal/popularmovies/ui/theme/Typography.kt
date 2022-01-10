package dev.sijanrijal.popularmovies.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.sijanrijal.popularmovies.R

private val PulpDisplayFontfamily = FontFamily(
    Font(R.font.pulp_display_light, FontWeight.SemiBold),
    Font(R.font.pulp_display_regular),
    Font(R.font.pulp_display_medium, FontWeight.Bold)
)

val Typography = Typography(
    defaultFontFamily = PulpDisplayFontfamily,
    h3 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )
)