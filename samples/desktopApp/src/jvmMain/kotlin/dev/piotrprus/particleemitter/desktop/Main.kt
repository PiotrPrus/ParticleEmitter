package dev.piotrprus.particleemitter.desktop

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.piotrprus.particleemitter.sample.SamplesNavigation

fun main() = application {
    val state = rememberWindowState(size = DpSize(420.dp, 760.dp))
    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "ParticleEmitter – Desktop Sample",
    ) {
        SamplesNavigation()
    }
}
