package dev.piotrprus.particleemitter.sample.screen

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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape
import kotlin.math.roundToInt

private val EmojiSet = listOf("\uD83C\uDF81", "\uD83C\uDF89", "\uD83E\uDD73", "\uD83C\uDF8A")

@Composable
fun EmojiRainSample() {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var rate by remember { mutableFloatStateOf(30f) }

    val emojiShapes = remember(textMeasurer) {
        EmojiSet.map { emoji ->
            ParticleShape.Text(
                text = emoji,
                textStyle = TextStyle(fontSize = 28.sp),
                textMeasurer = textMeasurer,
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
    ) {
        if (containerSize != IntSize.Zero) {
            val widthDp = with(density) { containerSize.width.toDp() }
            CanvasParticleEmitter(
                modifier = Modifier.fillMaxSize(),
                config = CanvasEmitterConfig(
                    particlePerSecond = rate.roundToInt(),
                    emitterCenter = DpOffset(widthDp / 2, 0.dp),
                    startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
                    startRegionSize = DpSize(widthDp, 20.dp),
                    particleShapes = emojiShapes,
                    lifespanRange = IntRange(3000, 5000),
                    fadeOutTime = IntRange(100_000, 100_000),
                    scaleTime = IntRange(500, 1000),
                    colors = listOf(Color.White),
                    particleSizes = listOf(DpSize(28.dp, 28.dp)),
                    spread = IntRange(165, 195),
                    initialForce = IntRange(60, 140),
                    rotationRange = IntRange(-60, 30),
                    startScaleRange = IntRange(1, 1),
                    targetScaleRange = IntRange(1, 1),
                    gravityStrength = 250f,
                    gravityAngle = 0,
                )
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
        ) {
            Text(
                text = "Particles/sec: ${rate.roundToInt()}",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = rate,
                onValueChange = { rate = it },
                valueRange = 1f..80f,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
