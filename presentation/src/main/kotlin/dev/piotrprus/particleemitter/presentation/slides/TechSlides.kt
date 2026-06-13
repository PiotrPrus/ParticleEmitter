package dev.piotrprus.particleemitter.presentation.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.piotrprus.particleemitter.EdgeBehavior
import dev.piotrprus.particleemitter.presentation.deck.Slide
import dev.piotrprus.particleemitter.presentation.demos.AmbientParticlesDemo
import dev.piotrprus.particleemitter.presentation.demos.CanvasThroughputDemo
import dev.piotrprus.particleemitter.presentation.demos.EdgeBehaviorDemo
import dev.piotrprus.particleemitter.presentation.demos.GravityShowcaseDemo
import dev.piotrprus.particleemitter.presentation.demos.ConfettiDemo
import dev.piotrprus.particleemitter.presentation.demos.DemoCard
import dev.piotrprus.particleemitter.presentation.demos.LayoutBurstDemo
import dev.piotrprus.particleemitter.presentation.demos.GravityCaseDemo
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
            text = "Piotr Prus @ TILT  ·  GDE Android",
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

val layoutEngineSlide = Slide(steps = 1) {
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
            Reveal(at = 1, modifier = Modifier.weight(1f).fillMaxSize()) {
                LabeledDemo(caption = "Real composables in flight — badges, chips, cards") {
                    LayoutBurstDemo()
                }
            }
        }
    }
}

val canvasEngineSlide = Slide(steps = 1) {
    SlideSurface {
        SlideTitle("Two rendering engines")
        Spacer(modifier = Modifier.height(70.dp))
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(60.dp),
        ) {
            // The explanation greets you as the slide arrives…
            Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                EngineCard(
                    name = "CanvasParticleEmitter",
                    tagline = "Canvas-based",
                    points = listOf(
                        "Zero layout nodes — pure drawBehind",
                        "Shapes, images, text rasterized once",
                        "One frame clock updates the whole world",
                        "Thousands of particles at 60/120 FPS",
                    ),
                    accent = DeckColors.accentAlt,
                )
            }

            // …and the example reveals on the next press.
            Reveal(at = 1, modifier = Modifier.weight(1f).fillMaxSize()) {
                LabeledDemo(caption = "10,000 particles/sec — one Canvas") {
                    CanvasThroughputDemo(width = 820, height = 760)
                }
            }
        }
    }
}

@Composable
private fun LabeledDemo(caption: String, content: @Composable () -> Unit) {
    DemoCard(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
            Text(
                text = caption,
                color = DeckColors.textSecondary,
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 18.dp),
            )
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
        SlideTitle("How a particle's position is calculated")
        Spacer(modifier = Modifier.height(44.dp))
        BodyText("Start point, plus two terms — the throw and the pull:")
        Spacer(modifier = Modifier.height(34.dp))
        PositionFormula()
        Spacer(modifier = Modifier.height(40.dp))
        Reveal(at = 1) {
            CodeBlock(
                code = """
                    // split per axis — 0° launch points up, y grows downward
                    x = startX + force * sin(angle) * t  +  0.5f * gravityX * t * t
                    y = startY - force * cos(angle) * t  +  0.5f * gravityY * t * t
                """,
                fontSize = 26.sp,
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Reveal(at = 2) {
            Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
                Bullet(text = "No gravity → the t² term is zero: a straight line at constant speed")
                Bullet(text = "No initial force → the velocity term is zero: a pure accelerating fall")
                Bullet(text = "Both terms → a parabola, the arc your eye reads as \"thrown\"")
                Bullet(
                    text = "The engine integrates this each frame (v += g·dt, then p += v·dt) " +
                        "— identical arc at 60 or 120 FPS",
                )
            }
        }
    }
}

/** The kinematic equation with its two terms colour-coded to match slide 7. */
@Composable
private fun PositionFormula() {
    val formula = buildAnnotatedString {
        withStyle(SpanStyle(color = DeckColors.textPrimary)) { append("position = start + ") }
        withStyle(SpanStyle(color = DeckColors.warm, fontWeight = FontWeight.Bold)) {
            append("velocity · t")
        }
        withStyle(SpanStyle(color = DeckColors.textPrimary)) { append("  +  ") }
        withStyle(SpanStyle(color = DeckColors.accentAlt, fontWeight = FontWeight.Bold)) {
            append("½ · gravity · t²")
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = formula, fontSize = 46.sp, fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
            FormulaLegend(DeckColors.warm, "velocity · t", "the initial force — the throw")
            FormulaLegend(DeckColors.accentAlt, "½ · gravity · t²", "acceleration — the pull")
        }
    }
}

@Composable
private fun FormulaLegend(color: Color, term: String, meaning: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(modifier = Modifier.size(16.dp).background(color, RoundedCornerShape(4.dp)))
        Column {
            Text(text = term, color = color, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(text = meaning, color = DeckColors.textSecondary, fontSize = 22.sp)
        }
    }
}

val gravityRealismSlide = Slide(steps = 3) {
    SlideSurface {
        SlideTitle("Why gravity makes it feel real")
        Spacer(modifier = Modifier.height(36.dp))
        BodyText(
            "Same emitter, trembling around the centre, ~20 particles/sec. Only the " +
                "force and gravity differ — watch how each changes the motion.",
            color = DeckColors.textSecondary,
        )
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(44.dp),
        ) {
            Reveal(at = 1, modifier = Modifier.weight(1f).fillMaxHeight()) {
                TraceCard("Force only", "g = 0  →  straight line", DeckColors.warm) {
                    GravityCaseDemo(initialForce = 200..300, gravityStrength = 0f, accent = DeckColors.warm)
                }
            }
            Reveal(at = 2, modifier = Modifier.weight(1f).fillMaxHeight()) {
                TraceCard("Gravity only", "v₀ = 0  →  accelerating fall", DeckColors.accentAlt) {
                    GravityCaseDemo(initialForce = 0..0, gravityStrength = 300f, accent = DeckColors.accentAlt)
                }
            }
            Reveal(at = 3, modifier = Modifier.weight(1f).fillMaxHeight()) {
                TraceCard("Force + gravity", "v·t + ½·g·t²  →  arc", DeckColors.accent) {
                    GravityCaseDemo(initialForce = 200..300, gravityStrength = 300f, accent = DeckColors.accent)
                }
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        Reveal(at = 3) {
            BodyText(
                "Linear motion looks like a screensaver. The parabola is what makes confetti " +
                    "just feel right and the eye believes it.",
            )
        }
    }
}

@Composable
private fun TraceCard(title: String, formula: String, accent: Color, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DemoCard(modifier = Modifier.weight(1f).fillMaxWidth()) { content() }
        Text(text = title, color = DeckColors.textPrimary, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
        Text(text = formula, color = accent, fontSize = 24.sp)
    }
}

val gravitySlide = Slide(steps = 2) {
    SlideSurface {
        SlideTitle("Feel the magic with Gravity")
        Spacer(modifier = Modifier.height(50.dp))
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(60.dp)) {
            Reveal(at = 1, modifier = Modifier.weight(1f).fillMaxSize()) {
                DemoCard(modifier = Modifier.fillMaxSize()) {
                    GravityShowcaseDemo()
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
