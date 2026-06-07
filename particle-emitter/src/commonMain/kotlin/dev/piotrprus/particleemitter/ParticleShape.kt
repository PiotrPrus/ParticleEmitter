package dev.piotrprus.particleemitter

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

sealed interface ParticleShape {
    object Circle : ParticleShape
    data class PathShape(val shapePath: Path) : ParticleShape
    data class Image(val imageBitmap: ImageBitmap) : ParticleShape
    data class Text(
        val text: String,
        val textStyle: TextStyle,
        val textMeasurer: TextMeasurer,
    ) : ParticleShape {
        internal val bitmap: ImageBitmap by lazy { rasterize() }

        private fun rasterize(): ImageBitmap {
            val layoutResult = textMeasurer.measure(text, textStyle)
            val w = layoutResult.size.width
            val h = layoutResult.size.height
            val bitmap = ImageBitmap(w, h)
            val canvas = Canvas(bitmap)
            val drawScope = CanvasDrawScope()
            drawScope.draw(
                density = Density(1f),
                layoutDirection = LayoutDirection.Ltr,
                canvas = canvas,
                size = Size(w.toFloat(), h.toFloat()),
            ) {
                drawText(textLayoutResult = layoutResult)
            }
            return bitmap
        }
    }
}
