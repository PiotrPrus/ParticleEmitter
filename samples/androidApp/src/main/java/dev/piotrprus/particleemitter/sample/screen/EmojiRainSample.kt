package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private val EmojiSet = listOf("\uD83C\uDF81", "\uD83C\uDF89", "\uD83E\uDD73", "\uD83C\uDF8A")

private const val BurstRate = 80
private const val BurstDurationMs = 3_000L
private const val RainEndedVisibleMs = 2_000L

@Composable
fun EmojiRainSample() {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var sliderRate by remember { mutableFloatStateOf(30f) }
    var burstTrigger by remember { mutableIntStateOf(0) }
    var isBursting by remember { mutableStateOf(false) }
    var showRainEnded by remember { mutableStateOf(false) }

    LaunchedEffect(burstTrigger) {
        if (burstTrigger == 0) return@LaunchedEffect
        isBursting = true
        delay(BurstDurationMs)
        isBursting = false
        showRainEnded = true
        delay(RainEndedVisibleMs)
        showRainEnded = false
    }

    val rate = if (isBursting) BurstRate else sliderRate.roundToInt()

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
                    particlePerSecond = rate,
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

        AnimatedVisibility(
            visible = showRainEnded,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp),
        ) {
            Text(
                text = "Rain ended",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
        ) {
            Text(
                text = if (isBursting) "Bursting: $BurstRate/s" else "Particles/sec: ${sliderRate.roundToInt()}",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = sliderRate,
                onValueChange = { sliderRate = it },
                valueRange = 0f..80f,
                enabled = !isBursting,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = { burstTrigger++ },
                    enabled = !isBursting,
                ) {
                    Text(text = "Burst 3s")
                }
            }
        }
    }
}
