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

/**
 * The per-frame state of a single particle simulated by [CanvasParticleEmitter].
 *
 * Instances are created by the emitter from a [CanvasEmitterConfig] and recreated (via [copy]) on
 * every animation frame as the particle moves, scales, and fades. You normally never construct this
 * yourself; it is exposed because [dev.piotrprus.particleemitter.ui.draw] takes it to render the
 * particle. All spatial values are expressed in [Dp] so the simulation is density-independent.
 *
 * @property shape how the particle is drawn — see [ParticleShape].
 * @property color tint applied to the particle (also used as the path/circle fill color).
 * @property startPoint the position at which the particle was emitted.
 * @property lifespan total time the particle lives, in milliseconds.
 * @property fadeOutDuration duration of the alpha animation, in milliseconds.
 * @property scaleDuration duration of the scale animation, in milliseconds.
 * @property size the particle's draw size.
 * @property velocityX current horizontal velocity, in Dp per second.
 * @property velocityY current vertical velocity, in Dp per second.
 * @property gravityX horizontal gravity acceleration applied each frame, in Dp/s².
 * @property gravityY vertical gravity acceleration applied each frame, in Dp/s².
 * @property scaleEasing easing curve for the scale animation.
 * @property alphaEasing easing curve for the alpha (fade-out) animation.
 * @property currentPosition the particle's position on the current frame; defaults to [startPoint].
 * @property startTime frame timestamp (nanoseconds) at which the particle was emitted.
 * @property blendMode the [BlendMode] used when drawing the particle.
 * @property scale current scale factor on this frame.
 * @property alpha current opacity on this frame, in `0f..1f`.
 * @property angle the emission angle, in degrees.
 * @property rotation rotation applied while drawing, in degrees.
 * @property targetScale scale reached at the end of the scale animation.
 * @property startScale scale at emission time.
 * @property stuck `true` once the particle has stuck to an edge (see [EdgeBehavior.Stick]); a stuck
 * particle stops moving but keeps animating scale and alpha.
 * @property scaleAnimConfig precomputed scale animation, driven by elapsed play time.
 * @property alphaAnimConfig precomputed alpha animation, driven by elapsed play time.
 */
internal data class CanvasParticle(
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
    val startTime: Long,
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
