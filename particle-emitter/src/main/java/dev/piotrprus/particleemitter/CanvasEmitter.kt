package dev.piotrprus.particleemitter

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.ui.draw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CanvasParticleEmitter(
    modifier: Modifier,
    config: CanvasEmitterConfig,
    onLastParticleEmitted: () -> Unit = {},
) {

    val itemsToAnimate = remember { mutableStateOf<List<CanvasParticle>>(emptyList()) }
    val lastFrameTime = remember { mutableStateOf(0L) }
    val pendingParticles = remember { mutableStateOf(0f) }
    val emitterStartTime = remember { mutableStateOf(0L) }
    val emissionEnded = remember { mutableStateOf(false) }
    val currentConfig by rememberUpdatedState(config)
    val currentOnLastParticleEmitted by rememberUpdatedState(onLastParticleEmitted)
    val boundsWidth = remember { mutableStateOf(0.dp) }
    val boundsHeight = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        while (true) {
            withContext(Dispatchers.IO) {
                withFrameNanos { frameNano ->
                    val cfg = currentConfig

                    val deltaSeconds: Float
                    val newParticles = if (lastFrameTime.value == 0L) {
                        deltaSeconds = 0.016f
                        lastFrameTime.value = frameNano
                        emitterStartTime.value = frameNano
                        emptyList()
                    } else {
                        deltaSeconds = ((frameNano - lastFrameTime.value) / 1_000_000_000.0)
                            .toFloat().coerceIn(0.001f, 0.1f)
                        lastFrameTime.value = frameNano

                        val duration = cfg.emitDurationMillis
                        val elapsedMs = (frameNano - emitterStartTime.value) / 1_000_000L
                        if (duration != null && !emissionEnded.value && elapsedMs >= duration) {
                            emissionEnded.value = true
                            currentOnLastParticleEmitted()
                            emptyList()
                        } else if (emissionEnded.value) {
                            emptyList()
                        } else {
                            pendingParticles.value += (cfg.particlePerSecond * deltaSeconds)
                            val count = pendingParticles.value.toInt()
                            pendingParticles.value -= count
                            createParticles(cfg, count)
                        }
                    }

                    val dt = deltaSeconds
                    val w = boundsWidth.value
                    val h = boundsHeight.value

                    itemsToAnimate.value = (itemsToAnimate.value + newParticles).mapNotNull { particle ->
                        val playTime = frameNano - particle.startTime
                        if (playTime > particle.lifespan.times(1_000_000L)) {
                            null
                        } else {
                            val newScale =
                                particle.scaleAnimConfig.getValueFromNanos(playTimeNanos = playTime)
                            val newAlpha =
                                particle.alphaAnimConfig.getValueFromNanos(playTimeNanos = playTime)

                            if (particle.stuck) {
                                particle.copy(scale = newScale, alpha = newAlpha)
                            } else {
                                // Incremental velocity: v += a * dt
                                var vx = particle.velocityX + particle.gravityX * dt
                                var vy = particle.velocityY + particle.gravityY * dt

                                // Incremental position: p += v * dt
                                var x = particle.currentPosition.x + vx * dt
                                var y = particle.currentPosition.y + vy * dt

                                var stuck = false

                                if (w > 0.dp && h > 0.dp) {
                                    val halfW = particle.size.width / 2
                                    val halfH = particle.size.height / 2
                                    val minX = halfW
                                    val maxX = w - halfW
                                    val minY = halfH
                                    val maxY = h - halfH

                                    when (cfg.edgeBehavior) {
                                        is EdgeBehavior.None -> { /* particles pass through */ }
                                        is EdgeBehavior.Bounce -> {
                                            val damping = cfg.edgeBehavior.damping
                                            if (x < minX) {
                                                x = minX + (minX - x)
                                                vx = -vx * damping
                                            } else if (x > maxX) {
                                                x = maxX - (x - maxX)
                                                vx = -vx * damping
                                            }
                                            if (y < minY) {
                                                y = minY + (minY - y)
                                                vy = -vy * damping
                                            } else if (y > maxY) {
                                                y = maxY - (y - maxY)
                                                vy = -vy * damping
                                            }
                                        }
                                        is EdgeBehavior.Stick -> {
                                            if (x < minX || x > maxX || y < minY || y > maxY) {
                                                x = x.coerceIn(minX, maxX)
                                                y = y.coerceIn(minY, maxY)
                                                vx = 0.dp
                                                vy = 0.dp
                                                stuck = true
                                            }
                                        }
                                        is EdgeBehavior.Wrap -> {
                                            if (x < 0.dp - halfW) x += w + particle.size.width
                                            else if (x > w + halfW) x -= w + particle.size.width
                                            if (y < 0.dp - halfH) y += h + particle.size.height
                                            else if (y > h + halfH) y -= h + particle.size.height
                                        }
                                    }
                                }

                                particle.copy(
                                    currentPosition = DpOffset(x, y),
                                    velocityX = vx,
                                    velocityY = vy,
                                    scale = newScale,
                                    alpha = newAlpha,
                                    stuck = stuck,
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    Spacer(modifier = modifier
        .fillMaxSize()
        .onSizeChanged { size ->
            with(density) {
                boundsWidth.value = size.width.toDp()
                boundsHeight.value = size.height.toDp()
            }
        }
        .drawBehind {
            for (canvasParticle in itemsToAnimate.value) {
                draw(canvasParticle = canvasParticle)
            }
        })
}

private fun createParticles(
    config: CanvasEmitterConfig,
    count: Int
): List<CanvasParticle> {
    val gravityRadians = Math.toRadians(config.gravityAngle.toDouble())
    val gravityXDp = (config.gravityStrength * -sin(gravityRadians).toFloat()).dp
    val gravityYDp = (config.gravityStrength * cos(gravityRadians).toFloat()).dp

    return List(count) {
        val angle = config.spread.random()
        val radians = Math.toRadians(angle.toDouble())
        val force = config.initialForce.random()
        val vx = (force * sin(radians).toFloat()).dp
        val vy = (-force * cos(radians).toFloat()).dp

        CanvasParticle(
            shape = config.particleShapes.random(),
            color = config.colors.random(),
            startPoint = config.startPoint,
            lifespan = config.lifespanRange.random(),
            blendMode = config.blendMode,
            size = config.particleSizes.random(),
            angle = angle,
            velocityX = vx,
            velocityY = vy,
            gravityX = gravityXDp,
            gravityY = gravityYDp,
            fadeOutDuration = config.fadeOutTime.random(),
            rotation = config.rotationRange.random(),
            alphaEasing = config.alphaEasing,
            scaleDuration = config.scaleTime.random(),
            scaleEasing = config.scaleEasing,
            targetScale = config.targetScaleRange.random().toFloat(),
            startScale = config.startScaleRange.random().toFloat(),
        )
    }
}
