package dev.piotrprus.particleemitter.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.useResource
import org.jetbrains.skia.Image as SkiaImage

/** Decodes a static image (PNG/JPG) from classpath resources into an [ImageBitmap]. */
@Composable
fun rememberResourceImage(resourcePath: String): ImageBitmap = remember(resourcePath) {
    val bytes = useResource(resourcePath) { it.readBytes() }
    SkiaImage.makeFromEncoded(bytes).toComposeImageBitmap()
}

/** Draws a static image from classpath resources. */
@Composable
fun PhotoImage(
    resourcePath: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    Image(
        bitmap = rememberResourceImage(resourcePath),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
}
