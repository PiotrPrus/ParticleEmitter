package dev.piotrprus.particleemitter

import androidx.compose.runtime.Composable

/**
 * @param gravityMultiplier value in m/s2 of acceleration for particles. Negative values represent gravity towards top of the screen (sky)
 * @param particleLifespanMillis represent duration of each particle being presented on the screen
 * @param particlesCount number of particles emitted in one run
 * @param emitDurationMillis duration of animation in milliseconds. This is duration of emitter, not the whole animation.
 * The duration of entire animation will be [emitDurationMillis] + [particleLifespanMillis] of last emitted [Particle]
 * @param rotationMultiplier TBD
 * @param initialForce theoretically in Newtons, but treat it rather as multiplier with default value 10
 * @param spread is a range of available angles that is applied to each particle randomly. The angle 0deg is pointing top of the screen (vertical)
 * @param maxHorizontalDisplacementDp is an integer that represents the horizontal boundary to which particle can move. If 0, there is no boundaries.
 */

data class EmitterConfig(
    val id: String = "",
    val particlesCount: Int = 10,
    val emitDurationMillis: Long = 0L,
    val particleLifespanMillis: Long = 2000L,
    val initialForce: Int = 100,
    val gravityMultiplier: Float = 1f,
    val spread: IntRange = IntRange(-180, 180),
    val maxHorizontalDisplacementDp: Int = 2000,
    val rotationMultiplier: Float = 1f,
    val randomStartPoint: Boolean = true,
    val particle: @Composable () -> Unit
)
