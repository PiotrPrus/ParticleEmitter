package dev.piotrprus.particleemitter.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.EmitterConfig
import dev.piotrprus.particleemitter.MultiEmitter
import dev.piotrprus.particleemitter.ParticleShape
import dev.piotrprus.particleemitter.ParticlesEmitter
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.piotrprus.particleemitter.sample.ui.theme.ExtendedColors
import dev.piotrprus.particleemitter.sample.ui.theme.ParticleEmitterTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.border
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParticleEmitterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ExtendedColors.paletteNeutral2
                ) {
                    SampleNavigation()
                }
            }
        }
    }
}

@Composable
fun SampleNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            SampleScaffold(title = "Particle Emitter Samples", onBack = { }) {
                MainScreen(onSampleClick = { route -> navController.navigate(route) })
            }
        }
        composable("canvas") {
            SampleScaffold(title = "Canvas Emitter", onBack = { navController.popBackStack() }) {
                CanvasSample()
            }
        }
        composable("confetti") {
            SampleScaffold(title = "Confetti", onBack = { navController.popBackStack() }) {
                Sample3()
            }
        }
        composable("glow") {
            SampleScaffold(title = "Glow Particles", onBack = { navController.popBackStack() }) {
                Sample4()
            }
        }
        composable("gravity") {
            SampleScaffold(title = "Gravity", onBack = { navController.popBackStack() }) {
                GravitySample()
            }
        }
        composable("gravity_point") {
            SampleScaffold(title = "Gravity Point", onBack = { navController.popBackStack() }) {
                GravityPointSample()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleScaffold(title: String, onBack: (() -> Unit)? = null, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ExtendedColors.paletteNeutral2,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = ExtendedColors.paletteNeutral2
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            content()
        }
    }
}

@Composable
fun MainScreen(onSampleClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SampleButton(
            title = "Canvas Emitter",
            description = "High-performance canvas-based particles with layered star effects",
            onClick = { onSampleClick("canvas") }
        )

        SampleButton(
            title = "Confetti",
            description = "Multi-emitter confetti with emoji and glowing stars",
            onClick = { onSampleClick("confetti") }
        )

        SampleButton(
            title = "Glow Particles",
            description = "Glowing particles with blur and color animations",
            onClick = { onSampleClick("glow") }
        )

        SampleButton(
            title = "Gravity",
            description = "Canvas particles with configurable gravity — toggle on/off",
            onClick = { onSampleClick("gravity") }
        )

        SampleButton(
            title = "Gravity Point",
            description = "Drag a gravity attractor point to bend particle trajectories",
            onClick = { onSampleClick("gravity_point") }
        )
    }
}

@Composable
fun SampleButton(title: String, description: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun GravitySample() {
    val density = LocalDensity.current
    val context = LocalContext.current
    val imageBitmap =
        remember { ImageBitmap.imageResource(context.resources, R.drawable.star_four) }
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
                text = "Angle: ${gravityAngleDeg}°  Strength: ${gravityStrength.toInt()}",
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
