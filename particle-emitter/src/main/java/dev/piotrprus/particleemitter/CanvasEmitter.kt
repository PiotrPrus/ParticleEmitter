package dev.piotrprus.particleemitter

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.ui.draw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun CanvasParticleEmitter(modifier: Modifier, config: CanvasEmitterConfig) {

    val itemsToAnimate = remember(config) { mutableStateOf<List<CanvasParticle>>(emptyList()) }
    val lastFrameTime = remember(config) { mutableStateOf(0L) }
    val pendingParticles = remember(config) { mutableStateOf(0f) }

    LaunchedEffect(config) {
        while (true) {
            withContext(Dispatchers.IO) {
                withFrameNanos { frameNano ->
                    val newParticles = if (lastFrameTime.value == 0L) {
                        lastFrameTime.value = frameNano
                        emptyList()
                    } else {
                        val deltaSeconds = (frameNano - lastFrameTime.value) / 1_000_000_000.0
                        lastFrameTime.value = frameNano
                        pendingParticles.value += (config.particlePerSecond * deltaSeconds).toFloat()
                        val count = pendingParticles.value.toInt()
                        pendingParticles.value -= count
                        createParticles(config, count)
                    }

                    itemsToAnimate.value = (itemsToAnimate.value + newParticles).mapNotNull { canvasParticle ->
                        val playTime = frameNano - canvasParticle.startTime
                        if (playTime > canvasParticle.lifespan.times(1_000_000L)) {
                            null
                        } else {
                            val newPosition =
                                canvasParticle.animationConfig.getValueFromNanos(playTimeNanos = playTime)
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
): List<CanvasParticle> =
    List(count) {
        CanvasParticle(
            id = UUID.randomUUID().toString(),
            shape = config.particleShapes.random(),
            color = config.colors.random(),
            startPoint = config.startPoint,
            lifespan = config.lifespanRange.random(),
            easing = config.translateEasing,
            blendMode = config.blendMode,
            size = config.particleSizes.random(),
            angle = config.spread.random(),
            distance = config.flyDistancesDp.random().dp,
            fadeOutDuration = config.fadeOutTime.random(),
            rotation = config.rotationRange.random(),
            alphaEasing = config.alphaEasing,
            scaleDuration = config.scaleTime.random(),
            scaleEasing = config.scaleEasing,
            targetScale = config.targetScaleRange.random().toFloat(),
            startScale = config.startScaleRange.random().toFloat(),
        )
    }