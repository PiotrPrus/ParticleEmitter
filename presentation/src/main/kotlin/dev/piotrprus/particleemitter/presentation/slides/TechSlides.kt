package dev.piotrprus.particleemitter.presentation.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.piotrprus.particleemitter.EdgeBehavior
import dev.piotrprus.particleemitter.presentation.deck.Slide
import dev.piotrprus.particleemitter.presentation.demos.AmbientParticlesDemo
import dev.piotrprus.particleemitter.presentation.demos.BubblesDemo
import dev.piotrprus.particleemitter.presentation.demos.EdgeBehaviorDemo
import dev.piotrprus.particleemitter.presentation.demos.ConfettiDemo
import dev.piotrprus.particleemitter.presentation.demos.DemoCard
import dev.piotrprus.particleemitter.presentation.demos.EmojiRainDemo
import dev.piotrprus.particleemitter.presentation.ui.BodyText
import dev.piotrprus.particleemitter.presentation.ui.Bullet
import dev.piotrprus.particleemitter.presentation.ui.CodeBlock
import dev.piotrprus.particleemitter.presentation.ui.DeckColors
import dev.piotrprus.particleemitter.presentation.ui.SlideSurface
import dev.piotrprus.particleemitter.presentation.ui.SlideTitle

val titleSlide = Slide {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF0A0D12), Color(0xFF131A2B))),
            ),
    ) {
        AmbientParticlesDemo()
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "ParticleEmitter",
                color = DeckColors.textPrimary,
                fontSize = 120.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Physics-based particle effects for Compose Multiplatform",
                color = DeckColors.textSecondary,
                fontSize = 40.sp,
            )
        }
        Text(
            text = "Piotr Prus  ·  GDG London",
            color = DeckColors.textSecondary,
            fontSize = 30.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp),
        )
    }
}

val whatIsItSlide = Slide(steps = 4) {
    SlideSurface {
        SlideTitle("What is ParticleEmitter?")
        Spacer(modifier = Modifier.height(70.dp))
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.weight(1.1f),
                verticalArrangement = Arrangement.spacedBy(54.dp),
            ) {
                Reveal(at = 1) {
                    Bullet(
                        emphasis = "One composable, thousands of particles",
                        text = "Drop it in any layout — it draws everything itself",
                    )
                }
                Reveal(at = 2) {
                    Bullet(
                        emphasis = "Real physics",
                        text = "Velocity, spread, directional gravity, rotation, edge collisions",
                    )
                }
                Reveal(at = 3) {
                    Bullet(
                        emphasis = "Fully configurable",
                        text = "Shapes, colors, sizes, blend modes, easing, lifespans",
                    )
                }
                Reveal(at = 4) {
                    Bullet(
                        emphasis = "Compose Multiplatform",
                        text = "Android · iOS · Desktop · Web, single artifact since 1.1.0",
                    )
                }
            }
            Spacer(modifier = Modifier.width(60.dp))
            DemoCard(modifier = Modifier.weight(0.9f).fillMaxSize()) {
                ConfettiDemo(width = 700, height = 760)
            }
        }
    }
}

val twoEnginesSlide = Slide(steps = 2) {
    SlideSurface {
        SlideTitle("Two rendering engines")
        Spacer(modifier = Modifier.height(70.dp))
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(60.dp),
        ) {
            Reveal(at = 1, modifier = Modifier.weight(1f)) {
                EngineCard(
                    name = "ParticlesEmitter",
                    tagline = "Layout-based",
                    points = listOf(
                        "Every particle is a real @Composable",
                        "Emoji, images, buttons — anything",
                        "One layout node per particle",
                        "Great for small bursts (≤ ~100)",
                    ),
                    accent = DeckColors.warm,
                )
            }
            Reveal(at = 2, modifier = Modifier.weight(1f)) {
                EngineCard(
                    name = "CanvasParticleEmitter",
                    tagline = "Canvas-based",
                    points = listOf(
                        "Zero layout nodes — pure drawBehind",
                        "Shapes, images, text rasterized once",
                        "One frame clock updates the whole world",
                        "Thousands of particles at 60 FPS",
                    ),
                    accent = DeckColors.accentAlt,
                )
            }
        }
    }
}

@Composable
private fun EngineCard(
    name: String,
    tagline: String,
    points: List<String>,
    accent: Color,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeckColors.surface, RoundedCornerShape(28.dp))
            .border(2.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(28.dp))
            .padding(48.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        Text(
            text = tagline.uppercase(),
            color = accent,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = name,
            color = DeckColors.textPrimary,
            fontSize = 44.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        points.forEach { point ->
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                Text(text = "—", color = accent, fontSize = 30.sp)
                Text(
                    text = point,
                    color = DeckColors.textSecondary,
                    fontSize = 30.sp,
                    lineHeight = 40.sp,
                )
            }
        }
    }
}

val frameLoopSlide = Slide(steps = 2) {
    SlideSurface {
        SlideTitle("One frame at a time")
        Spacer(modifier = Modifier.height(50.dp))
        BodyText("Compose hands us the display clock: withFrameNanos suspends until the next frame.")
        Spacer(modifier = Modifier.height(40.dp))
        Reveal(at = 1) {
            CodeBlock(
                code = """
                    LaunchedEffect(Unit) {
                        while (true) {
                            withFrameNanos { frameNano ->
                                val dt = ((frameNano - lastFrame) / 1_000_000_000.0)
                                    .toFloat()
                                    .coerceIn(0.001f, 0.1f)
                                lastFrame = frameNano

                                particles = particles.update(dt) + emitNew(dt)
                            }
                        }
                    }
                """,
            )
        }
        Spacer(modifier = Modifier.height(44.dp))
        Reveal(at = 2) {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Bullet(text = "Fires once per vsync — 60 Hz, 120 Hz, whatever the display does")
                Bullet(text = "No Animatable per particle: one loop owns the entire simulation")
                Bullet(text = "Clamping dt keeps physics sane after GC pauses or window drags")
            }
        }
    }
}

val physicsSlide = Slide(steps = 2) {
    SlideSurface {
        SlideTitle("Δt physics — semi-implicit Euler")
        Spacer(modifier = Modifier.height(50.dp))
        Reveal(at = 1) {
            CodeBlock(
                code = """
                    // velocity first: v += a · dt
                    var vx = particle.velocityX + particle.gravityX * dt
                    var vy = particle.velocityY + particle.gravityY * dt

                    // then position: p += v · dt
                    var x = particle.position.x + vx * dt
                    var y = particle.position.y + vy * dt
                """,
                fontSize = 30.sp,
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Reveal(at = 2) {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Bullet(text = "Frame-rate independent: same arc at 60 and 120 FPS")
                Bullet(text = "Everything in dp — density handled once, identical on every screen")
                Bullet(text = "Parabolic confetti arcs fall out of two additions per axis")
            }
        }
    }
}

val gravitySlide = Slide(steps = 2) {
    SlideSurface {
        SlideTitle("Gravity is just a vector")
        Spacer(modifier = Modifier.height(50.dp))
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(60.dp)) {
            Column(modifier = Modifier.weight(1.2f)) {
                Reveal(at = 1) {
                    CodeBlock(
                        code = """
                            // 0° = down, 180° = up, ±90° = sideways wind
                            val radians = toRadians(gravityAngle.toDouble())
                            val gravityX = (strength * -sin(radians)).dp
                            val gravityY = (strength *  cos(radians)).dp

                            // each particle launches along its own angle
                            val angle = config.spread.random()
                            val force = config.initialForce.random()
                            val vx = (force * sin(angle.radians)).dp
                            val vy = (-force * cos(angle.radians)).dp
                        """,
                        fontSize = 26.sp,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.spacedBy(40.dp),
            ) {
                Reveal(at = 2, modifier = Modifier.weight(1f).fillMaxWidth()) {
                    DemoCard(modifier = Modifier.fillMaxSize()) {
                        BubblesDemo(width = 520, height = 380)
                    }
                }
                Reveal(at = 2, modifier = Modifier.weight(1f).fillMaxWidth()) {
                    DemoCard(modifier = Modifier.fillMaxSize()) {
                        EmojiRainDemo(width = 520, height = 380)
                    }
                }
            }
        }
    }
}

val emissionSlide = Slide(steps = 2) {
    SlideSurface {
        SlideTitle("Emitting fractions of a particle")
        Spacer(modifier = Modifier.height(50.dp))
        BodyText("37 particles/sec at 120 Hz is 0.3 particles per frame. You can't draw a third of a particle.")
        Spacer(modifier = Modifier.height(40.dp))
        Reveal(at = 1) {
            CodeBlock(
                code = """
                    pendingParticles += particlePerSecond * dt
                    val count = pendingParticles.toInt()  // emit whole particles…
                    pendingParticles -= count             // …carry the fraction over

                    spawnParticles(count)
                """,
                fontSize = 30.sp,
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Reveal(at = 2) {
            Bullet(
                emphasis = "Accumulate, floor, carry the remainder",
                text = "Emission rate stays exact at any refresh rate — no drift, no bursts",
            )
        }
    }
}

private data class EdgeOption(
    val name: String,
    val description: String,
    val behavior: EdgeBehavior,
)

private val edgeOptions = listOf(
    EdgeOption("None", "Particles fly through the bounds (default)", EdgeBehavior.None),
    EdgeOption("Bounce(damping = 0.7)", "Reflect and lose energy on every hit", EdgeBehavior.Bounce(damping = 0.7f)),
    EdgeOption("Stick", "Freeze where they touch the edge", EdgeBehavior.Stick),
    EdgeOption("Wrap", "Exit right, re-enter left — asteroid style", EdgeBehavior.Wrap),
)

val edgeBehaviorSlide = Slide(steps = edgeOptions.lastIndex) {
    val active = step.coerceIn(0, edgeOptions.lastIndex)
    SlideSurface {
        SlideTitle("Edge behavior")
        Spacer(modifier = Modifier.height(70.dp))
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(60.dp)) {
            Column(
                modifier = Modifier.weight(1.1f),
                verticalArrangement = Arrangement.spacedBy(36.dp),
            ) {
                edgeOptions.forEachIndexed { index, option ->
                    Reveal(at = index) {
                        EdgeOptionRow(option = option, selected = index == active)
                    }
                }
            }
            DemoCard(modifier = Modifier.weight(0.9f).fillMaxSize()) {
                EdgeBehaviorDemo(
                    edgeBehavior = edgeOptions[active].behavior,
                    width = 640,
                    height = 760,
                )
            }
        }
    }
}

@Composable
private fun EdgeOptionRow(option: EdgeOption, selected: Boolean) {
    val borderColor = if (selected) DeckColors.accentAlt else DeckColors.codeBorder
    val background = if (selected) DeckColors.surface else Color.Transparent
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(background, RoundedCornerShape(20.dp))
            .border(if (selected) 2.dp else 1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 36.dp, vertical = 26.dp),
    ) {
        Column {
            Text(
                text = option.name,
                color = if (selected) DeckColors.accentAlt else DeckColors.textPrimary,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = option.description,
                color = DeckColors.textSecondary,
                fontSize = 28.sp,
                lineHeight = 38.sp,
            )
        }
    }
}

val performanceSlide = Slide(steps = 3) {
    SlideSurface {
        SlideTitle("How many particles can you afford?")
        Spacer(modifier = Modifier.height(40.dp))
        BodyText(
            "Measured on a Pixel 8 Pro with adb gfxinfo, 10-second windows.",
            color = DeckColors.textSecondary,
        )
        Spacer(modifier = Modifier.height(50.dp))
        Column(verticalArrangement = Arrangement.spacedBy(34.dp)) {
            Reveal(at = 1) {
                BudgetRow("120 FPS", "≤ 1,000 particles/sec", "Tight: ~9 ms median frame")
            }
            Reveal(at = 2) {
                BudgetRow("60 FPS", "≤ 5,000 particles/sec", "The sweet spot — jank under 10%")
            }
            Reveal(at = 3) {
                BudgetRow(
                    "30 FPS", "≤ 10,000 particles/sec",
                    "Use 10 × 1k emitters: 69% more frames than a single 10k one",
                )
            }
        }
    }
}

@Composable
private fun BudgetRow(fps: String, budget: String, note: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeckColors.surface, RoundedCornerShape(20.dp))
            .padding(horizontal = 44.dp, vertical = 34.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = fps,
            color = DeckColors.accentAlt,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(260.dp),
        )
        Text(
            text = budget,
            color = DeckColors.textPrimary,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(560.dp),
        )
        Text(
            text = note,
            color = DeckColors.textSecondary,
            fontSize = 28.sp,
            lineHeight = 38.sp,
        )
    }
}

val perfLessonsSlide = Slide(steps = 3) {
    SlideSurface {
        SlideTitle("Performance lessons that paid off")
        Spacer(modifier = Modifier.height(70.dp))
        Column(verticalArrangement = Arrangement.spacedBy(54.dp)) {
            Reveal(at = 1) {
                Bullet(
                    emphasis = "Cache animation specs per particle",
                    text = "get() properties rebuilt a tween on every access — 1.2M allocations/sec. " +
                        "Constructor defaults instead: +21% frames",
                )
            }
            Reveal(at = 2) {
                Bullet(
                    emphasis = "Delete what nobody reads",
                    text = "An unused id meant UUID.randomUUID() per particle. Removing it: +8% frames",
                )
            }
            Reveal(at = 3) {
                Bullet(
                    emphasis = "Skip invisible work",
                    text = "Early-return when alpha ≤ 0.01 or scale ≤ 0.01: +92% frames, " +
                        "median frame time halved",
                )
            }
        }
    }
}
