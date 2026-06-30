package dev.piotrprus.particleemitter

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

/**
 * A Compose layout-based particle emitter whose particles are arbitrary `@Composable` content.
 *
 * Each particle hosts the composable supplied by [EmitterConfig.particle] (text, an image, an icon,
 * a whole component…) and is moved with `Modifier.offset` along a kinematic trajectory while spinning
 * via `graphicsLayer`. This is the right choice when particles must be real UI; for large counts of
 * lightweight shapes, prefer the `Canvas`-based [CanvasParticleEmitter].
 *
 * The emitter performs a single finite run of [EmitterConfig.particlesCount] particles — emitted all
 * at once or staggered over [EmitterConfig.emitDurationMillis] — and removes each particle when its
 * lifespan ends. To replay, supply a new [config] instance. See [MultiEmitter] to chain several runs.
 *
 * @param modifier the [Modifier] applied to the emitter container.
 * @param config the [EmitterConfig] describing count, timing, physics, and the particle composable.
 * @param onAnimationFinished invoked once, after the last particle of the run has completed its
 * lifespan.
 */
@Composable
fun ParticlesEmitter(
    modifier: Modifier = Modifier,
    config: EmitterConfig,
    onAnimationFinished: () -> Unit
) {
    val particles = remember(config) { mutableStateListOf<Particle>() }

    BoxWithConstraints(modifier = modifier) {
        val constraintsScope = this
        val startingPoint: IntOffset by remember(config) {
            mutableStateOf(
                IntOffset(
                    x = if (config.randomStartPoint) Random.nextInt(
                        0,
                        constraintsScope.maxWidth.value.toInt()
                    ) else constraintsScope.maxWidth.value.toInt() / 2,
                    y = if (config.randomStartPoint) Random.nextInt(
                        0,
                        constraintsScope.maxHeight.value.toInt()
                    ) else constraintsScope.maxHeight.value.toInt() / 2
                )
            )
        }

        LaunchedEffect(config) {
            when (config.emitDurationMillis) {
                0L -> {
                    particles.addAll(List(config.particlesCount) {
                        Particle(
                            id = it.toString(),
                            angle = config.spread.random(),
                            initialForce = config.initialForce,
                            gravityStrength = config.gravityStrength,
                            gravityAngle = config.gravityAngle,
                            lifespanMillis = config.particleLifespanMillis,
                            maxHorizontalDisplacementDp = config.maxHorizontalDisplacementDp,
                            rotationMultiplier = config.rotationMultiplier,
                            content = config.particle
                        )
                    })
                }

                else -> {
                    List(config.particlesCount) {
                        Particle(
                            id = it.toString(),
                            angle = config.spread.random(),
                            initialForce = config.initialForce,
                            gravityStrength = config.gravityStrength,
                            gravityAngle = config.gravityAngle,
                            lifespanMillis = config.particleLifespanMillis,
                            maxHorizontalDisplacementDp = config.maxHorizontalDisplacementDp,
                            rotationMultiplier = config.rotationMultiplier,
                            content = config.particle
                        )
                    }.onEach { item ->
                        particles.add(item)
                        delay(config.emitDurationMillis / config.particlesCount)
                    }
                }
            }
        }

        particles.forEach { item ->
            key(item.id) {
                SingleParticleContainer(
                    particle = item,
                    startingPoint = startingPoint,
                    onLifeEnded = {
                        particles.remove(item)
                        if (item.id == "${config.particlesCount - 1}") {
                            onAnimationFinished()
                        }
                    })
            }
        }
    }
}

/**
 * Renders and animates one [Particle] for [ParticlesEmitter].
 *
 * Drives the particle's position with projectile kinematics — initial velocity from
 * [Particle.initialForce] and [Particle.angle], plus constant gravity acceleration derived from
 * [Particle.gravityStrength] and [Particle.gravityAngle] — applies a looping rotation scaled by
 * [Particle.rotationMultiplier], and calls [onLifeEnded] once [Particle.lifespanMillis] elapses.
 * Exposed mainly as a building block; most callers use [ParticlesEmitter] or [MultiEmitter].
 *
 * @param particle the particle state to render.
 * @param startingPoint the emission origin, in pixels, within the parent bounds.
 * @param onLifeEnded invoked when the particle's lifespan ends so the host can remove it.
 */
@Composable
internal fun SingleParticleContainer(
    particle: Particle,
    startingPoint: IntOffset = IntOffset.Zero,
    onLifeEnded: () -> Unit
) {
    val gravityDp = LocalDensity.current.density * 386 // 386 is gravity force in inch/s2
    val gravityAccelX = gravityDp * particle.gravityStrength * -sin(particle.gravityRadians).toFloat()
    val gravityAccelY = gravityDp * particle.gravityStrength * cos(particle.gravityRadians).toFloat()

    val time by produceState(0.0) {
        while (true) {
            delay(16) // 16 millis is time of 1 frame in 60frame/sec
            value += 16.0 / 1000
        }
    }
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f * particle.rotationMultiplier,
        animationSpec = infiniteRepeatable(
            animation = tween(particle.lifespanMillis.toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        delay(particle.lifespanMillis + Random.nextLong(-100, 100))
        onLifeEnded()
    }

    Box(modifier = Modifier
        .offset {
            IntOffset(
                x = (startingPoint.x + time * particle.initialForce * sin(particle.radians) + 0.5 * gravityAccelX * time.pow(2)).coerceIn(
                    minimumValue = if (particle.maxHorizontalDisplacementDp == 0) {
                        Double.MIN_VALUE
                    } else {
                        (startingPoint.x - particle.maxHorizontalDisplacementDp).toDouble()
                    },
                    maximumValue = if (particle.maxHorizontalDisplacementDp == 0) {
                        Double.MAX_VALUE
                    } else {
                        (startingPoint.x + particle.maxHorizontalDisplacementDp).toDouble()
                    }
                ).dp.roundToPx(),
                y = (startingPoint.y - particle.initialForce * cos(particle.radians) * time + 0.5 * gravityAccelY * time.pow(
                    2
                )).dp.roundToPx()
            )
        }
        .graphicsLayer {
            rotationZ = 360 * rotationAnim
        }
    ) {
        particle.content()
    }
}
