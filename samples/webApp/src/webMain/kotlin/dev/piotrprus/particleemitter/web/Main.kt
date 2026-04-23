package dev.piotrprus.particleemitter.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.piotrprus.particleemitter.sample.SamplesNavigation

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        SamplesNavigation()
    }
}
