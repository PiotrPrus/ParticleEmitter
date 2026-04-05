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

@Composable
fun MultiEmitter(
    modifier: Modifier,
    emitterCount: Int,
    emitterDelay: Long,
    emitterConfig: EmitterConfig,
    onAnimationFinished: () -> Unit = {}
) {

    val emitters = remember { mutableStateListOf<EmitterConfig>() }

    LaunchedEffect(Unit) {
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