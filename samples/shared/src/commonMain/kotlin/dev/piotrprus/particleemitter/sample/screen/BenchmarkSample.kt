package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape

private const val MAX_EMITTERS = 10
private const val PARTICLES_PER_SECOND = 1000
private const val COLUMNS = 2
private const val ROWS = 5

@Composable
fun BenchmarkSample() {
    var activeCount by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            repeat(ROWS) { row ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    repeat(COLUMNS) { col ->
                        val index = row * COLUMNS + col
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        ) {
                            if (index < activeCount) {
                                BenchmarkEmitter()
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (activeCount > 0) activeCount-- },
                enabled = activeCount > 0
            ) { Text("-", style = MaterialTheme.typography.titleLarge) }

            Text(
                text = "Active: $activeCount / $MAX_EMITTERS",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = { if (activeCount < MAX_EMITTERS) activeCount++ },
                enabled = activeCount < MAX_EMITTERS
            ) { Text("+", style = MaterialTheme.typography.titleLarge) }
        }
    }
}

@Composable
private fun BenchmarkEmitter() {
    val density = LocalDensity.current
    var cellSize by remember { mutableStateOf(DpSize.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onPlaced {
                cellSize = with(density) {
                    DpSize(it.size.width.toDp(), it.size.height.toDp())
                }
            }
    ) {
        if (cellSize != DpSize.Zero) {
            CanvasParticleEmitter(
                modifier = Modifier.fillMaxSize(),
                config = CanvasEmitterConfig(
                    particlePerSecond = PARTICLES_PER_SECOND,
                    emitterCenter = DpOffset(cellSize.width / 2, cellSize.height / 2),
                    startRegionShape = CanvasEmitterConfig.Shape.POINT,
                    startRegionSize = DpSize(1.dp, 1.dp),
                    particleShapes = listOf(ParticleShape.Circle),
                    lifespanRange = IntRange(800, 1200),
                    fadeOutTime = IntRange(400, 800),
                    scaleTime = IntRange(400, 800),
                    colors = listOf(
                        Color(0xff53FF00),
                        Color(0xffE5FF5E),
                        Color(0xff4AC2FF),
                        Color(0xffFF6EC7),
                    ),
                    particleSizes = listOf(DpSize(4.dp, 4.dp)),
                    initialForce = IntRange(40, 120),
                    spread = IntRange(-180, 180),
                    startScaleRange = IntRange(1, 2),
                    targetScaleRange = IntRange(0, 1),
                )
            )
        }
    }
}
