package dev.piotrprus.particleemitter.sample

import androidx.compose.ui.window.ComposeUIViewController
import dev.piotrprus.particleemitter.sample.ui.theme.ParticleEmitterTheme

fun MainViewController() = ComposeUIViewController {
    ParticleEmitterTheme {
        SamplesNavigation()
    }
}
