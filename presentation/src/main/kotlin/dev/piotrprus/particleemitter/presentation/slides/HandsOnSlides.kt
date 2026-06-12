package dev.piotrprus.particleemitter.presentation.slides

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.piotrprus.particleemitter.CanvasEmitterConfig
import dev.piotrprus.particleemitter.CanvasParticleEmitter
import dev.piotrprus.particleemitter.ParticleShape
import dev.piotrprus.particleemitter.presentation.deck.Slide
import dev.piotrprus.particleemitter.presentation.demos.DemoCard
import dev.piotrprus.particleemitter.presentation.ui.BodyText
import dev.piotrprus.particleemitter.presentation.ui.Bullet
import dev.piotrprus.particleemitter.presentation.ui.CodeBlock
import dev.piotrprus.particleemitter.presentation.ui.DeckColors
import dev.piotrprus.particleemitter.presentation.ui.GifImage
import dev.piotrprus.particleemitter.presentation.ui.SlideSurface
import dev.piotrprus.particleemitter.presentation.ui.SlideTitle
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

val multiplatformSlide = Slide(steps = 5) {
    SlideSurface {
        SlideTitle("One config, four platforms")
        Spacer(modifier = Modifier.height(90.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(50.dp),
        ) {
            Reveal(at = 1, modifier = Modifier.weight(1f)) {
                PlatformCard("Android", "🤖")
            }
            Reveal(at = 2, modifier = Modifier.weight(1f)) {
                PlatformCard("iOS", "🍎")
            }
            Reveal(at = 3, modifier = Modifier.weight(1f)) {
                PlatformCard("Desktop", "🖥️")
            }
            Reveal(at = 4, modifier = Modifier.weight(1f)) {
                PlatformCard("Web", "🌐")
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
        Reveal(at = 5) {
            BodyText(
                "Even this presentation is a Compose Desktop app — every demo you saw was live.",
                color = DeckColors.textSecondary,
            )
        }
    }
}

@Composable
private fun PlatformCard(name: String, emoji: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeckColors.surface, RoundedCornerShape(28.dp))
            .border(1.dp, DeckColors.codeBorder, RoundedCornerShape(28.dp))
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        Text(text = emoji, fontSize = 80.sp)
        Text(
            text = name,
            color = DeckColors.textPrimary,
            fontSize = 38.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

val setupSlide = Slide(steps = 3) {
    SlideSurface {
        SlideTitle("Your turn — new KMP project")
        Spacer(modifier = Modifier.height(70.dp))
        Column(verticalArrangement = Arrangement.spacedBy(54.dp)) {
            Reveal(at = 1) {
                StepRow(
                    number = "1",
                    title = "kmp.jetbrains.com",
                    detail = "Kotlin Multiplatform wizard — check Android, iOS, Desktop, Web · share UI with Compose",
                )
            }
            Reveal(at = 2) {
                StepRow(
                    number = "2",
                    title = "Unzip & open in Android Studio",
                    detail = "Let Gradle sync finish — grab a coffee, it's earned it",
                )
            }
            Reveal(at = 3) {
                StepRow(
                    number = "3",
                    title = "Run the desktop target",
                    detail = "./gradlew :composeApp:run — fastest feedback loop for playing with particles",
                )
            }
        }
    }
}

@Composable
private fun StepRow(number: String, title: String, detail: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(40.dp),
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(DeckColors.accent, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number,
                color = Color.White,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Column {
            Text(
                text = title,
                color = DeckColors.textPrimary,
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = detail,
                color = DeckColors.textSecondary,
                fontSize = 30.sp,
                lineHeight = 42.sp,
            )
        }
    }
}

val dependencySlide = Slide(steps = 1) {
    SlideSurface {
        SlideTitle("Add the dependency")
        Spacer(modifier = Modifier.height(70.dp))
        BodyText("Published on Maven Central — nothing else to configure.")
        Spacer(modifier = Modifier.height(40.dp))
        Reveal(at = 1) {
            CodeBlock(
                code = """
                    // composeApp/build.gradle.kts
                    kotlin {
                        sourceSets {
                            commonMain.dependencies {
                                implementation("io.github.piotrprus:particle-emitter:1.1.0")
                            }
                        }
                    }
                """,
                fontSize = 30.sp,
            )
        }
    }
}

val firstEmitterSlide = Slide(steps = 2) {
    SlideSurface {
        SlideTitle("Your first emitter")
        Spacer(modifier = Modifier.height(50.dp))
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(60.dp)) {
            Reveal(at = 1, modifier = Modifier.weight(1.25f)) {
                CodeBlock(
                    code = """
                        CanvasParticleEmitter(
                            modifier = Modifier.fillMaxSize(),
                            config = CanvasEmitterConfig(
                                particlePerSecond = 50,
                                emitterCenter = DpOffset(330.dp, 380.dp),
                                startRegionShape = Shape.POINT,
                                startRegionSize = DpSize(0.dp, 0.dp),
                                particleShapes = listOf(ParticleShape.Circle),
                                lifespanRange = 800..1200,
                                fadeOutTime = 600..1000,
                                scaleTime = 800..1200,
                                colors = listOf(Cyan, Magenta, Yellow),
                                particleSizes = listOf(DpSize(8.dp, 8.dp)),
                                spread = IntRange(-90, 90),
                                blendMode = BlendMode.Screen,
                                initialForce = IntRange(50, 150),
                            )
                        )
                    """,
                    fontSize = 23.sp,
                )
            }
            Reveal(at = 2, modifier = Modifier.weight(0.75f).fillMaxSize()) {
                DemoCard(modifier = Modifier.fillMaxSize()) {
                    FirstEmitterDemo()
                }
            }
        }
    }
}

@Composable
private fun FirstEmitterDemo() {
    CanvasParticleEmitter(
        modifier = Modifier.fillMaxSize(),
        config = CanvasEmitterConfig(
            particlePerSecond = 50,
            emitterCenter = DpOffset(330.dp, 380.dp),
            startRegionShape = CanvasEmitterConfig.Shape.POINT,
            startRegionSize = DpSize(0.dp, 0.dp),
            particleShapes = listOf(ParticleShape.Circle),
            lifespanRange = 800..1200,
            fadeOutTime = 600..1000,
            scaleTime = 800..1200,
            colors = listOf(Color.Cyan, Color.Magenta, Color.Yellow),
            particleSizes = listOf(DpSize(8.dp, 8.dp), DpSize(12.dp, 12.dp)),
            spread = IntRange(-90, 90),
            blendMode = BlendMode.Screen,
            initialForce = IntRange(50, 150),
        ),
    )
}

val inspirationSlide = Slide(steps = 4) {
    SlideSurface {
        SlideTitle("Build something awesome")
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(50.dp),
        ) {
            Reveal(at = 1, modifier = Modifier.weight(1f)) {
                InspirationPlaceholder("Inspiration #1")
            }
            Reveal(at = 2, modifier = Modifier.weight(1f)) {
                InspirationPlaceholder("Inspiration #2")
            }
            Reveal(at = 3, modifier = Modifier.weight(1f)) {
                InspirationPlaceholder("Inspiration #3")
            }
        }
        Spacer(modifier = Modifier.height(44.dp))
        Reveal(at = 4) {
            BodyText(
                "…or: a snow globe with a shake button · tap-to-confetti cannon · campfire embers · " +
                    "soda fizz tied to a slider · magic wand drag trail · rain with a wind slider",
                color = DeckColors.textSecondary,
            )
        }
    }
}

/**
 * Placeholder card — swap in your own animation before the talk, e.g. a
 * [GifImage] from resources or a live emitter demo.
 */
@Composable
private fun InspirationPlaceholder(label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.9f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0A0D12))
                .border(1.dp, DeckColors.codeBorder, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Your\nanimation\nhere",
                color = DeckColors.textSecondary,
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = label,
            color = DeckColors.textPrimary,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

val closingSlide = Slide {
    SlideSurface {
        SlideTitle("Thank you! Go emit particles 🎉")
        Spacer(modifier = Modifier.height(80.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(80.dp),
        ) {
            QrCard(
                label = "github.com/PiotrPrus/ParticleEmitter",
                data = "https://github.com/PiotrPrus/ParticleEmitter",
            )
            QrCard(
                label = "Slides & sources",
                data = null,
            )
            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(30.dp),
            ) {
                ContactRow("X / Twitter", "@piotr_prus")
                ContactRow("LinkedIn", "Piotr Prus")
                ContactRow("Library", "io.github.piotrprus:particle-emitter:1.1.0")
            }
        }
    }
}

@Composable
private fun QrCard(label: String, data: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(340.dp)
                .background(Color.White, RoundedCornerShape(28.dp))
                .padding(28.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (data != null) {
                Image(
                    painter = rememberQrCodePainter(data),
                    contentDescription = label,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                // Placeholder — swap in a dedicated QR code before the talk.
                Text(
                    text = "QR\ncoming\nsoon",
                    color = Color(0xFF888888),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = label,
            color = DeckColors.textSecondary,
            fontSize = 28.sp,
        )
    }
}

@Composable
private fun ContactRow(channel: String, handle: String) {
    Column {
        Text(
            text = channel.uppercase(),
            color = DeckColors.accentAlt,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = handle,
            color = DeckColors.textPrimary,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
