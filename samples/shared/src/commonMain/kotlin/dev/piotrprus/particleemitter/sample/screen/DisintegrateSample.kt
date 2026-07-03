package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.EdgeBehavior
import dev.piotrprus.particleemitter.ParticleShape
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private val RectSize = DpSize(200.dp, 150.dp)
// Square dimensions so the oval renders as a perfect circle.
private val CircleSize = DpSize(190.dp, 190.dp)
// Keep the shape near the top so there is room below for particles to rain down.
private val ShapeTopPadding = 72.dp

// A short, intense burst that spawns the whole body at once, then falls silent.
// The emitter emits per second, so the rate is derived from the desired particle count and window.
private const val BurstWindowMs = 120L
private const val MinParticles = 50f
private const val MaxParticles = 1500f
private const val EdgeDamping = 0.3f

private val ParticleColors = listOf(
    Color(0xFF90CAF9),
    Color(0xFF64B5F6),
    Color(0xFF2196F3),
    Color(0xFF1565C0),
    Color(0xFFFFFFFF),
)

@Composable
fun DisintegrateSample() {
    val density = LocalDensity.current

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    // true = shape is intact and shown; false = it has been blown into particles.
    var intact by remember { mutableStateOf(true) }
    var useOval by remember { mutableStateOf(false) }
    var bounce by remember { mutableStateOf(false) }
    var particleCount by remember { mutableStateOf(700f) }
    // While true the emitter spawns; a LaunchedEffect turns it back off after the burst window.
    var emitting by remember { mutableStateOf(false) }

    LaunchedEffect(emitting) {
        if (emitting) {
            delay(BurstWindowMs)
            emitting = false
        }
    }

    fun boom() {
        if (!intact) return
        intact = false
        emitting = true
    }

    fun recreate() {
        emitting = false
        intact = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
    ) {
        if (containerSize != IntSize.Zero) {
            val widthDp = with(density) { containerSize.width.toDp() }
            val regionSize = if (useOval) CircleSize else RectSize
            val center = DpOffset(widthDp / 2, ShapeTopPadding + regionSize.height / 2)

            // Emitting for BurstWindowMs at this rate yields ~particleCount particles in the burst.
            val burstRate = (particleCount / (BurstWindowMs / 1000f)).toInt()

            CanvasParticleEmitter(
                modifier = Modifier.fillMaxSize(),
                config = CanvasEmitterConfig(
                    particlePerSecond = if (emitting) burstRate else 0,
                    emitterCenter = center,
                    startRegionShape = if (useOval) {
                        CanvasEmitterConfig.Shape.SOLID_OVAL
                    } else {
                        CanvasEmitterConfig.Shape.SOLID_RECT
                    },
                    startRegionSize = regionSize,
                    particleShapes = listOf(ParticleShape.Circle),
                    lifespanRange = IntRange(2000, 5000),
                    fadeOutTime = IntRange(2000, 5000),
                    scaleTime = IntRange(400, 1200),
                    colors = ParticleColors,
                    particleSizes = listOf(
                        DpSize(6.dp, 6.dp),
                        DpSize(8.dp, 8.dp),
                        DpSize(10.dp, 10.dp),
                        DpSize(12.dp, 12.dp),
                    ),
                    spread = IntRange(-180, 180),
                    initialForce = IntRange(60, 260),
                    startScaleRange = IntRange(1, 1),
                    targetScaleRange = IntRange(1, 1),
                    gravityStrength = 550f,
                    gravityAngle = 0,
                    edgeBehavior = if (bounce) EdgeBehavior.Bounce(EdgeDamping) else EdgeBehavior.None,
                )
            )

            if (intact) {
                SolidShape(
                    oval = useOval,
                    onClick = { boom() },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = ShapeTopPadding)
                        .size(regionSize),
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Switch(
                    checked = useOval,
                    onCheckedChange = { useOval = it },
                    enabled = intact,
                )
                Text(
                    text = if (useOval) "Shape: SOLID_OVAL" else "Shape: SOLID_RECT",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Switch(
                    checked = bounce,
                    onCheckedChange = { bounce = it },
                )
                Text(
                    text = if (bounce) "Edges: Bounce (damping $EdgeDamping)" else "Edges: None",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Text(
                text = "Particles: ${particleCount.roundToInt()}",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = particleCount,
                onValueChange = { particleCount = it },
                valueRange = MinParticles..MaxParticles,
            )

            Button(
                onClick = { if (intact) boom() else recreate() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(if (intact) "💥 Boom" else "↻ Recreate")
            }

            Text(
                text = "Tap the shape (or Boom) to disintegrate it — particles spawn from the whole " +
                    "filled area, not just the outline, then fall under gravity. Recreate to reset.",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun SolidShape(
    oval: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val fill = Brush.linearGradient(
        colors = listOf(Color(0xFF64B5F6), Color(0xFF1565C0)),
    )
    Canvas(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        )
    ) {
        if (oval) {
            drawOval(brush = fill)
        } else {
            drawRoundRect(brush = fill, cornerRadius = CornerRadius(16.dp.toPx()))
        }
    }
}
