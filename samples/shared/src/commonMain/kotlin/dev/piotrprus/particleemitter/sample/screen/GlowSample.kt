package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.piotrprus.particleemitter.EmitterConfig
import dev.piotrprus.particleemitter.ParticlesEmitter

@Composable
fun GlowSample() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        ParticlesEmitter(config = EmitterConfig(
            id = "",
            particlesCount = 100,
            emitDurationMillis = 20000,
            particleLifespanMillis = 5000,
            initialForce = 40,
            gravityStrength = 0.0f,
            spread = IntRange(-180, 180),
            maxHorizontalDisplacementDp = 100,
            rotationMultiplier = 0.5f,
            randomStartPoint = false,
            particle = {
                val infiniteTransition = rememberInfiniteTransition()
                val glow by infiniteTransition.animateFloat(
                    initialValue = 0.5f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                val colorAnim by infiniteTransition.animateColor(
                    initialValue = Color.White,
                    targetValue = Color.Red,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(30.dp * glow)
                            .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .background(colorAnim, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(colorAnim, CircleShape)
                    )
                }
            }), onAnimationFinished = {})

        ParticlesEmitter(config = EmitterConfig(
            id = "",
            particlesCount = 20,
            emitDurationMillis = 20000,
            particleLifespanMillis = 5000,
            initialForce = 100,
            gravityStrength = 0.0f,
            spread = IntRange(-180, 180),
            maxHorizontalDisplacementDp = 100,
            rotationMultiplier = 0.5f,
            randomStartPoint = false,
            particle = {
                val infiniteTransition = rememberInfiniteTransition()
                val glow by infiniteTransition.animateFloat(
                    initialValue = 0.5f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                val colorAnim by infiniteTransition.animateColor(
                    initialValue = Color.Red,
                    targetValue = Color.White,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(30.dp * glow)
                            .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .background(colorAnim, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(colorAnim, CircleShape)
                    )
                }
            }), onAnimationFinished = {})
    }
}
