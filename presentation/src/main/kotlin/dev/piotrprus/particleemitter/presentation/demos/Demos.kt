package dev.piotrprus.particleemitter.presentation.demos

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.EdgeBehavior
import dev.piotrprus.particleemitter.EmitterConfig
import dev.piotrprus.particleemitter.ParticleShape
import dev.piotrprus.particleemitter.ParticlesEmitter
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

private val layoutBurstEmojis = listOf("🎉", "🎈", "⭐", "💜", "🚀", "✨")
private val layoutBurstAccents = listOf(
    Color(0xFF7C4DFF), Color(0xFF00E5FF), Color(0xFFFFB74D),
    Color(0xFFE040FB), Color(0xFF69F0AE), Color(0xFFFF5252),
)
private val layoutChipLabels = listOf("Compose", "@Composable", "Modifier", "remember", "State")
private val layoutCardTitles = listOf("Particle", "Layout", "Node", "Widget")

/**
 * Layout-engine showcase: a repeating burst where every particle is a real
 * composable. To make the point obvious, most particles are structured
 * layouts — a bordered badge, a Row chip with a dot + label, a Column card
 * with title + subtitle — not just emoji glyphs. Restarts when the last dies.
 */
@Composable
fun LayoutBurstDemo(modifier: Modifier = Modifier) {
    var burst by remember { mutableStateOf(0) }
    val config = remember(burst) {
        EmitterConfig(
            id = "layout-burst-$burst",
            particlesCount = 140,
            emitDurationMillis = 1100L,
            particleLifespanMillis = 2600L,
            initialForce = 420,
            gravityStrength = 0.5f,
            gravityAngle = 0,
            spread = IntRange(-60, 60),
            rotationMultiplier = 1.2f,
            randomStartPoint = false,
            particle = { LayoutParticle() },
        )
    }
    ParticlesEmitter(
        modifier = modifier.fillMaxSize(),
        config = config,
        onAnimationFinished = { burst++ },
    )
}

/**
 * One particle for [LayoutBurstDemo]: picks a structured composable at random.
 * The structure (background, border, nested Row/Column, multiple Texts) is the
 * whole point — it shows the layout engine flinging real UI, not just glyphs.
 */
@Composable
private fun LayoutParticle() {
    val kind = remember { (0..3).random() }
    val accent = remember { layoutBurstAccents.random() }
    when (kind) {
        0 -> {
            // Emoji — the familiar baseline.
            val emoji = remember { layoutBurstEmojis.random() }
            Text(text = emoji, fontSize = 34.sp)
        }
        1 -> {
            // Circular badge: bordered Box with a centered letter.
            val letter = remember { ('A'..'Z').random().toString() }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.25f))
                    .border(2.dp, accent, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = letter, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
        2 -> {
            // Chip: a Row with a colored dot and a label.
            val label = remember { layoutChipLabels.random() }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF161B22))
                    .border(1.5.dp, accent, RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(accent))
                Text(text = label, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
        else -> {
            // Mini card: a Column with a bold title and a muted subtitle.
            val title = remember { layoutCardTitles.random() }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1B2230))
                    .border(1.5.dp, accent.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 18.dp, vertical = 12.dp),
            ) {
                Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "@Composable", color = accent, fontSize = 13.sp)
            }
        }
    }
}

/** Canvas-engine showcase: a firehose of thousands of particles per second. */
@Composable
fun CanvasThroughputDemo(modifier: Modifier = Modifier, width: Int = 820, height: Int = 330) {
    CanvasParticleEmitter(
        modifier = modifier.fillMaxSize(),
        config = CanvasEmitterConfig(
            particlePerSecond = 10000,
            emitterCenter = DpOffset(width.dp / 2, height.dp / 2),
            startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
            startRegionSize = DpSize(width.dp / 4, 0.dp),
            particleShapes = listOf(ParticleShape.Circle),
            lifespanRange = 900..1500,
            fadeOutTime = 400..700,
            scaleTime = 200..400,
            colors = listOf(
                Color(0xFF7C4DFF), Color(0xFF00E5FF), Color(0xFFE040FB), Color(0xFF536DFE),
            ),
            particleSizes = listOf(
                DpSize(4.dp, 4.dp), DpSize(6.dp, 6.dp), DpSize(8.dp, 8.dp),
            ),
            spread = IntRange(-180, 180),
            blendMode = BlendMode.Screen,
            initialForce = IntRange(200, 480),
            startScaleRange = IntRange(1, 1),
            targetScaleRange = IntRange(1, 1),
            gravityStrength = 300f,
            gravityAngle = 0,
        ),
    )
}

/**
 * Campfire-style flame: a dense additive column of shrinking embers rising
 * from a narrow base — hot yellow core fading through orange to deep red.
 */
@Composable
fun FlameDemo(modifier: Modifier = Modifier, width: Int = 530, height: Int = 590) {
    Box(modifier = modifier.fillMaxSize()) {
        // Outer flame: wider, slower, red-orange body.
        CanvasParticleEmitter(
            modifier = Modifier.fillMaxSize(),
            config = CanvasEmitterConfig(
                particlePerSecond = 600,
                emitterCenter = DpOffset(width.dp / 2, height.dp),
                startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
                startRegionSize = DpSize(90.dp, 0.dp),
                particleShapes = listOf(ParticleShape.Circle),
                lifespanRange = 900..1600,
                fadeOutTime = 700..1200,
                scaleTime = 700..1300,
                colors = listOf(Color(0xFFFF5722), Color(0xFFE64A19), Color(0xFFFF7043)),
                particleSizes = listOf(
                    DpSize(8.dp, 8.dp), DpSize(11.dp, 11.dp), DpSize(6.dp, 6.dp),
                ),
                spread = IntRange(-10, 10),
                blendMode = BlendMode.Plus,
                initialForce = IntRange(90, 200),
                startScaleRange = IntRange(1, 1),
                targetScaleRange = IntRange(0, 0),
                gravityStrength = 60f,
                gravityAngle = 180,
            ),
        )
        // Inner core: narrow, faster, hot yellow-white tongue.
        CanvasParticleEmitter(
            modifier = Modifier.fillMaxSize(),
            config = CanvasEmitterConfig(
                particlePerSecond = 450,
                emitterCenter = DpOffset(width.dp / 2, height.dp),
                startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
                startRegionSize = DpSize(40.dp, 0.dp),
                particleShapes = listOf(ParticleShape.Circle),
                lifespanRange = 600..1100,
                fadeOutTime = 450..800,
                scaleTime = 450..900,
                colors = listOf(Color(0xFFFFC107), Color(0xFFFFE082), Color(0xFFFFF8E1)),
                particleSizes = listOf(DpSize(5.dp, 5.dp), DpSize(7.dp, 7.dp)),
                spread = IntRange(-6, 6),
                blendMode = BlendMode.Plus,
                initialForce = IntRange(130, 240),
                startScaleRange = IntRange(1, 1),
                targetScaleRange = IntRange(0, 0),
                gravityStrength = 80f,
                gravityAngle = 180,
            ),
        )
        // Stray sparks drifting up past the flame tip.
        CanvasParticleEmitter(
            modifier = Modifier.fillMaxSize(),
            config = CanvasEmitterConfig(
                particlePerSecond = 12,
                emitterCenter = DpOffset(width.dp / 2, height.dp - 20.dp),
                startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
                startRegionSize = DpSize(60.dp, 0.dp),
                particleShapes = listOf(ParticleShape.Circle),
                lifespanRange = 1400..2400,
                fadeOutTime = 1000..1800,
                scaleTime = 300..500,
                colors = listOf(Color(0xFFFFD54F), Color(0xFFFFAB40)),
                particleSizes = listOf(DpSize(3.dp, 3.dp), DpSize(4.dp, 4.dp)),
                spread = IntRange(-25, 25),
                blendMode = BlendMode.Plus,
                initialForce = IntRange(180, 320),
                startScaleRange = IntRange(1, 1),
                targetScaleRange = IntRange(0, 1),
                gravityStrength = 50f,
                gravityAngle = 180,
            ),
        )
    }
}

private val MatrixGlyphs = listOf("ア", "カ", "ネ", "ク", "ヲ", "ミ", "7", "0", "1", "3")
private val MatrixGreens = listOf(
    Color(0xFF00FF41), Color(0xFF00C838), Color(0xFFCCFFCC),
)
private val MatrixFaintGreens = listOf(
    Color(0xFF0A5F1E), Color(0xFF0E8226),
)

// ParticleShape.Text ignores the config's colors and particleSizes — the glyph
// is rasterized once from its TextStyle — so color and size are baked in here,
// cycling both palettes for variety.
private fun matrixGlyphShapes(
    textMeasurer: androidx.compose.ui.text.TextMeasurer,
    palette: List<Color>,
    fontSizes: List<Int>,
): List<ParticleShape> = MatrixGlyphs.mapIndexed { index, glyph ->
    ParticleShape.Text(
        text = glyph,
        textStyle = TextStyle(
            fontSize = fontSizes[index % fontSizes.size].sp,
            color = palette[index % palette.size],
            fontWeight = FontWeight.Bold,
        ),
        textMeasurer = textMeasurer,
    )
}

/**
 * Matrix digital rain: katakana and digit glyphs streaming down with additive
 * glow — a bright near layer over a faint, smaller far layer for depth.
 */
@Composable
fun MatrixRainDemo(modifier: Modifier = Modifier, width: Int = 530, height: Int = 590) {
    val textMeasurer = rememberTextMeasurer()
    val farShapes = remember(textMeasurer) {
        matrixGlyphShapes(textMeasurer, MatrixFaintGreens, fontSizes = listOf(8, 10, 12))
    }
    val nearShapes = remember(textMeasurer) {
        matrixGlyphShapes(textMeasurer, MatrixGreens, fontSizes = listOf(11, 13, 16))
    }
    Box(modifier = modifier.fillMaxSize()) {
        // Far layer: small, dark green, slower — fills the background.
        CanvasParticleEmitter(
            modifier = Modifier.fillMaxSize(),
            config = CanvasEmitterConfig(
                particlePerSecond = 220,
                emitterCenter = DpOffset(width.dp / 2, 0.dp),
                startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
                startRegionSize = DpSize(width.dp, 0.dp),
                particleShapes = farShapes,
                lifespanRange = 3500..5000,
                fadeOutTime = 3000..4500,
                scaleTime = 100..200,
                colors = MatrixFaintGreens,
                particleSizes = listOf(DpSize(12.dp, 12.dp), DpSize(15.dp, 15.dp)),
                spread = IntRange(180, 180),
                blendMode = BlendMode.Plus,
                initialForce = IntRange(60, 180),
                rotationRange = IntRange(0, 0),
                startScaleRange = IntRange(1, 1),
                targetScaleRange = IntRange(1, 1),
                gravityStrength = 30f,
                gravityAngle = 0,
            ),
        )
        // Near layer: bright phosphor-green glyphs racing down on top.
        CanvasParticleEmitter(
            modifier = Modifier.fillMaxSize(),
            config = CanvasEmitterConfig(
                particlePerSecond = 320,
                emitterCenter = DpOffset(width.dp / 2, 0.dp),
                startRegionShape = CanvasEmitterConfig.Shape.H_LINE,
                startRegionSize = DpSize(width.dp, 0.dp),
                particleShapes = nearShapes,
                lifespanRange = 1800..3200,
                fadeOutTime = 1500..2800,
                scaleTime = 100..200,
                colors = MatrixGreens,
                particleSizes = listOf(
                    DpSize(18.dp, 18.dp), DpSize(22.dp, 22.dp), DpSize(26.dp, 26.dp),
                ),
                spread = IntRange(180, 180),
                blendMode = BlendMode.Plus,
                initialForce = IntRange(140, 420),
                rotationRange = IntRange(0, 0),
                startScaleRange = IntRange(1, 1),
                targetScaleRange = IntRange(1, 1),
                gravityStrength = 60f,
                gravityAngle = 0,
            ),
        )
    }
}

private val WandGold = Color(0xFFFFC93C)
private val WandStickBrown = Color(0xFF6B4A2B)
private val MagicColors = listOf(
    Color(0xFFFFFFFF), Color(0xFFFFE066), Color(0xFFFFC93C), Color(0xFFB3E0FF),
)

/**
 * The magic wand from the samples, self-driving: instead of drag gestures the
 * wand wanders the viewport on a Lissajous curve, trailing star particles.
 */
@Composable
fun MagicWandDemo(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    var time by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                if (last != 0L) time += (now - last) / 1_000_000_000f
                last = now
            }
        }
    }

    val particleStarShapes = remember(density) {
        listOf(8.dp, 11.dp, 14.dp).map { size ->
            val radiusPx = with(density) { size.toPx() } / 2f
            ParticleShape.PathShape(buildStarPath(radiusPx, centerOffsetPx = radiusPx / 3f))
        }
    }
    val wandTipPath = remember(density) {
        buildStarPath(with(density) { 14.dp.toPx() }, centerOffsetPx = 0f)
    }
    val stickLenPx = with(density) { 70.dp.toPx() }
    val stickWidthPx = with(density) { 4.dp.toPx() }

    Box(modifier = modifier.fillMaxSize().onSizeChanged { containerSize = it }) {
        if (containerSize != IntSize.Zero) {
            // Lissajous curve: incommensurate frequencies keep the path from
            // ever repeating exactly, so it reads as random wandering.
            val wandPx = Offset(
                x = containerSize.width * (0.5f + 0.34f * sin(1.1f * time)),
                y = containerSize.height * (0.42f + 0.30f * sin(0.7f * time + 1.3f)),
            )
            CanvasParticleEmitter(
                modifier = Modifier.fillMaxSize(),
                config = CanvasEmitterConfig(
                    particlePerSecond = 110,
                    emitterCenter = with(density) { DpOffset(wandPx.x.toDp(), wandPx.y.toDp()) },
                    startRegionShape = CanvasEmitterConfig.Shape.POINT,
                    startRegionSize = DpSize.Zero,
                    particleShapes = particleStarShapes,
                    lifespanRange = 1400..2400,
                    fadeOutTime = 1000..1800,
                    scaleTime = 1400..2400,
                    colors = MagicColors,
                    particleSizes = listOf(
                        DpSize(8.dp, 8.dp), DpSize(11.dp, 11.dp), DpSize(14.dp, 14.dp),
                    ),
                    spread = IntRange(0, 360),
                    scaleEasing = EaseOutCubic,
                    alphaEasing = EaseOutCubic,
                    initialForce = IntRange(20, 45),
                    startScaleRange = IntRange(1, 1),
                    targetScaleRange = IntRange(0, 0),
                    rotationRange = IntRange(-120, 120),
                    gravityStrength = 90f,
                    gravityAngle = 0,
                ),
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                rotate(degrees = 25f, pivot = wandPx) {
                    drawRect(
                        color = WandStickBrown,
                        topLeft = Offset(wandPx.x - stickWidthPx / 2f, wandPx.y),
                        size = Size(stickWidthPx, stickLenPx),
                    )
                }
                translate(left = wandPx.x, top = wandPx.y) {
                    drawPath(path = wandTipPath, color = WandGold)
                }
            }
        }
    }
}

private fun buildStarPath(radiusPx: Float, centerOffsetPx: Float): Path {
    // 5-pointed star path: alternate between outer and inner radius around the circle.
    val points = 5
    val outer = radiusPx
    val inner = radiusPx * 0.45f
    val path = Path()
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) outer else inner
        val angle = -PI / 2 + i * PI / points
        val x = centerOffsetPx + r * cos(angle).toFloat()
        val y = centerOffsetPx + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

/**
 * Practical gravity showcase: the emitter sits fixed in the centre, throwing
 * sparks in every direction. A gravity point orbits around it, and every frame
 * gravityAngle is re-aimed from the centre toward that point — so the spray
 * bends to follow it, sloshing around like water chasing the pull. This is the
 * physics engine working live.
 */
@Composable
fun GravityShowcaseDemo(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var t by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                if (last != 0L) t += (now - last) / 1_000_000_000f
                last = now
            }
        }
    }

    Box(modifier = modifier.fillMaxSize().onSizeChanged { containerSize = it }) {
        if (containerSize != IntSize.Zero) {
            val center = Offset(containerSize.width / 2f, containerSize.height / 2f)
            val orbitR = minOf(containerSize.width, containerSize.height) * 0.36f
            val gravityPoint = Offset(
                x = center.x + orbitR * cos(t * 0.8f),
                y = center.y + orbitR * sin(t * 0.8f),
            )
            // aim gravity from centre toward the orbiting point.
            // engine: gravity = strength · (-sin θ, cos θ), y growing downward.
            val dx = gravityPoint.x - center.x
            val dy = gravityPoint.y - center.y
            val gravityAngleDeg = (atan2(-dx, dy) * 180f / PI.toFloat()).toInt()

            CanvasParticleEmitter(
                modifier = Modifier.fillMaxSize(),
                config = CanvasEmitterConfig(
                    particlePerSecond = 320,
                    emitterCenter = with(density) { DpOffset(center.x.toDp(), center.y.toDp()) },
                    startRegionShape = CanvasEmitterConfig.Shape.POINT,
                    startRegionSize = DpSize.Zero,
                    particleShapes = listOf(ParticleShape.Circle),
                    lifespanRange = 1300..2100,
                    fadeOutTime = 1000..1800,
                    scaleTime = 200..400,
                    colors = listOf(
                        Color(0xFFFFD54F), Color(0xFFFF8A65), Color(0xFFFFFFFF),
                        Color(0xFF40C4FF), Color(0xFFE040FB),
                    ),
                    particleSizes = listOf(
                        DpSize(5.dp, 5.dp), DpSize(8.dp, 8.dp), DpSize(4.dp, 4.dp),
                    ),
                    spread = IntRange(0, 360), // thrown every direction
                    blendMode = BlendMode.Plus,
                    initialForce = IntRange(110, 210),
                    startScaleRange = IntRange(1, 1),
                    targetScaleRange = IntRange(0, 1),
                    gravityStrength = 320f,
                    gravityAngle = gravityAngleDeg,
                ),
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                // faint line shows the current pull direction
                drawLine(
                    color = Color(0xFF00E5FF).copy(alpha = 0.35f),
                    start = center,
                    end = gravityPoint,
                    strokeWidth = 3f,
                )
                // fixed emitter at the centre
                drawCircle(Color.White.copy(alpha = 0.18f), radius = 20f, center = center)
                drawCircle(Color.White, radius = 7f, center = center)
                // the orbiting gravity point
                drawCircle(Color(0xFF00E5FF).copy(alpha = 0.25f), radius = 30f, center = gravityPoint)
                drawCircle(Color(0xFF00E5FF), radius = 12f, center = gravityPoint)
            }
        }
    }
}

/**
 * One physics case for slide 7, shown as real particles. The emitter trembles
 * gently around the viewport centre and emits ~20 particles/sec, so the
 * audience sees the actual effect. Identical setup across the three cards —
 * only [initialForce] and [gravityStrength] differ.
 */
@Composable
fun GravityCaseDemo(
    initialForce: IntRange,
    gravityStrength: Float,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var t by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                if (last != 0L) t += (now - last) / 1_000_000_000f
                last = now
            }
        }
    }

    Box(modifier = modifier.fillMaxSize().onSizeChanged { containerSize = it }) {
        if (containerSize != IntSize.Zero) {
            // small organic wander around the centre — two sines at odd freqs
            val tremble = minOf(containerSize.width, containerSize.height) * 0.08f
            val center = Offset(
                x = containerSize.width / 2f + tremble * sin(t * 1.3f),
                y = containerSize.height / 2f + tremble * sin(t * 1.9f + 0.7f),
            )
            CanvasParticleEmitter(
                modifier = Modifier.fillMaxSize(),
                config = CanvasEmitterConfig(
                    particlePerSecond = 20,
                    emitterCenter = with(density) { DpOffset(center.x.toDp(), center.y.toDp()) },
                    startRegionShape = CanvasEmitterConfig.Shape.POINT,
                    startRegionSize = DpSize.Zero,
                    particleShapes = listOf(ParticleShape.Circle),
                    lifespanRange = 1600..2600,
                    fadeOutTime = 1200..2200,
                    scaleTime = 200..400,
                    colors = listOf(accent, Color.White),
                    particleSizes = listOf(
                        DpSize(7.dp, 7.dp), DpSize(10.dp, 10.dp), DpSize(5.dp, 5.dp),
                    ),
                    spread = IntRange(-55, 55),
                    blendMode = BlendMode.Plus,
                    initialForce = initialForce,
                    startScaleRange = IntRange(1, 1),
                    targetScaleRange = IntRange(0, 1),
                    gravityStrength = gravityStrength,
                    gravityAngle = 0,
                ),
            )
            // the trembling emitter point itself
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(Color.White.copy(alpha = 0.18f), radius = 15f, center = center)
                drawCircle(Color.White, radius = 6f, center = center)
            }
        }
    }
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
