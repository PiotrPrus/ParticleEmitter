package dev.piotrprus.particleemitter

import androidx.compose.runtime.Composable

/**
 * State of a single particle managed by [ParticlesEmitter] and rendered by [SingleParticleContainer].
 *
 * Created by the emitter from an [EmitterConfig], with per-particle values such as [angle] already
 * sampled from the config's ranges. You normally do not construct this directly.
 *
 * @property id identifier unique within an emitter run, used as the composition key.
 * @property angle launch angle in degrees (0° points toward the top of the screen).
 * @property initialForce launch-velocity multiplier (see [EmitterConfig.initialForce]).
 * @property gravityStrength gravity magnitude in Dp/s² (`0` disables gravity).
 * @property gravityAngle gravity direction in degrees (0° points down).
 * @property lifespanMillis how long the particle lives, in milliseconds.
 * @property maxHorizontalDisplacementDp horizontal travel limit in Dp; `0` means unbounded.
 * @property rotationMultiplier scales the looping rotation applied while the particle is alive.
 * @property content the `@Composable` rendered for this particle.
 */
data class Particle(
    val id: String,
    val angle: Int,
    val initialForce: Int,
    val gravityStrength: Float,
    val gravityAngle: Int,
    val lifespanMillis: Long,
    val maxHorizontalDisplacementDp: Int,
    val rotationMultiplier: Float,
    val content: @Composable () -> Unit
) {
    /** [angle] converted to radians, for use in the trajectory equations. */
    val radians
        get() = Math.toRadians(angle.toDouble())

    /** [gravityAngle] converted to radians, for decomposing gravity into x/y components. */
    val gravityRadians
        get() = Math.toRadians(gravityAngle.toDouble())
}
