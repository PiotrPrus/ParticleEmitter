package dev.piotrprus.particleemitter

import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

data class CanvasParticle(
    val id: String,
    val shape: ParticleShape,
    val color: Color,
    val startPoint: DpOffset,
    val lifespan: Int,
    val fadeOutDuration: Int,
    val scaleDuration: Int,
    val size: DpSize,
    val velocityX: Dp,
    val velocityY: Dp,
    val gravityX: Dp,
    val gravityY: Dp,
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
    val stuck: Boolean = false,
    val scaleAnimConfig: TargetBasedAnimation<Float, AnimationVector1D> = TargetBasedAnimation(
        animationSpec = tween(durationMillis = scaleDuration, easing = scaleEasing),
        typeConverter = Float.VectorConverter,
        initialValue = startScale,
        targetValue = targetScale,
    ),
    val alphaAnimConfig: TargetBasedAnimation<Float, AnimationVector1D> = TargetBasedAnimation(
        animationSpec = tween(durationMillis = fadeOutDuration, easing = alphaEasing),
        typeConverter = Float.VectorConverter,
        initialValue = 1f,
        targetValue = 0f,
    ),
)
