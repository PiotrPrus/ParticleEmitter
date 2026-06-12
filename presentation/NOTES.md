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

## 3. Two rendering engines
- ParticlesEmitter = flexibility (any composable), CanvasParticleEmitter = throughput.
- Rule of thumb: if you need more than ~100 particles, go Canvas.

## 4. One frame at a time
- Key insight: don't animate particles individually — run a simulation clock.
- `withFrameNanos` is the same primitive Compose's own animation system is built on.
- Mention: callback gets the frame timestamp in nanos; that drives everything.

## 5. Δt physics
- Semi-implicit Euler: update velocity BEFORE position — more stable than naive Euler.
- dt clamp story: dragging the window or a GC pause would otherwise teleport particles.

## 6. Gravity is just a vector
- Angle convention: 0° down, 180° up, ±90° sideways (wind!).
- Demos right: same code, only gravityAngle differs (bubbles up, snow down).

## 7. Emitting fractions
- Fun bug story material: without the carry, low rates emit nothing and high rates pulse.

## 8. Edge behavior
- Interactive: each → press selects the next behavior AND switches the live demo to it.
- Order: None → Bounce(0.7) → Stick → Wrap. Particles in flight adopt the new rule next frame.
- Bounce damping = energy loss per hit. 1.0 = perpetual motion machine.
- Stick is great for snow piling at the bottom.

## 9. Performance budgets
- All numbers: Pixel 8 Pro, gfxinfo, 10 s windows — details in PERFORMANCE.md in the repo.
- Headline: 5k particles/sec at 60 FPS is the sweet spot.
- Why multi-emitter wins at 10k: each emitter has its own coroutine → parallelizes across cores.

## 10. One config, four platforms
- Four reveals for the platforms, then a fifth for THE REVEAL: this deck is a
  Compose Desktop app; everything animated was live.
- Transition to hands-on part here.

## 11. Setup (guide them, ~10 min)
- kmp.jetbrains.com → check all four targets, "Share UI with Compose".
- Walk the room; pair people with broken Gradle syncs.
- Desktop target = fastest iteration (no emulator).

## 12. Dependency
- Maven Central, no extra repositories. Version 1.1.0.

## 13. First emitter
- Type it live with them if time allows; otherwise let them copy from the slide.
- Point out: BlendMode.Screen makes overlapping particles glow.

## 14. Inspiration (hands-on brief)
- Three placeholder cards — swap in your own animations (GifImage from
  resources, or a live emitter demo) before the talk.
- Challenge ideas are on the slide; encourage interactions (tap, drag, sliders).
- Announce show-and-tell at the end — 3-4 volunteers demo their effect.

## 15. Closing
- Left QR = repo. Right QR = placeholder — generate one for slides/sources before the talk.
- Ask for GitHub stars, mention issues/PRs welcome.
