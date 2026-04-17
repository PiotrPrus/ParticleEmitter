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
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.ui.draw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CanvasParticleEmitter(modifier: Modifier, config: CanvasEmitterConfig) {

    val itemsToAnimate = remember { mutableStateOf<List<CanvasParticle>>(emptyList()) }
    val lastFrameTime = remember { mutableStateOf(0L) }
    val pendingParticles = remember { mutableStateOf(0f) }
    val currentConfig by rememberUpdatedState(config)

    LaunchedEffect(Unit) {
        while (true) {
            withContext(Dispatchers.IO) {
                withFrameNanos { frameNano ->
                    val cfg = currentConfig
                    val newParticles = if (lastFrameTime.value == 0L) {
                        lastFrameTime.value = frameNano
                        emptyList()
                    } else {
                        val deltaSeconds = (frameNano - lastFrameTime.value) / 1_000_000_000.0
                        lastFrameTime.value = frameNano
                        pendingParticles.value += (cfg.particlePerSecond * deltaSeconds).toFloat()
                        val count = pendingParticles.value.toInt()
                        pendingParticles.value -= count
                        createParticles(cfg, count)
                    }

                    itemsToAnimate.value = (itemsToAnimate.value + newParticles).mapNotNull { canvasParticle ->
                        val playTime = frameNano - canvasParticle.startTime
                        if (playTime > canvasParticle.lifespan.times(1_000_000L)) {
                            null
                        } else {
                            val elapsedSeconds = playTime / 1_000_000_000.0
                            val newPosition = canvasParticle.positionAt(elapsedSeconds)
                            val newScale =
                                canvasParticle.scaleAnimConfig.getValueFromNanos(playTimeNanos = playTime)
                            val newAlpha =
                                canvasParticle.alphaAnimConfig.getValueFromNanos(playTimeNanos = playTime)
                            canvasParticle.copy(
                                currentPosition = newPosition,
                                scale = newScale,
                                alpha = newAlpha,
                            )
                        }
                    }
                }
            }
        }
    }


    Spacer(modifier = modifier
        .fillMaxSize()
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
    val gravityXDp = (config.gravityStrength * sin(gravityRadians).toFloat()).dp
    val gravityYDp = (config.gravityStrength * cos(gravityRadians).toFloat()).dp

    return List(count) {
        val angle = config.spread.random()
        val radians = Math.toRadians(angle.toDouble())
        val force = config.initialForce.random()
        val vx = (force * sin(radians).toFloat()).dp
        val vy = (-force * cos(radians).toFloat()).dp

        CanvasParticle(
            id = UUID.randomUUID().toString(),
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
