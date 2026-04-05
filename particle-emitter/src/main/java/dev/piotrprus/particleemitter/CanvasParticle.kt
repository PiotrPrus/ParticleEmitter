package dev.piotrprus.particleemitter

import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import kotlin.math.cos
import kotlin.math.sin

data class CanvasParticle(
    val id: String,
    val shape: ParticleShape,
    val color: Color,
    val startPoint: DpOffset,
    val lifespan: Int,
    val fadeOutDuration: Int,
    val scaleDuration: Int,
    val size: DpSize,
    val distance: Dp,
    val easing: Easing,
    val scaleEasing: Easing,
    val alphaEasing: Easing,
    val currentPosition: DpOffset = startPoint,
    val startTime: Long = System.nanoTime(),
    val blendMode: BlendMode,
    val scale: Float = 1f,
    val alpha: Float = 1f,
    val angle: Int = 0,
    val rotation: Int = 0,
    val targetScale: Float,
    val startScale: Float,
) {
    private val radians
        get() = Math.toRadians(angle.toDouble())

    private val endPoint: DpOffset
        get() = DpOffset(
            x = startPoint.x + distance * sin(radians).toFloat(),
            y = startPoint.y - distance * cos(radians).toFloat()
        )

    val animationConfig: TargetBasedAnimation<DpOffset, AnimationVector2D>
        get() = TargetBasedAnimation(
            animationSpec = tween(durationMillis = lifespan, easing = easing),
            typeConverter = DpOffset.VectorConverter,
            initialValue = startPoint,
            targetValue = endPoint
        )
    val scaleAnimConfig: TargetBasedAnimation<Float, AnimationVector1D>
        get() = TargetBasedAnimation(
            animationSpec = tween(durationMillis = scaleDuration, easing = scaleEasing),
            typeConverter = Float.VectorConverter,
            initialValue = startScale,
            targetValue = targetScale
        )
    val alphaAnimConfig: TargetBasedAnimation<Float, AnimationVector1D>
        get() = TargetBasedAnimation(
            animationSpec = tween(durationMillis = fadeOutDuration, easing = alphaEasing),
            typeConverter = Float.VectorConverter,
            initialValue = 1f,
            targetValue = 0f
        )
}
