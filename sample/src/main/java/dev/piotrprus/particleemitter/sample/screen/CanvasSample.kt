package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape
import dev.piotrprus.particleemitter.sample.R

@Composable
fun CanvasSample() {
    val density = LocalDensity.current
    val context = LocalContext.current
    val imageBitmap =
        remember { ImageBitmap.imageResource(context.resources, R.drawable.star_four) }
    var birthRate by remember { mutableStateOf(100f) }

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
                        particlePerSecond = birthRate.toInt(),
                        emitterCenter = avatarCenter,
                        startRegionShape = CanvasEmitterConfig.Shape.OVAL,
                        startRegionSize = avatarSize * 0.8f,
                        particleShapes = listOf(ParticleShape.Image(imageBitmap)),
                        lifespanRange = IntRange(1000, 1500),
                        colors = listOf(
                            Color(0xff53FF00), Color(0xffE5FF5E), Color(0xff4AC2FF)
                        ),
                        blendMode = BlendMode.Screen,
                        scaleEasing = EaseOutCubic,
                        particleSizes = listOf(DpSize(8.dp, 8.dp)),
                        initialForce = IntRange(40, 100),
                        spread = IntRange(-180, 180),
                        fadeOutTime = IntRange(700, 1000),
                        rotationRange = IntRange(0, 90),
                        scaleTime = IntRange(500, 700),
                        targetScaleRange = IntRange(0, 1),
                        startScaleRange = IntRange(2, 3),
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
                    .background(color = Color.Red, shape = CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Birth rate: ${birthRate.toInt()} particles/sec",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = birthRate,
                onValueChange = { birthRate = it },
                valueRange = 0f..1000f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
