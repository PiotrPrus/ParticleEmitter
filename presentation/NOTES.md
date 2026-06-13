# Speaker notes — ParticleEmitter workshop (GDG London)

**Format:** 60–90 min · ~15 min talk · ~10 min guided setup · 25–30 min hands-on · wrap-up & show-and-tell.

**Controls:** `→` / `Space` / `Enter` = next reveal/slide · `←` = previous slide (fully revealed) · `Esc` = leave fullscreen · `F` = back to fullscreen.

Run with: `./gradlew :presentation:run`

---

## 1. Title
- Welcome, who I am, one-liner about the library.
- Mention the meta-joke early or save for slide 11: the deck itself is Compose Desktop.

## 2. What is ParticleEmitter?
- Live confetti on the right is the library running in this very window.
- Story: started as Android-only confetti experiment, became full KMP library.

## 3. Two rendering engines — layout
- One reveal: ParticlesEmitter card + live layout burst on the right. Each
  particle is a real composable with STRUCTURE — bordered circular badges,
  Row chips with a dot + label, Column cards with title + "@Composable"
  subtitle — mixed with a few emoji (~140 in flight).
- Flexibility pitch: point at a chip/card mid-air — "that's a full Compose
  layout, background + border + nested Row/Column, not a sprite."

## 4. Two rendering engines — canvas
- Same title, the throughput half. CanvasParticleEmitter card is there on arrival;
  one reveal fires up the 4,000 particles/sec firehose on the right.
- Contrast with previous slide: 40 layout nodes vs thousands on one Canvas, 60 FPS.
- Rule of thumb: if you need more than ~100 particles, go Canvas.

## 5. One frame at a time
- Key insight: don't animate particles individually — run a simulation clock.
- `withFrameNanos` is the same primitive Compose's own animation system is built on.
- Mention: callback gets the frame timestamp in nanos; that drives everything.

## 6. How position is calculated
- The whole point: position = start + velocity·t + ½·gravity·t² — the throw
  (initial force) plus the pull (gravity). Coloured terms match slide 7.
- Reveal 1: the same equation split per axis, straight from the layout engine's
  code (x uses sin, y uses −cos because 0° points up and y grows downward).
- Reveal 2: when each term vanishes (no gravity → straight line; no force →
  pure fall) and the note that the engine integrates it incrementally each
  frame (semi-implicit Euler) so it's frame-rate independent.

## 7. Why gravity makes it feel real (NEW)
- Three live emitters, revealed left to right. Same setup — emitter trembling
  around the centre, ~20 particles/sec — only force and gravity differ.
  - Force only (g=0): sparks shoot out and keep going straight, constant speed.
  - Gravity only (v₀=0): no throw, particles just fall from the centre and
    visibly accelerate downward.
  - Force + gravity: thrown up and pulled down → the parabola / fountain arc.
- Punchline (reveals with the arc): linear motion looks like a screensaver;
  the parabola is what makes confetti fall and embers rise. Add gravity and
  the eye believes it. THIS is the slide's takeaway.

## 8. Gravity is just a vector
- Angle convention: 0° down, 180° up, ±90° sideways (wind!).
- Code left: gravity and launch velocity are both (x, y) vectors.
- Demo right (reveal 2): emitter is FIXED in the centre, throwing sparks in
  every direction. The cyan gravity point orbits around it; the faint line is
  the current pull direction. Each frame gravityAngle is re-aimed centre→point,
  so the spray bends to chase it — sloshing around the orbit. THE practical
  "why physics matters" payoff. Let it run; talk over it.
- Honest caveat if asked: the engine bakes gravity per-particle at birth, so a
  spark's own arc is fixed once launched — it's the newly emitted stream that
  swings toward the moving point. The aggregate still reads as a living field.

## 9. Emitting fractions
- Fun bug story material: without the carry, low rates emit nothing and high rates pulse.

## 10. Edge behavior
- Interactive: each → press selects the next behavior AND switches the live demo to it.
- Order: None → Bounce(0.7) → Stick → Wrap. Particles in flight adopt the new rule next frame.
- Bounce damping = energy loss per hit. 1.0 = perpetual motion machine.
- Stick is great for snow piling at the bottom.

## 11. Performance budgets
- All numbers: Pixel 8 Pro, gfxinfo, 10 s windows — details in PERFORMANCE.md in the repo.
- Headline: 5k particles/sec at 60 FPS is the sweet spot.
- Why multi-emitter wins at 10k: each emitter has its own coroutine → parallelizes across cores.

## 12. One config, four platforms
- Four reveals for the platforms, then a fifth for THE REVEAL: this deck is a
  Compose Desktop app; everything animated was live.
- Transition to hands-on part here.

## 13. Setup (guide them, ~10 min)
- Straight from Android Studio — no website, no wizard. New Project →
  Empty Activity (Compose) for the simple path, or the Kotlin Multiplatform
  template for people who want all targets.
- Step 2 is just adding the Maven Central dependency — snippet on the next slide.
- Walk the room; pair people with broken Gradle syncs.

## 14. Dependency
- Maven Central, no extra repositories. Version 1.1.0.
- Two reveals: plain Android variant (app/build.gradle.kts) first, then the
  multiplatform variant (commonMain.dependencies) — pick whichever matches
  the project you created on the previous slide.

## 15. First emitter
- Type it live with them if time allows; otherwise let them copy from the slide.
- Point out: BlendMode.Screen makes overlapping particles glow.

## 16. Inspiration (hands-on brief)
- All three cards are live emitters:
  - Campfire flame — three stacked emitters (red-orange body, yellow core,
    stray sparks), BlendMode.Plus, gravity up, particles shrink to zero.
  - Matrix rain — katakana/digit glyphs via ParticleShape.Text, two layers
    (faint dark-green background, bright phosphor foreground), BlendMode.Plus
    glow, no rotation so the glyphs stay upright.
  - Magic wand — the sample app's wand, self-driving on a Lissajous curve,
    trailing star-shaped PathShape particles.
- Challenge ideas are on the slide; encourage interactions (tap, drag, sliders).
- Announce show-and-tell at the end — 3-4 volunteers demo their effect.

## 17. Closing
- Left QR = repo. Right QR = placeholder — generate one for slides/sources before the talk.
- Ask for GitHub stars, mention issues/PRs welcome.
