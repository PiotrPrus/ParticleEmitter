package dev.piotrprus.particleemitter

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle

sealed interface ParticleShape {
    object Circle : ParticleShape
    data class PathShape(val shapePath: Path) : ParticleShape
    data class Image(val imageBitmap: ImageBitmap) : ParticleShape
    data class Text(
        val text: String,
        val textStyle: TextStyle,
        val textMeasurer: TextMeasurer,
    ) : ParticleShape {
        internal val layoutResult: TextLayoutResult by lazy {
            textMeasurer.measure(text, textStyle)
        }
    }
}
