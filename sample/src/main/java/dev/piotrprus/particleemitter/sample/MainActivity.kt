package dev.piotrprus.particleemitter.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.piotrprus.particleemitter.sample.screen.BenchmarkSample
import dev.piotrprus.particleemitter.sample.screen.CanvasSample
import dev.piotrprus.particleemitter.sample.screen.ConfettiSample
import dev.piotrprus.particleemitter.sample.screen.EmojiRainSample
import dev.piotrprus.particleemitter.sample.screen.GlowSample
import dev.piotrprus.particleemitter.sample.screen.GravityPointSample
import dev.piotrprus.particleemitter.sample.screen.GravitySample
import dev.piotrprus.particleemitter.sample.screen.MagicWandSample
import dev.piotrprus.particleemitter.sample.screen.SingleEmitterBenchmarkSample
import dev.piotrprus.particleemitter.sample.screen.StickyEdgesSample
import dev.piotrprus.particleemitter.sample.ui.theme.ExtendedColors
import dev.piotrprus.particleemitter.sample.ui.theme.ParticleEmitterTheme

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
            SampleScaffold(title = "Particle Emitter Samples") {
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
                ConfettiSample()
            }
        }
        composable("glow") {
            SampleScaffold(title = "Glow Particles", onBack = { navController.popBackStack() }) {
                GlowSample()
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
        composable("magic_wand") {
            SampleScaffold(title = "Magic Wand", onBack = { navController.popBackStack() }) {
                MagicWandSample()
            }
        }
        composable("emoji_rain") {
            SampleScaffold(title = "Emoji Rain", onBack = { navController.popBackStack() }) {
                EmojiRainSample()
            }
        }
        composable("sticky_edges") {
            SampleScaffold(title = "Sticky Edges", onBack = { navController.popBackStack() }) {
                StickyEdgesSample()
            }
        }
        composable("benchmark") {
            SampleScaffold(title = "Benchmark", onBack = { navController.popBackStack() }) {
                BenchmarkSample()
            }
        }
        composable("single_benchmark") {
            SampleScaffold(title = "Single Emitter Benchmark", onBack = { navController.popBackStack() }) {
                SingleEmitterBenchmarkSample()
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

private data class SampleEntry(
    val route: String,
    val title: String,
    val description: String,
)

private val sampleEntries = listOf(
    SampleEntry(
        route = "canvas",
        title = "Canvas Emitter",
        description = "High-performance canvas-based particles with layered star effects",
    ),
    SampleEntry(
        route = "confetti",
        title = "Confetti",
        description = "Multi-emitter confetti with emoji and glowing stars",
    ),
    SampleEntry(
        route = "glow",
        title = "Glow Particles",
        description = "Glowing particles with blur and color animations",
    ),
    SampleEntry(
        route = "gravity",
        title = "Gravity",
        description = "Canvas particles with configurable gravity — toggle on/off",
    ),
    SampleEntry(
        route = "gravity_point",
        title = "Gravity Point",
        description = "Drag a gravity attractor point to bend particle trajectories",
    ),
    SampleEntry(
        route = "magic_wand",
        title = "Magic Wand",
        description = "Drag to leave a sparkling trail of stars",
    ),
    SampleEntry(
        route = "emoji_rain",
        title = "Emoji Rain",
        description = "Fullscreen emoji particles using the Text shape",
    ),
    SampleEntry(
        route = "sticky_edges",
        title = "Sticky Edges",
        description = "Particles bounce, stick, or wrap at screen edges",
    ),
    SampleEntry(
        route = "benchmark",
        title = "Benchmark",
        description = "10 emitters × 1000 particles/sec — toggle active count for profiling",
    ),
    SampleEntry(
        route = "single_benchmark",
        title = "Single Emitter Benchmark",
        description = "1 emitter, slider from 0 to 10,000 particles/sec in 1,000 steps",
    ),
)

@Composable
fun MainScreen(onSampleClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(sampleEntries, key = { it.route }) { entry ->
            SampleButton(
                title = entry.title,
                description = entry.description,
                onClick = { onSampleClick(entry.route) },
            )
        }
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
