# Module ParticleEmitter

A Compose Multiplatform particle-effects library for Android, iOS, JVM desktop, and web (JS/Wasm).
It ships two complementary rendering engines:

- [CanvasParticleEmitter][dev.piotrprus.particleemitter.CanvasParticleEmitter] — `Canvas`-based,
  high-performance rendering tuned for 1 000+ lightweight particles (circles, images, text, paths).
- [ParticlesEmitter][dev.piotrprus.particleemitter.ParticlesEmitter] — Compose layout-based, where
  every particle is an arbitrary `@Composable`, so you can fling text, images, or whole UI elements.

Both engines share a physics model: directional gravity, configurable spread, initial force,
rotation, scale/alpha animation, and per-engine edge handling.

## Quick start

```kotlin
CanvasParticleEmitter(
    modifier = Modifier.fillMaxSize(),
    config = CanvasEmitterConfig(
        particlePerSecond = 50,
        emitterCenter = DpOffset(200.dp, 400.dp),
        startRegionShape = CanvasEmitterConfig.Shape.POINT,
        startRegionSize = DpSize(0.dp, 0.dp),
        particleShapes = listOf(ParticleShape.Circle),
        lifespanRange = 800..1200,
        fadeOutTime = 600..1000,
        scaleTime = 800..1200,
        colors = listOf(Color.Cyan, Color.Magenta, Color.Yellow),
        particleSizes = listOf(DpSize(8.dp, 8.dp), DpSize(12.dp, 12.dp)),
    ),
)
```

See the [project README](https://github.com/PiotrPrus/ParticleEmitter) for gravity, edge-behavior,
and multi-emitter recipes, and [PERFORMANCE.md](https://github.com/PiotrPrus/ParticleEmitter/blob/main/PERFORMANCE.md)
for particle-count budgets per target frame rate.

# Package dev.piotrprus.particleemitter

Public API of the library: the two emitter composables
([CanvasParticleEmitter][dev.piotrprus.particleemitter.CanvasParticleEmitter],
[ParticlesEmitter][dev.piotrprus.particleemitter.ParticlesEmitter]),
the [MultiEmitter][dev.piotrprus.particleemitter.MultiEmitter] orchestrator, their configuration
types ([CanvasEmitterConfig][dev.piotrprus.particleemitter.CanvasEmitterConfig],
[EmitterConfig][dev.piotrprus.particleemitter.EmitterConfig]), and the supporting value types
[ParticleShape][dev.piotrprus.particleemitter.ParticleShape] and
[EdgeBehavior][dev.piotrprus.particleemitter.EdgeBehavior].

# Package dev.piotrprus.particleemitter.ui

Canvas drawing helpers — `DrawScope` extensions that render a particle according to its
[ParticleShape][dev.piotrprus.particleemitter.ParticleShape].
