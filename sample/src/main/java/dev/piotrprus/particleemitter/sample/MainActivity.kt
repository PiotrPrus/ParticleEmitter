package dev.piotrprus.particleemitter.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import dev.piotrprus.particleemitter.sample.screen.CanvasSample
import dev.piotrprus.particleemitter.sample.screen.ConfettiSample
import dev.piotrprus.particleemitter.sample.screen.GlowSample
import dev.piotrprus.particleemitter.sample.screen.GravityPointSample
import dev.piotrprus.particleemitter.sample.screen.GravitySample
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
        composable("sticky_edges") {
            SampleScaffold(title = "Sticky Edges", onBack = { navController.popBackStack() }) {
                StickyEdgesSample()
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

        SampleButton(
            title = "Sticky Edges",
            description = "Particles bounce, stick, or wrap at screen edges",
            onClick = { onSampleClick("sticky_edges") }
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
