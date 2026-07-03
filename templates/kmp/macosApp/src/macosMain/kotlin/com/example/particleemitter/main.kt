package com.example.particleemitter

import androidx.compose.ui.window.Window
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

fun main() {
    NSApplication.sharedApplication()
    Window("Particle Emitter KMP Starter") {
        App()
    }
    NSApp?.run()
}
