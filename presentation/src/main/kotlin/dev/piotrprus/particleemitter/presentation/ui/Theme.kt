package dev.piotrprus.particleemitter.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object DeckColors {
    val background = Color(0xFF0E1116)
    val surface = Color(0xFF161B22)
    val codeBackground = Color(0xFF2B2B2B)
    val codeBorder = Color(0xFF3C3F41)
    val textPrimary = Color(0xFFE6EDF3)
    val textSecondary = Color(0xFF8B949E)
    val accent = Color(0xFF7C4DFF)
    val accentAlt = Color(0xFF00E5FF)
    val warm = Color(0xFFFFB74D)
}

@Composable
fun PresentationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = DeckColors.background,
            surface = DeckColors.surface,
            primary = DeckColors.accent,
            onBackground = DeckColors.textPrimary,
            onSurface = DeckColors.textPrimary,
        ),
        content = content,
    )
}

/** Standard slide chrome: gradient background and consistent padding. */
@Composable
fun SlideSurface(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(DeckColors.background, Color(0xFF131A26)),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 110.dp, vertical = 70.dp),
            content = content,
        )
    }
}

@Composable
fun SlideTitle(text: String, accent: Color = DeckColors.accent) {
    Column {
        Text(
            text = text,
            color = DeckColors.textPrimary,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.size(18.dp))
        Box(
            modifier = Modifier
                .size(width = 180.dp, height = 6.dp)
                .background(
                    Brush.horizontalGradient(listOf(accent, DeckColors.accentAlt)),
                    CircleShape,
                ),
        )
    }
}

@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = DeckColors.textPrimary,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = 34.sp,
        lineHeight = 48.sp,
    )
}

@Composable
fun Bullet(text: String, emphasis: String? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(DeckColors.accentAlt, CircleShape),
        )
        Column {
            if (emphasis != null) {
                Text(
                    text = emphasis,
                    color = DeckColors.textPrimary,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = text,
                    color = DeckColors.textSecondary,
                    fontSize = 30.sp,
                    lineHeight = 42.sp,
                )
            } else {
                BodyText(text)
            }
        }
    }
}
