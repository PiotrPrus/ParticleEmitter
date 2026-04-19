package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.EdgeBehavior
import dev.piotrprus.particleemitter.ParticleShape
import dev.piotrprus.particleemitter.sample.R
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState

@Composable
fun StickyEdgesSample() {
    val context = LocalContext.current
    val imageBitmap =
        remember { ImageBitmap.imageResource(context.resources, R.drawable.star_four) }

    var selectedBehavior by remember { mutableStateOf<EdgeBehavior>(EdgeBehavior.Bounce()) }

    val behaviors = listOf(
        "None" to EdgeBehavior.None,
        "Bounce" to EdgeBehavior.Bounce(),
        "Stick" to EdgeBehavior.Stick,
        "Wrap" to EdgeBehavior.Wrap,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        CanvasParticleEmitter(
            modifier = Modifier.fillMaxSize(),
            CanvasEmitterConfig(
                particlePerSecond = 10,
                emitterCenter = DpOffset(200.dp, 200.dp),
                startRegionShape = CanvasEmitterConfig.Shape.POINT,
                startRegionSize = DpSize.Zero,
                particleShapes = listOf(
                    ParticleShape.Circle,
                    ParticleShape.Image(imageBitmap)
                ),
                lifespanRange = IntRange(8000, 12000),
                colors = listOf(
                    Color(0xffFF6B6B), Color(0xffFFE66D), Color(0xff4ECDC4),
                    Color(0xff45B7D1), Color(0xffFF9A56)
                ),
                scaleEasing = EaseOutCubic,
                particleSizes = listOf(DpSize(8.dp, 8.dp), DpSize(12.dp, 12.dp)),
                initialForce = IntRange(80, 160),
                spread = IntRange(-45, 45),
                fadeOutTime = IntRange(7000, 11000),
                rotationRange = IntRange(-180, 180),
                scaleTime = IntRange(500, 1000),
                targetScaleRange = IntRange(0, 1),
                startScaleRange = IntRange(1, 2),
                gravityStrength = 100f,
                gravityAngle = 0,
                edgeBehavior = selectedBehavior,
            )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Edge behavior:",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                behaviors.forEach { (label, behavior) ->
                    FilterChip(
                        selected = selectedBehavior::class == behavior::class,
                        onClick = { selectedBehavior = behavior },
                        label = { Text(label) }
                    )
                }
            }
        }
    }
}
