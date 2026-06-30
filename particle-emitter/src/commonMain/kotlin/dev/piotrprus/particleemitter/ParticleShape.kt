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

/**
 * How a particle is drawn by [CanvasParticleEmitter].
 *
 * A [CanvasEmitterConfig] takes a list of shapes and picks one at random per particle, so a single
 * emitter can mix circles, images, glyphs, and custom paths.
 */
sealed interface ParticleShape {
    /** A filled circle whose radius comes from the particle's size and whose color is the particle tint. */
    object Circle : ParticleShape

    /**
     * A custom vector shape.
     * @property shapePath the [Path] to fill with the particle's color.
     */
    data class PathShape(val shapePath: Path) : ParticleShape

    /**
     * A bitmap particle. The bitmap is tinted with the particle's color via `ColorFilter.tint`.
     * @property imageBitmap the image to draw for each particle.
     */
    data class Image(val imageBitmap: ImageBitmap) : ParticleShape

    /**
     * A text or emoji particle.
     *
     * The string is rasterized once into an [ImageBitmap] (lazily, on first draw) using
     * [textMeasurer] and [textStyle], then drawn like an [Image] particle — so emitting many text
     * particles stays cheap. Obtain the measurer with `rememberTextMeasurer()`.
     *
     * @property text the string (or emoji) to render.
     * @property textStyle the [TextStyle] controlling font, size, and color of the rasterized text.
     * @property textMeasurer the [TextMeasurer] used to lay out and rasterize the text.
     */
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
