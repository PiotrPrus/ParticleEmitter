package dev.piotrprus.particleemitter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * Orchestrates several [ParticlesEmitter] runs from a shared [EmitterConfig], launched one after
 * another with a fixed delay between them.
 *
 * Use it for staggered bursts — for example a confetti cannon that fires [emitterCount] waves
 * [emitterDelay] milliseconds apart. Each wave is an independent [ParticlesEmitter] using a copy of
 * [emitterConfig]; finished waves are removed automatically, and [onAnimationFinished] fires once
 * the final wave completes.
 *
 * @param modifier the [Modifier] applied to each emitter wave.
 * @param emitterCount how many emitter waves to launch in total.
 * @param emitterDelay delay, in milliseconds, between the start of consecutive waves.
 * @param emitterConfig the [EmitterConfig] shared by every wave (its `id` is overridden per wave).
 * @param onAnimationFinished invoked once after the last wave has finished its animation.
 */
@Composable
fun MultiEmitter(
    modifier: Modifier,
    emitterCount: Int,
    emitterDelay: Long,
    emitterConfig: EmitterConfig,
    onAnimationFinished: () -> Unit = {}
) {

    val emitters = remember(emitterConfig, emitterCount, emitterDelay) { mutableStateListOf<EmitterConfig>() }

    LaunchedEffect(emitterConfig, emitterCount, emitterDelay) {
        repeat(emitterCount) {
            emitters.add(emitterConfig.copy(id = it.toString()))
            delay(emitterDelay)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        emitters.forEach { config ->
            key(config.id) {
                ParticlesEmitter(
                    modifier = modifier,
                    config = config,
                    onAnimationFinished = {
                        emitters.remove(config)
                        if (config.id == "${emitterCount - 1}") {
                            onAnimationFinished()
                        }
                    }
                )
            }
        }
    }
}