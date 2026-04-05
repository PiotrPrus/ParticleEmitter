package dev.piotrprus.particleemitter

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path

sealed interface ParticleShape {
    object Circle: ParticleShape
    data class PathShape(val shapePath: Path): ParticleShape
    data class Image(val imageBitmap: ImageBitmap): ParticleShape
}
