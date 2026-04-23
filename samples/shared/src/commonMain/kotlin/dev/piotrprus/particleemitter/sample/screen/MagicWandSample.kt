package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private val WandGold = Color(0xFFFFC93C)
private val StickBrown = Color(0xFF6B4A2B)
private val MagicColors = listOf(
    Color(0xFFFFFFFF),
    Color(0xFFFFE066),
    Color(0xFFFFC93C),
    Color(0xFFB3E0FF),
)

private const val MoveDebounceMs = 120L
private const val StickTiltDeg = 25f

@Composable
fun MagicWandSample() {
    val density = LocalDensity.current

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var wandPx by remember { mutableStateOf(Offset.Zero) }
    var wandInitialized by remember { mutableStateOf(false) }
    var hintVisible by remember { mutableStateOf(true) }
    var multiplier by remember { mutableFloatStateOf(5f) }
    var baseRate by remember { mutableFloatStateOf(5f) }
    var lifespan by remember { mutableFloatStateOf(700f) }
    var moveCounter by remember { mutableLongStateOf(0L) }
    var isMoving by remember { mutableStateOf(false) }

    LaunchedEffect(moveCounter) {
        if (moveCounter == 0L) return@LaunchedEffect
        isMoving = true
        delay(MoveDebounceMs)
        isMoving = false
    }

    if (containerSize != IntSize.Zero && !wandInitialized) {
        wandPx = Offset(containerSize.width / 2f, containerSize.height / 2f)
        wandInitialized = true
    }

    val particleStarShapes = remember(density) {
        listOf(8.dp, 11.dp, 14.dp).map { dp ->
            val radiusPx = with(density) { dp.toPx() } / 2f
            ParticleShape.PathShape(buildStarPath(radiusPx, centerOffsetPx = radiusPx / 3f))
        }
    }
    val particleSizes = remember {
        listOf(DpSize(8.dp, 8.dp), DpSize(11.dp, 11.dp), DpSize(14.dp, 14.dp))
    }

    val wandTipRadiusPx = with(density) { 14.dp.toPx() }
    val wandTipPath = remember(density) { buildStarPath(wandTipRadiusPx, centerOffsetPx = 0f) }
    val stickLenPx = with(density) { 80.dp.toPx() }
    val stickWidthPx = with(density) { 4.dp.toPx() }

    val baseRateInt = baseRate.roundToInt()
    val rate = if (isMoving) (baseRateInt * multiplier).roundToInt() else baseRateInt
    val lifespanMax = lifespan.roundToInt()
    val lifespanMin = (lifespanMax * 0.7f).roundToInt().coerceAtLeast(50)

    val emitterCenter = with(density) {
        DpOffset(wandPx.x.toDp(), wandPx.y.toDp())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
    ) {
        if (containerSize != IntSize.Zero && wandInitialized) {
            CanvasParticleEmitter(
                modifier = Modifier.fillMaxSize(),
                config = CanvasEmitterConfig(
                    particlePerSecond = rate,
                    emitterCenter = emitterCenter,
                    startRegionShape = CanvasEmitterConfig.Shape.POINT,
                    startRegionSize = DpSize.Zero,
                    particleShapes = particleStarShapes,
                    lifespanRange = IntRange(lifespanMin, lifespanMax),
                    fadeOutTime = IntRange(
                        (lifespanMin * 0.6f).roundToInt().coerceAtLeast(50),
                        (lifespanMax * 0.6f).roundToInt().coerceAtLeast(50),
                    ),
                    scaleTime = IntRange(lifespanMin, lifespanMax),
                    colors = MagicColors,
                    particleSizes = particleSizes,
                    spread = IntRange(0, 360),
                    scaleEasing = EaseOutCubic,
                    alphaEasing = EaseOutCubic,
                    initialForce = IntRange(30, 50),
                    startScaleRange = IntRange(1, 1),
                    targetScaleRange = IntRange(0, 0),
                    rotationRange = IntRange(-120, 120),
                    gravityStrength = 0f,
                )
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { hintVisible = false },
                        ) { change, drag ->
                            change.consume()
                            wandPx = Offset(
                                x = (wandPx.x + drag.x).coerceIn(0f, containerSize.width.toFloat()),
                                y = (wandPx.y + drag.y).coerceIn(0f, containerSize.height.toFloat()),
                            )
                            moveCounter++
                        }
                    }
            ) {
                rotate(degrees = StickTiltDeg, pivot = Offset(wandPx.x, wandPx.y)) {
                    drawRect(
                        color = StickBrown,
                        topLeft = Offset(wandPx.x - stickWidthPx / 2f, wandPx.y),
                        size = Size(stickWidthPx, stickLenPx),
                    )
                }
                translate(left = wandPx.x, top = wandPx.y) {
                    drawPath(path = wandTipPath, color = WandGold)
                }
            }
        }

        AnimatedVisibility(
            visible = hintVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp, start = 24.dp, end = 24.dp),
        ) {
            Text(
                text = "drag the wand",
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
                text = "Base rate: ${baseRateInt}/s",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = baseRate,
                onValueChange = { baseRate = it },
                valueRange = 1f..50f,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "Lifespan: ${lifespanMax}ms",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = lifespan,
                onValueChange = { lifespan = it },
                valueRange = 200f..3000f,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "Multiplier: ${multiplier.roundToInt()}\u00d7",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = multiplier,
                onValueChange = { multiplier = it },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth(),
            )
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
        // Start at the top (-PI/2), sweep clockwise.
        val angle = -PI / 2 + i * PI / points
        val x = centerOffsetPx + r * cos(angle).toFloat()
        val y = centerOffsetPx + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}
