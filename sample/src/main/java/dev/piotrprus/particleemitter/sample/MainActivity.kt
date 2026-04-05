package dev.piotrprus.particleemitter.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.EmitterConfig
import dev.piotrprus.particleemitter.MultiEmitter
import dev.piotrprus.particleemitter.ParticleShape
import dev.piotrprus.particleemitter.ParticlesEmitter
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.piotrprus.particleemitter.sample.ui.theme.ExtendedColors
import dev.piotrprus.particleemitter.sample.ui.theme.ParticleEmitterTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParticleEmitterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ExtendedColors.paletteNeutral2
                ) {
//                    Sample3()
//                    Sample4()
//                    Sample5()
                    CanvasSample()
                }
            }
        }
    }
}

//TODO: 1. use drawable star from SVG
// 2. put particles in 3 different layers. each layer with different color and size.
// 1st layer should have big particles, very small distance
// 3. particles from different layers needs to be visible on the screen. not override each other.

@Composable
fun CanvasSample() {
    val density = LocalDensity.current
    val context = LocalContext.current
    val imageBitmap =
        remember { ImageBitmap.imageResource(context.resources, R.drawable.star_four) }
    val imageBitmapSmall =
        remember { ImageBitmap.imageResource(context.resources, R.drawable.star_four_5px) }
    Box(modifier = Modifier.fillMaxSize()) {
//        Image(
//            painter = painterResource(id = R.drawable.vert_image),
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize()
//        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center), contentAlignment = Alignment.Center
        ) {
            var avatarSize by remember { mutableStateOf(DpSize.Zero) }
            var avatarCenter by remember { mutableStateOf(DpOffset.Zero) }

            if (avatarSize != DpSize.Zero) {
                //TODO: put it inside remember
                //TODO: Add presets for explosion, confetti, etc
                CanvasParticleEmitter(
                    modifier = Modifier.fillMaxSize(),
                    CanvasEmitterConfig(
                        particlePerSecond = 30,
                        emitterCenter = avatarCenter,
                        startRegionShape = CanvasEmitterConfig.Shape.OVAL,
                        startRegionSize = avatarSize * 0.8f,
                        particleShapes = listOf(ParticleShape.Image(imageBitmap)),
                        lifespanRange = IntRange(1000, 1500),
                        colors = listOf(
                            Color(0xff53FF00), Color(0xffE5FF5E)
                        ),
                        blendMode = BlendMode.Screen,
                        translateEasing = FastOutSlowInEasing,
                        scaleEasing = EaseOutCubic,
                        particleSizes = listOf(DpSize(10.dp, 10.dp)),
                        flyDistancesDp = IntRange(0, 3),
                        spread = IntRange(-180, 180),
                        fadeOutTime = IntRange(700, 1000),
                        rotationRange = IntRange(0, 0),
                        scaleTime = IntRange(500, 700),
                        targetScaleRange = IntRange(4, 5),
                        startScaleRange = IntRange(2, 3),
                    )
                )

                CanvasParticleEmitter(
                    modifier = Modifier.fillMaxSize(),
                    CanvasEmitterConfig(
                        particlePerSecond = 60,
                        emitterCenter = avatarCenter,
                        startRegionShape = CanvasEmitterConfig.Shape.OVAL,
                        startRegionSize = avatarSize * 0.8f,
                        particleShapes = listOf(ParticleShape.Image(imageBitmap)),
                        lifespanRange = IntRange(700, 700),
                        colors = listOf(
                            Color(0xff53FF00), Color(0xffE5FF5E), Color(0xff4AC2FF)
                        ),
                        blendMode = BlendMode.Screen,
                        translateEasing = FastOutLinearInEasing,
                        alphaEasing = EaseInCubic,
                        scaleEasing = EaseInCubic,
                        particleSizes = listOf(DpSize(5.dp, 5.dp)),
                        flyDistancesDp = IntRange(35, 40),
                        spread = IntRange(-65, 45),
                        fadeOutTime = IntRange(700, 700),
                        rotationRange = IntRange(0, 90),
                        scaleTime = IntRange(400, 700),
                        targetScaleRange = IntRange(0, 1),
                        startScaleRange = IntRange(0, 2),
                    )
                )
            }

            Box(modifier = Modifier
                .size(28.dp)
                .offset(x = (-140).dp)
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
                .background(color = Color.Red, shape = CircleShape),
                contentAlignment = Alignment.Center) {
                Text(text = "A")
            }
        }
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center), contentAlignment = Alignment.Center
        ) {
            var avatarSize by remember { mutableStateOf(DpSize.Zero) }
            var avatarCenter by remember { mutableStateOf(DpOffset.Zero) }

            if (avatarSize != DpSize.Zero) {
                //TODO: put it inside remember
                //TODO: Add presets for explosion, confetti, etc
                CanvasParticleEmitter(
                    modifier = Modifier.fillMaxSize(),
                    CanvasEmitterConfig(
                        particlePerSecond = 30,
                        emitterCenter = avatarCenter,
                        startRegionShape = CanvasEmitterConfig.Shape.OVAL,
                        startRegionSize = avatarSize * 0.9f,
                        particleShapes = listOf(ParticleShape.Image(imageBitmap)),
                        lifespanRange = IntRange(1000, 1500),
                        colors = listOf(
                            Color(0xff53FF00), Color(0xffE5FF5E)
                        ),
                        blendMode = BlendMode.Screen,
                        translateEasing = FastOutSlowInEasing,
                        scaleEasing = EaseOutCubic,
                        particleSizes = listOf(DpSize(10.dp, 10.dp)),
                        flyDistancesDp = IntRange(0, 3),
                        spread = IntRange(-180, 180),
                        fadeOutTime = IntRange(700, 1000),
                        rotationRange = IntRange(0, 0),
                        scaleTime = IntRange(500, 700),
                        targetScaleRange = IntRange(4, 5),
                        startScaleRange = IntRange(2, 3),
                    )
                )

                CanvasParticleEmitter(
                    modifier = Modifier.fillMaxSize(),
                    CanvasEmitterConfig(
                        particlePerSecond = 100,
                        emitterCenter = avatarCenter,
                        startRegionShape = CanvasEmitterConfig.Shape.OVAL,
                        startRegionSize = avatarSize,
                        particleShapes = listOf(ParticleShape.Image(imageBitmap)),
                        lifespanRange = IntRange(1500, 1500),
                        colors = listOf(
                            Color(0xff53FF00), Color(0xffE5FF5E), Color(0xff4AC2FF)
                        ),
                        blendMode = BlendMode.Screen,
                        translateEasing = FastOutLinearInEasing,
                        alphaEasing = EaseIn,
                        scaleEasing = EaseInCubic,
                        particleSizes = listOf(DpSize(6.dp, 6.dp)),
                        flyDistancesDp = IntRange(80, 100),
                        spread = IntRange(-45, 35),
                        fadeOutTime = IntRange(1000, 1500),
                        rotationRange = IntRange(0, 45),
                        scaleTime = IntRange(700, 800),
                        targetScaleRange = IntRange(0, 1),
                        startScaleRange = IntRange(2, 3),
                    )
                )
            }

            Box(modifier = Modifier
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
                .background(color = Color.Red, shape = CircleShape),
                contentAlignment = Alignment.Center) {
                Text(text = "B")
            }
        }

        Box(
            modifier = Modifier
                .offset(x = 140.dp)
                .size(30.dp)
                .align(Alignment.Center), contentAlignment = Alignment.Center
        ) {
            var avatarSize by remember { mutableStateOf(DpSize.Zero) }
            var avatarCenter by remember { mutableStateOf(DpOffset.Zero) }

            if (avatarSize != DpSize.Zero) {
                //TODO: put it inside remember
                //TODO: Add presets for explosion, confetti, etc
                CanvasParticleEmitter(
                    modifier = Modifier.fillMaxSize(),
                    CanvasEmitterConfig(
                        particlePerSecond = 50,
                        emitterCenter = avatarCenter,
                        startRegionShape = CanvasEmitterConfig.Shape.OVAL,
                        startRegionSize = avatarSize,
                        particleShapes = listOf(ParticleShape.Image(imageBitmap)),
                        lifespanRange = IntRange(1000, 1500),
                        colors = listOf(
                            Color(0xff53FF00), Color(0xffE5FF5E)
                        ),
                        blendMode = BlendMode.Screen,
                        translateEasing = FastOutSlowInEasing,
                        scaleEasing = EaseOutCubic,
                        particleSizes = listOf(DpSize(10.dp, 10.dp)),
                        flyDistancesDp = IntRange(0, 3),
                        spread = IntRange(-180, 180),
                        fadeOutTime = IntRange(700, 1000),
                        rotationRange = IntRange(0, 0),
                        scaleTime = IntRange(500, 700),
                        targetScaleRange = IntRange(4, 5),
                        startScaleRange = IntRange(2, 3),
                    )
                )

                //Screen, Overlay, ColorDodge,
                CanvasParticleEmitter(
                    modifier = Modifier.fillMaxSize(),
                    CanvasEmitterConfig(
                        particlePerSecond = 200,
                        emitterCenter = avatarCenter,
                        startRegionShape = CanvasEmitterConfig.Shape.OVAL,
                        startRegionSize = avatarSize,
                        particleShapes = listOf(ParticleShape.Image(imageBitmap)),
                        lifespanRange = IntRange(1500, 1500),
                        colors = listOf(
                            Color(0xff53FF00),
                            Color(0xffE5FF5E),
                            Color(0xff4AC2FF)
                        ),
                        blendMode = BlendMode.Screen,
                        translateEasing = FastOutLinearInEasing,
                        alphaEasing = EaseInCirc,
                        scaleEasing = EaseInCubic,
                        particleSizes = listOf(DpSize(6.dp, 6.dp)),
                        flyDistancesDp = IntRange(140, 200),
                        spread = IntRange(-40, 30),
                        fadeOutTime = IntRange(1000, 1500),
                        rotationRange = IntRange(0, 90),
                        scaleTime = IntRange(1000, 2000),
                        targetScaleRange = IntRange(0, 2),
                        startScaleRange = IntRange(2, 3),
                    )
                )
            }

            Box(modifier = Modifier
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
                .background(color = Color.Green, shape = CircleShape),
                contentAlignment = Alignment.Center) {
                Text(text = "C")
            }
        }
    }
}


@Composable
fun Sample4() {
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
            gravityMultiplier = 0.0f,
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
            gravityMultiplier = 0.0f,
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

@Composable
fun Sample3() {
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
                    gravityMultiplier = 0.2f,
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
                            text = "\uD83D\uDECD️",
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
                    gravityMultiplier = 0.1f,
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

val StarShape = GenericShape { size, _ ->
    val width = size.width
    val height = size.height

    val centerX = width / 2f
    val centerY = height / 2f

    val outerRadius = width / 2f
    val innerRadius = outerRadius / 2.5f

    moveTo(centerX, centerY - outerRadius)

    for (i in 1..5) {
        val outerAngle = i * 4 * PI / 5
        val outerX = centerX + outerRadius * sin(outerAngle).toFloat()
        val outerY = centerY - outerRadius * cos(outerAngle).toFloat()
        lineTo(outerX, outerY)

        val innerAngle = (i * 4 + 2) * PI / 5
        val innerX = centerX + innerRadius * sin(innerAngle).toFloat()
        val innerY = centerY - innerRadius * cos(innerAngle).toFloat()
        lineTo(innerX, innerY)
    }

    close()
}
