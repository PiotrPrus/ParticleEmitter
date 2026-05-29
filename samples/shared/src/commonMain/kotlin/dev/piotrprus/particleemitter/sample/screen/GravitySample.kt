package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape
import particleemitter.samples.shared.generated.resources.Res
import particleemitter.samples.shared.generated.resources.star_four
import org.jetbrains.compose.resources.imageResource

@Composable
fun GravitySample() {
    val density = LocalDensity.current
    val imageBitmap = imageResource(Res.drawable.star_four)
    var gravityEnabled by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            var avatarSize by remember { mutableStateOf(DpSize.Zero) }
            var avatarCenter by remember { mutableStateOf(DpOffset.Zero) }

            if (avatarSize != DpSize.Zero) {
                CanvasParticleEmitter(
                    modifier = Modifier.fillMaxSize(),
                    CanvasEmitterConfig(
                        particlePerSecond = 80,
                        emitterCenter = avatarCenter,
                        startRegionShape = CanvasEmitterConfig.Shape.POINT,
                        startRegionSize = DpSize.Zero,
                        particleShapes = listOf(
                            ParticleShape.Circle,
                            ParticleShape.Image(imageBitmap)
                        ),
                        lifespanRange = IntRange(4000, 6000),
                        colors = listOf(
                            Color(0xffFF6B6B), Color(0xffFFE66D), Color(0xff4ECDC4), Color(0xff45B7D1)
                        ),
                        blendMode = BlendMode.SrcOver,
                        scaleEasing = EaseOutCubic,
                        particleSizes = listOf(DpSize(6.dp, 6.dp), DpSize(10.dp, 10.dp)),
                        initialForce = IntRange(40, 120),
                        spread = IntRange(-60, 60),
                        fadeOutTime = IntRange(1000, 1500),
                        rotationRange = IntRange(-180, 180),
                        scaleTime = IntRange(500, 800),
                        targetScaleRange = IntRange(0, 1),
                        startScaleRange = IntRange(1, 2),
                        gravityStrength = if (gravityEnabled) 120f else 0f,
                        gravityAngle = 0,
                    )
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .onPlaced {
                        avatarCenter = with(density) {
                            DpOffset(
                                x = (it.positionInParent().x + it.size.width / 2).toDp(),
                                y = (it.positionInParent().y + it.size.height / 2).toDp()
                            )
                        }
                        avatarSize = with(density) {
                            DpSize(
                                width = it.size.width.toDp(),
                                height = it.size.height.toDp()
                            )
                        }
                    }
                    .background(color = Color(0xffFF6B6B), shape = CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Gravity: ${if (gravityEnabled) "ON" else "OFF"}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = gravityEnabled,
                    onCheckedChange = { gravityEnabled = it }
                )
            }
        }
    }
}
