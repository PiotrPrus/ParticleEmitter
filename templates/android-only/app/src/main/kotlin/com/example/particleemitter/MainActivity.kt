package com.example.particleemitter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                    CenteredParticles()
                }
            }
        }
    }
}

/**
 * The simplest possible use of the library: a single [CanvasParticleEmitter] centered on screen,
 * emitting a gentle upward burst. Tweak the [CanvasEmitterConfig] below to make it your own.
 */
@Composable
private fun CenteredParticles() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val center = DpOffset(maxWidth / 2, maxHeight / 2)
        CanvasParticleEmitter(
            modifier = Modifier.fillMaxSize(),
            config = CanvasEmitterConfig(
                particlePerSecond = 60,
                emitterCenter = center,
                startRegionShape = CanvasEmitterConfig.Shape.POINT,
                startRegionSize = DpSize(0.dp, 0.dp),
                particleShapes = listOf(ParticleShape.Circle),
                lifespanRange = IntRange(1500, 2500),
                fadeOutTime = IntRange(1000, 2000),
                scaleTime = IntRange(1000, 2000),
                colors = listOf(
                    Color(0xFF64B5F6),
                    Color(0xFFFFC93C),
                    Color(0xFFFFFFFF),
                ),
                particleSizes = listOf(DpSize(6.dp, 6.dp), DpSize(10.dp, 10.dp)),
                spread = IntRange(-30, 30),
                initialForce = IntRange(60, 140),
            ),
        )
    }
}
