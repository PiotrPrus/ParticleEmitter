package dev.piotrprus.particleemitter.sample

import dev.piotrprus.particleemitter.sample.screen.BenchmarkSample
import dev.piotrprus.particleemitter.sample.screen.CanvasSample
import dev.piotrprus.particleemitter.sample.screen.ConfettiSample
import dev.piotrprus.particleemitter.sample.screen.EmojiRainSample
import dev.piotrprus.particleemitter.sample.screen.GlowSample
import dev.piotrprus.particleemitter.sample.screen.GravityPointSample
import dev.piotrprus.particleemitter.sample.screen.GravitySample
import dev.piotrprus.particleemitter.sample.screen.MagicWandSample
import dev.piotrprus.particleemitter.sample.screen.RingEmitterSample
import dev.piotrprus.particleemitter.sample.screen.SingleEmitterBenchmarkSample
import dev.piotrprus.particleemitter.sample.screen.StickyEdgesSample
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private val BackgroundColor = Color(0xFF1E1D20)

/**
 * Entry-point composable used by every sample application (Android, desktop,
 * iOS, web). Hosts a navigation graph over all the
 * [ParticleEmitter][dev.piotrprus.particleemitter] samples — the main menu
 * lists every sample and tapping one pushes a screen with its [Scaffold] and
 * a back arrow.
 */
@Composable
fun SamplesNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            SampleScaffold(title = "Particle Emitter Samples") {
                MainScreen(onSampleClick = { route -> navController.navigate(route) })
            }
        }
        sampleEntries.forEach { entry ->
            composable(entry.route) {
                SampleScaffold(title = entry.title, onBack = { navController.popBackStack() }) {
                    entry.content()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SampleScaffold(title: String, onBack: (() -> Unit)? = null, content: @Composable () -> Unit) {
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
                    containerColor = BackgroundColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                ),
            )
        },
        containerColor = BackgroundColor,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            content()
        }
    }
}

private data class SampleEntry(
    val route: String,
    val title: String,
    val description: String,
    val content: @Composable () -> Unit,
)

private val sampleEntries: List<SampleEntry> = listOf(
    SampleEntry(
        route = "canvas",
        title = "Canvas Emitter",
        description = "High-performance canvas-based particles with layered star effects",
        content = { CanvasSample() },
    ),
    SampleEntry(
        route = "confetti",
        title = "Confetti",
        description = "Multi-emitter confetti with emoji and glowing stars",
        content = { ConfettiSample() },
    ),
    SampleEntry(
        route = "glow",
        title = "Glow Particles",
        description = "Glowing particles with blur and color animations",
        content = { GlowSample() },
    ),
    SampleEntry(
        route = "gravity",
        title = "Gravity",
        description = "Canvas particles with configurable gravity — toggle on/off",
        content = { GravitySample() },
    ),
    SampleEntry(
        route = "gravity_point",
        title = "Gravity Point",
        description = "Drag a gravity attractor point to bend particle trajectories",
        content = { GravityPointSample() },
    ),
    SampleEntry(
        route = "magic_wand",
        title = "Magic Wand",
        description = "Drag to leave a sparkling trail of stars",
        content = { MagicWandSample() },
    ),
    SampleEntry(
        route = "emoji_rain",
        title = "Emoji Rain",
        description = "Fullscreen emoji particles using the Text shape",
        content = { EmojiRainSample() },
    ),
    SampleEntry(
        route = "ring_emitter",
        title = "Ring Emitter",
        description = "Ring start region with 360° spread — toggle hideInStartRegion",
        content = { RingEmitterSample() },
    ),
    SampleEntry(
        route = "sticky_edges",
        title = "Sticky Edges",
        description = "Particles bounce, stick, or wrap at screen edges",
        content = { StickyEdgesSample() },
    ),
    SampleEntry(
        route = "benchmark",
        title = "Benchmark",
        description = "10 emitters × 1000 particles/sec — toggle active count for profiling",
        content = { BenchmarkSample() },
    ),
    SampleEntry(
        route = "single_benchmark",
        title = "Single Emitter Benchmark",
        description = "1 emitter, slider from 0 to 10,000 particles/sec in 1,000 steps",
        content = { SingleEmitterBenchmarkSample() },
    ),
)

@Composable
private fun MainScreen(onSampleClick: (String) -> Unit) {
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
private fun SampleButton(title: String, description: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            )
        }
    }
}
