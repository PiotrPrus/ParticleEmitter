package dev.piotrprus.particleemitter.sample.screen

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.piotrprus.particleemitter.EmitterConfig
import dev.piotrprus.particleemitter.MultiEmitter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConfettiSample() {
    var visibility by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (visibility) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(30.dp))
            )
            MultiEmitter(
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp),
                emitterCount = 12,
                emitterDelay = 150L,
                emitterConfig = EmitterConfig(
                    id = "",
                    particlesCount = 1,
                    emitDurationMillis = 0,
                    particleLifespanMillis = 3000,
                    initialForce = 140,
                    gravityStrength = 0.2f,
                    spread = IntRange(-30, 30),
                    maxHorizontalDisplacementDp = 2000,
                    rotationMultiplier = 0f,
                    randomStartPoint = true,
                    particle = {
                        val sizeAndAlpha =
                            remember { androidx.compose.animation.core.Animatable(1f) }

                        LaunchedEffect(Unit) {
                            launch {
                                sizeAndAlpha.animateTo(0f, animationSpec = tween(3000))
                            }
                        }
                        Text(
                            text = "\uD83D\uDECD\uFE0F",
                            fontSize = 30.sp,
                            modifier = Modifier.graphicsLayer {
                                scaleX = sizeAndAlpha.value
                                scaleY = sizeAndAlpha.value
                                alpha = sizeAndAlpha.value
                            })
                    })
            )
            MultiEmitter(
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp),
                onAnimationFinished = {},
                emitterCount = 15,
                emitterDelay = 200L,
                emitterConfig = EmitterConfig(
                    id = "",
                    particlesCount = 10,
                    emitDurationMillis = 200,
                    particleLifespanMillis = 2000,
                    initialForce = 80,
                    gravityStrength = 0.1f,
                    spread = IntRange(-180, 180),
                    maxHorizontalDisplacementDp = 2000,
                    rotationMultiplier = 1f,
                    randomStartPoint = true
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val color = remember { Animatable(Color.Green) }
                    val sizeAndAlpha =
                        remember { androidx.compose.animation.core.Animatable(1f) }
                    val glow by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(300, easing = EaseOutBack),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    LaunchedEffect(Unit) {
                        launch {
                            color.animateTo(Color.Red, animationSpec = tween(2000))
                        }
                        launch {
                            sizeAndAlpha.animateTo(0f, animationSpec = tween(2000))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .graphicsLayer {
                                alpha = sizeAndAlpha.value
                                scaleX = sizeAndAlpha.value
                                scaleY = sizeAndAlpha.value
                            }, contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(15.dp * glow)
                                .background(color = color.value, StarShape)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = color.value, StarShape)
                        )
                    }
                })
        }
        Button(
            onClick = {
                scope.launch {
                    visibility = visibility.not()
                    delay(100)
                    visibility = visibility.not()
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Reset")
        }
    }
}

val StarShape = androidx.compose.foundation.shape.GenericShape { size, _ ->
    val width = size.width
    val height = size.height

    val centerX = width / 2f
    val centerY = height / 2f

    val outerRadius = width / 2f
    val innerRadius = outerRadius / 2.5f

    moveTo(centerX, centerY - outerRadius)

    for (i in 1..5) {
        val outerAngle = i * 4 * kotlin.math.PI / 5
        val outerX = centerX + outerRadius * kotlin.math.sin(outerAngle).toFloat()
        val outerY = centerY - outerRadius * kotlin.math.cos(outerAngle).toFloat()
        lineTo(outerX, outerY)

        val innerAngle = (i * 4 + 2) * kotlin.math.PI / 5
        val innerX = centerX + innerRadius * kotlin.math.sin(innerAngle).toFloat()
        val innerY = centerY - innerRadius * kotlin.math.cos(innerAngle).toFloat()
        lineTo(innerX, innerY)
    }

    close()
}
