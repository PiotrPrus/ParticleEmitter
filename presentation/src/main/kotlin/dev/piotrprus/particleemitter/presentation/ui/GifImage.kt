package dev.piotrprus.particleemitter.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.useResource
import kotlinx.coroutines.delay
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

/**
 * Plays an animated GIF from classpath resources by decoding frames with the
 * bundled Skia codec — no extra image-loading dependency needed on desktop.
 */
@Composable
fun GifImage(
    resourcePath: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val codec = remember(resourcePath) {
        val bytes = useResource(resourcePath) { it.readBytes() }
        Codec.makeFromData(Data.makeFromBytes(bytes))
    }
    val frameIndex by produceState(initialValue = 0, codec) {
        if (codec.frameCount <= 1) return@produceState
        val durations = IntArray(codec.frameCount) { index ->
            codec.getFrameInfo(index).duration.coerceAtLeast(20)
        }
        while (true) {
            for (index in 0 until codec.frameCount) {
                value = index
                delay(durations[index].toLong())
            }
        }
    }
    val bitmap = remember(codec) {
        Bitmap().apply { allocPixels(codec.imageInfo) }
    }
    val frame = remember(codec, frameIndex) {
        codec.readPixels(bitmap, frameIndex)
        bitmap.asComposeImageBitmap()
    }
    Image(
        bitmap = frame,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
}
