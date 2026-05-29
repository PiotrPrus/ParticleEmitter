package dev.piotrprus.particleemitter.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.piotrprus.particleemitter.sample.SamplesNavigation
import dev.piotrprus.particleemitter.sample.ui.theme.ParticleEmitterTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        ParticleEmitterTheme {
            SamplesNavigation()
        }
    }
}
