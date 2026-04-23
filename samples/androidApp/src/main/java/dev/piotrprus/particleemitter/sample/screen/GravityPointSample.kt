package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape
import dev.piotrprus.particleemitter.sample.R
import kotlin.math.atan2
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun GravityPointSample() {
    val density = LocalDensity.current
    val context = LocalContext.current
    val imageBitmap =
        remember { ImageBitmap.imageResource(context.resources, R.drawable.star_four) }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var particleForce by remember { mutableStateOf(120f) }

    // Emitter at center of the screen
    val emitterCenter = with(density) {
        DpOffset(
            x = (containerSize.width / 2).toDp(),
            y = (containerSize.height / 2).toDp()
        )
    }

    // Gravity point — starts above center, draggable
    var gravityPointPx by remember { mutableStateOf(Offset.Zero) }
    val gravityPointInitialized = remember { mutableStateOf(false) }

    // Initialize gravity point position once we know container size
    if (containerSize != IntSize.Zero && !gravityPointInitialized.value) {
        gravityPointPx = Offset(
            x = containerSize.width / 2f,
            y = containerSize.height * 0.3f
        )
        gravityPointInitialized.value = true
    }

    // Compute gravity angle and strength from emitter center to gravity point
    val emitterPx = with(density) {
        Offset(emitterCenter.x.toPx(), emitterCenter.y.toPx())
    }
    val dx = gravityPointPx.x - emitterPx.x
    val dy = gravityPointPx.y - emitterPx.y
    val distancePx = sqrt(dx * dx + dy * dy)
    // atan2(-dx, dy) maps: down=0, left=90, right=-90, up=180
    val gravityAngleDeg = Math.toDegrees(atan2(-dx.toDouble(), dy.toDouble())).toInt()
    // Scale strength by distance — farther point = stronger pull
    val maxDistance = sqrt(
        (containerSize.width * containerSize.width + containerSize.height * containerSize.height).toFloat()
    )
    val gravityStrength = if (maxDistance > 0f) (distancePx / maxDistance) * 300f else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { containerSize = it.size }
    ) {
        if (containerSize != IntSize.Zero && gravityPointInitialized.value) {
            CanvasParticleEmitter(
                modifier = Modifier.fillMaxSize(),
                CanvasEmitterConfig(
                    particlePerSecond = 60,
                    emitterCenter = emitterCenter,
                    startRegionShape = CanvasEmitterConfig.Shape.POINT,
                    startRegionSize = DpSize.Zero,
                    particleShapes = listOf(
                        ParticleShape.Circle,
                        ParticleShape.Image(imageBitmap)
                    ),
                    lifespanRange = IntRange(3000, 5000),
                    colors = listOf(
                        Color(0xffFF9A56), Color(0xffFF6B6B), Color(0xffFFE66D),
                        Color(0xffFF4E8A), Color(0xffFFA726)
                    ),
                    blendMode = BlendMode.SrcOver,
                    scaleEasing = EaseOutCubic,
                    particleSizes = listOf(
                        DpSize(5.dp, 5.dp), DpSize(8.dp, 8.dp), DpSize(12.dp, 12.dp)
                    ),
                    initialForce = IntRange(particleForce.toInt(), (particleForce * 1.3f).toInt()),
                    spread = IntRange(-45, 45),
                    fadeOutTime = IntRange(2000, 3500),
                    rotationRange = IntRange(-180, 180),
                    scaleTime = IntRange(500, 1000),
                    targetScaleRange = IntRange(0, 1),
                    startScaleRange = IntRange(1, 3),
                    gravityStrength = gravityStrength,
                    gravityAngle = gravityAngleDeg,
                )
            )
        }

        // Emitter source indicator
        Box(
            modifier = Modifier
                .size(16.dp)
                .offset {
                    IntOffset(
                        x = containerSize.width / 2 - with(density) { 8.dp.toPx() }.toInt(),
                        y = containerSize.height / 2 - with(density) { 8.dp.toPx() }.toInt()
                    )
                }
                .background(color = Color.White, shape = CircleShape)
        )

        // Draggable gravity point
        if (gravityPointInitialized.value) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (gravityPointPx.x - with(density) { 20.dp.toPx() }).toInt(),
                            y = (gravityPointPx.y - with(density) { 20.dp.toPx() }).toInt()
                        )
                    }
                    .size(40.dp)
                    .border(2.dp, Color(0xffFF6B6B), CircleShape)
                    .background(Color(0x44FF6B6B), CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            gravityPointPx = Offset(
                                x = (gravityPointPx.x + dragAmount.x).coerceIn(
                                    0f,
                                    containerSize.width.toFloat()
                                ),
                                y = (gravityPointPx.y + dragAmount.y).coerceIn(
                                    0f,
                                    containerSize.height.toFloat()
                                )
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xffFF6B6B), CircleShape)
                )
            }
        }

        // Info overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Drag the circle to move gravity point",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Angle: ${gravityAngleDeg}\u00b0  Strength: ${gravityStrength.toInt()}",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Initial force: ${particleForce.toInt()}",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = particleForce,
                onValueChange = { particleForce = it },
                valueRange = 20f..400f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
