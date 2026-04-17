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
                            println("AAAA, last particle emitted")
                            onAnimationFinished()
                        }
                    })
            }
        }
    }
}

@Composable
fun SingleParticleContainer(
    particle: Particle,
    startingPoint: IntOffset = IntOffset.Zero,
    onLifeEnded: () -> Unit
) {
    val gravityDp = LocalDensity.current.density * 386 // 386 is gravity force in inch/s2
    val gravityAccelX = gravityDp * particle.gravityStrength * sin(particle.gravityRadians).toFloat()
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
                x = (startingPoint.x + time * particle.initialForce * sin(particle.radiants) + 0.5 * gravityAccelX * time.pow(2)).coerceIn(
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
                y = (startingPoint.y - particle.initialForce * cos(particle.radiants) * time + 0.5 * gravityAccelY * time.pow(
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
