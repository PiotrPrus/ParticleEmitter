package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

private const val MAX_STEPS = 10
private const val PARTICLES_PER_STEP = 1000

@Composable
fun SingleEmitterBenchmarkSample() {
    var step by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    var canvasSize by remember { mutableStateOf(DpSize.Zero) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onPlaced {
                    canvasSize = with(density) {
                        DpSize(it.size.width.toDp(), it.size.height.toDp())
                    }
                }
        ) {
            if (step > 0 && canvasSize != DpSize.Zero) {
                CanvasParticleEmitter(
                    modifier = Modifier.fillMaxSize(),
                    config = CanvasEmitterConfig(
                        particlePerSecond = step * PARTICLES_PER_STEP,
                        emitterCenter = DpOffset(canvasSize.width / 2, canvasSize.height / 2),
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${step * PARTICLES_PER_STEP} particles/sec (step $step / $MAX_STEPS)",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = step.toFloat(),
                onValueChange = { step = it.toInt() },
                valueRange = 0f..MAX_STEPS.toFloat(),
                steps = MAX_STEPS - 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
