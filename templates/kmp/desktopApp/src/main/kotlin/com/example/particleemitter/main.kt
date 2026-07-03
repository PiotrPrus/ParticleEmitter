package com.example.particleemitter

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Particle Emitter KMP Starter",
    ) {
        App()
    }
}
