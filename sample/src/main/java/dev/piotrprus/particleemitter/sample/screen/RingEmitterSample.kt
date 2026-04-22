package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape

private const val RingDiameterDp = 180
private val ParticleColors = listOf(
    Color(0xFFFFC93C),
    Color(0xFFFF6B6B),
    Color(0xFFB3E0FF),
    Color(0xFFFFFFFF),
)

@Composable
fun RingEmitterSample() {
    val density = LocalDensity.current

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var hideInside by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
    ) {
        if (containerSize != IntSize.Zero) {
            val widthDp = with(density) { containerSize.width.toDp() }
            val heightDp = with(density) { containerSize.height.toDp() }
            val center = DpOffset(widthDp / 2, heightDp / 2)

            CanvasParticleEmitter(
                modifier = Modifier.fillMaxSize(),
                config = CanvasEmitterConfig(
                    particlePerSecond = 180,
                    emitterCenter = center,
                    startRegionShape = CanvasEmitterConfig.Shape.OVAL,
                    startRegionSize = DpSize(RingDiameterDp.dp, RingDiameterDp.dp),
                    particleShapes = listOf(ParticleShape.Circle),
                    lifespanRange = IntRange(1500, 2500),
                    fadeOutTime = IntRange(1500, 2500),
                    scaleTime = IntRange(1500, 2500),
                    colors = ParticleColors,
                    particleSizes = listOf(DpSize(4.dp, 4.dp), DpSize(6.dp, 6.dp)),
                    spread = IntRange(0, 360),
                    initialForce = IntRange(40, 90),
                    startScaleRange = IntRange(1, 1),
                    targetScaleRange = IntRange(1, 2),
                    gravityStrength = 0f,
                    hideInStartRegion = hideInside,
                )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(RingDiameterDp.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Switch(checked = hideInside, onCheckedChange = { hideInside = it })
                Text(
                    text = "hideInStartRegion: $hideInside",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                text = "Toggle to see particles crossing the ring interior appear / disappear.",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
