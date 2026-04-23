package dev.piotrprus.particleemitter.sample

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Entry point for the iOS sample app.
 *
 * Called from Swift via `MainViewControllerKt.MainViewController()` and
 * embedded in a SwiftUI view using `UIViewControllerRepresentable`.
 */
fun MainViewController() = ComposeUIViewController { SamplesNavigation() }
