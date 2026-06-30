package dev.piotrprus.particleemitter

import androidx.compose.runtime.Composable

/**
 * Configuration for [ParticlesEmitter] (and, in turn, [MultiEmitter]) — the layout-based emitter
 * whose particles are arbitrary `@Composable` content supplied via [particle].
 *
 * Unlike [CanvasEmitterConfig], this config emits a fixed [particlesCount] of particles in a single
 * run rather than continuously: all at once when [emitDurationMillis] is `0`, or spread evenly over
 * that duration otherwise. Each particle follows a kinematic trajectory derived from [initialForce],
 * a [spread]-sampled launch angle, and the [gravityStrength]/[gravityAngle] gravity vector.
 *
 * @param id identifier used to key the particle in composition; set by [MultiEmitter] to
 * distinguish concurrent emitters. Leave as the default for a single emitter.
 * @param particlesCount number of particles emitted in one run.
 * @param emitDurationMillis how long emission takes, in milliseconds. `0` emits every particle at
 * once; a positive value staggers emission evenly across the duration. This is the duration of the
 * emitter, not of the whole animation — the entire animation lasts [emitDurationMillis] plus the
 * [particleLifespanMillis] of the last emitted [Particle].
 * @param particleLifespanMillis how long each particle stays on screen, in milliseconds.
 * @param initialForce launch-velocity multiplier for each particle (default `100`); higher values
 * fling particles farther from the start point.
 * @param gravityStrength strength of gravitational force applied to particles in Dp/s². A value of 0 means no gravity. Higher values create stronger pull.
 * @param gravityAngle direction of gravity in degrees. 0 degrees points downward (bottom of the screen), 90 degrees points left, -90 degrees points right, 180 degrees points upward.
 * @param spread range of launch angles (degrees) sampled randomly per particle. 0° points toward the top of the screen (vertical).
 * @param maxHorizontalDisplacementDp horizontal boundary, in Dp, that a particle may travel from its start point. `0` means no boundary.
 * @param rotationMultiplier scales how much each particle spins over its lifespan: `0f` disables
 * rotation, `1f` (default) is one full back-and-forth turn, higher values spin faster.
 * @param randomStartPoint when `true` (default) each particle starts at a random point within the
 * emitter bounds; when `false` all particles start from the center.
 * @param particle the `@Composable` content drawn for every particle.
 */
data class EmitterConfig(
    val id: String = "",
    val particlesCount: Int = 10,
    val emitDurationMillis: Long = 0L,
    val particleLifespanMillis: Long = 2000L,
    val initialForce: Int = 100,
    val gravityStrength: Float = 1f,
    val gravityAngle: Int = 0,
    val spread: IntRange = IntRange(-180, 180),
    val maxHorizontalDisplacementDp: Int = 2000,
    val rotationMultiplier: Float = 1f,
    val randomStartPoint: Boolean = true,
    val particle: @Composable () -> Unit
)
