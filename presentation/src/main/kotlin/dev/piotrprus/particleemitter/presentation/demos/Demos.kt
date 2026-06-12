package dev.piotrprus.particleemitter.presentation.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.EdgeBehavior
import dev.piotrprus.particleemitter.ParticleShape
import dev.piotrprus.particleemitter.presentation.ui.DeckColors

/** Rounded dark stage that frames every live demo on a slide. */
@Composable
fun DemoCard(
    modifier: Modifier = Modifier,
    background: Color = Color(0xFF0A0D12),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(background)
            .border(1.dp, DeckColors.codeBorder, RoundedCornerShape(24.dp)),
    ) {
        content()
    }
}

/** Continuous celebratory confetti fountain shooting up from the bottom edge. */
@Composable
fun ConfettiDemo(modifier: Modifier = Modifier, width: Int = 700, height: Int = 760) {
    CanvasParticleEmitter(
        modifier = modifier.fillMaxSize(),
        config = CanvasEmitterConfig(
            particlePerSecond = 160,
            emitterCenter = DpOffset(width.dp / 2, height.dp),
            startRegionShape = CanvasEmitterConfig.Shape.POINT,
            startRegionSize = DpSize(0.dp, 0.dp),
            particleShapes = listOf(ParticleShape.Circle),
            lifespanRange = 2200..3200,
            fadeOutTime = 600..900,
            scaleTime = 300..500,
            colors = listOf(
                Color(0xFFFF5252), Color(0xFFFFD740), Color(0xFF69F0AE),
                Color(0xFF40C4FF), Color(0xFFE040FB), Color(0xFFFFFFFF),
            ),
            particleSizes = listOf(
                DpSize(10.dp, 10.dp), DpSize(14.dp, 14.dp), DpSize(7.dp, 7.dp),
            ),
            spread = IntRange(-35, 35),
            initialForce = IntRange(450, 750),
            rotationRange = IntRange(-180, 180),
            startScaleRange = IntRange(1, 1),
            targetScaleRange = IntRange(1, 1),
            gravityStrength = 320f,
            gravityAngle = 0,
        ),
    )
}

/** Glowing cyan bubbles drifting upwards — gravity pointing up. */
@Composable
fun BubblesDemo(modifier: Modifier = Modifier, width: Int = 520, height: Int = 560) {
    CanvasParticleEmitter(
        modifier = modifier.fillMaxSize(),
        config = CanvasEmitterConfig(
            particlePerSecond = 40,
            emitterCenter = DpOffset(width.dp / 2, height.dp - 20.dp),
            startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
            startRegionSize = DpSize(width.dp - 80.dp, 0.dp),
            particleShapes = listOf(ParticleShape.Circle),
            lifespanRange = 2500..4000,
            fadeOutTime = 800..1400,
            scaleTime = 1500..2500,
            colors = listOf(Color(0xFF00E5FF), Color(0xFF18FFFF), Color(0xFF80D8FF)),
            particleSizes = listOf(DpSize(8.dp, 8.dp), DpSize(14.dp, 14.dp), DpSize(20.dp, 20.dp)),
            spread = IntRange(-12, 12),
            blendMode = BlendMode.Screen,
            initialForce = IntRange(30, 90),
            startScaleRange = IntRange(0, 1),
            targetScaleRange = IntRange(1, 2),
            gravityStrength = 60f,
            gravityAngle = 180,
        ),
    )
}

/** Emoji snowfall using the Text particle shape. */
@Composable
fun EmojiRainDemo(
    modifier: Modifier = Modifier,
    emoji: String = "❄️",
    width: Int = 520,
    height: Int = 560,
) {
    val textMeasurer = rememberTextMeasurer()
    CanvasParticleEmitter(
        modifier = modifier.fillMaxSize(),
        config = CanvasEmitterConfig(
            particlePerSecond = 25,
            emitterCenter = DpOffset(width.dp / 2, 0.dp),
            startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
            startRegionSize = DpSize(width.dp, 0.dp),
            particleShapes = listOf(
                ParticleShape.Text(
                    text = emoji,
                    textStyle = TextStyle(fontSize = 28.sp),
                    textMeasurer = textMeasurer,
                ),
            ),
            lifespanRange = 3000..5000,
            fadeOutTime = 500..900,
            scaleTime = 400..700,
            colors = listOf(Color.White),
            particleSizes = listOf(DpSize(28.dp, 28.dp), DpSize(20.dp, 20.dp)),
            spread = IntRange(160, 200),
            initialForce = IntRange(40, 120),
            rotationRange = IntRange(-90, 90),
            startScaleRange = IntRange(1, 1),
            targetScaleRange = IntRange(1, 1),
            gravityStrength = 70f,
            gravityAngle = 0,
        ),
    )
}

/** Ring emitter with additive blending and a clean interior. */
@Composable
fun GlowRingDemo(modifier: Modifier = Modifier, width: Int = 520, height: Int = 560) {
    CanvasParticleEmitter(
        modifier = modifier.fillMaxSize(),
        config = CanvasEmitterConfig(
            particlePerSecond = 120,
            emitterCenter = DpOffset(width.dp / 2, height.dp / 2),
            startRegionShape = CanvasEmitterConfig.Shape.OVAL,
            startRegionSize = DpSize(260.dp, 260.dp),
            particleShapes = listOf(ParticleShape.Circle),
            lifespanRange = 900..1600,
            fadeOutTime = 700..1200,
            scaleTime = 600..1000,
            colors = listOf(Color(0xFF7C4DFF), Color(0xFFE040FB), Color(0xFF536DFE)),
            particleSizes = listOf(DpSize(6.dp, 6.dp), DpSize(10.dp, 10.dp)),
            spread = IntRange(0, 360),
            blendMode = BlendMode.Plus,
            initialForce = IntRange(40, 140),
            startScaleRange = IntRange(1, 1),
            targetScaleRange = IntRange(0, 1),
            hideInStartRegion = true,
        ),
    )
}

/**
 * Edge-behavior showcase: the same emitter, switching [edgeBehavior] live.
 * Existing particles adopt the new behavior on their next frame.
 */
@Composable
fun EdgeBehaviorDemo(
    edgeBehavior: EdgeBehavior,
    modifier: Modifier = Modifier,
    width: Int = 520,
    height: Int = 560,
) {
    CanvasParticleEmitter(
        modifier = modifier.fillMaxSize(),
        config = CanvasEmitterConfig(
            particlePerSecond = 25,
            emitterCenter = DpOffset(width.dp / 2, height.dp / 3),
            startRegionShape = CanvasEmitterConfig.Shape.POINT,
            startRegionSize = DpSize(0.dp, 0.dp),
            particleShapes = listOf(ParticleShape.Circle),
            // alpha animates from birth over fadeOutTime, so keep it close to
            // the lifespan or particles vanish before they ever reach an edge
            lifespanRange = 6500..8500,
            fadeOutTime = 6000..8000,
            scaleTime = 200..400,
            colors = listOf(Color(0xFFFFB74D), Color(0xFFFF8A65), Color(0xFFFFF176)),
            particleSizes = listOf(DpSize(12.dp, 12.dp), DpSize(16.dp, 16.dp)),
            spread = IntRange(-180, 180),
            initialForce = IntRange(150, 420),
            startScaleRange = IntRange(1, 1),
            targetScaleRange = IntRange(1, 1),
            gravityStrength = 380f,
            gravityAngle = 0,
            edgeBehavior = edgeBehavior,
        ),
    )
}

/** Sparse, slow ambient sparks used behind the title slide. */
@Composable
fun AmbientParticlesDemo(modifier: Modifier = Modifier) {
    CanvasParticleEmitter(
        modifier = modifier.fillMaxSize(),
        config = CanvasEmitterConfig(
            particlePerSecond = 30,
            emitterCenter = DpOffset(960.dp, 1110.dp),
            startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
            startRegionSize = DpSize(1920.dp, 0.dp),
            particleShapes = listOf(ParticleShape.Circle),
            lifespanRange = 6000..11000,
            fadeOutTime = 2500..4000,
            scaleTime = 2000..4000,
            colors = listOf(
                Color(0xFF7C4DFF), Color(0xFF00E5FF), Color(0xFF536DFE), Color(0xFFE040FB),
            ),
            particleSizes = listOf(DpSize(5.dp, 5.dp), DpSize(8.dp, 8.dp), DpSize(12.dp, 12.dp)),
            spread = IntRange(-8, 8),
            blendMode = BlendMode.Screen,
            initialForce = IntRange(90, 220),
            startScaleRange = IntRange(0, 1),
            targetScaleRange = IntRange(1, 2),
        ),
    )
}
