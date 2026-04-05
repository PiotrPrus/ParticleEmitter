# ParticleEmitter

A Jetpack Compose particle effects library for Android. Create beautiful, physics-based particle animations with two rendering approaches optimized for different use cases.

## Features

- **Two rendering engines:**
  - `ParticlesEmitter` — Compose layout-based, supports custom `@Composable` particles (text, images, shapes)
  - `CanvasParticleEmitter` — Canvas-based, high-performance rendering for 100+ particles
- **Physics simulation** — Gravity, force, angle, rotation, horizontal displacement
- **Flexible particle shapes** — Circles, images with tinting, custom paths
- **Configurable easing** — Per-particle easing curves for position, scale, and alpha
- **Blend modes** — Additive, screen, and other blend effects for glowing particles
- **Multi-emitter orchestration** — Sequential or overlapping emitters with `MultiEmitter`
- **Emitter source shapes** — Point, oval, rectangle, vertical/horizontal lines

## Modules

| Module | Description |
|--------|-------------|
| `particle-emitter` | Core library — emitters, particle models, configs, rendering |
| `sample` | Demo app showcasing different particle effects |

## Usage

### Canvas Emitter (recommended for performance)

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
        spread = IntRange(-90, 90),
        blendMode = BlendMode.Screen,
        flyDistancesDp = IntRange(50, 150),
    )
)
```

### Compose Emitter (for custom particle content)

```kotlin
ParticlesEmitter(
    config = EmitterConfig(
        particlesCount = 30,
        emitDurationMillis = 1000L,
        particleLifespanMillis = 2000L,
        initialForce = 80,
        gravityMultiplier = 1f,
        spread = IntRange(-45, 45),
    ) {
        // Any @Composable content as a particle
        Text("✨", fontSize = 20.sp)
    }
)
```

### Multi Emitter (sequential bursts)

```kotlin
MultiEmitter(
    modifier = Modifier.fillMaxSize(),
    emitterCount = 5,
    emitterDelay = 200L,
    emitterConfig = EmitterConfig(
        particlesCount = 20,
        particleLifespanMillis = 1500L,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color.White, CircleShape)
        )
    }
)
```

## Particle Shapes

```kotlin
// Circle
ParticleShape.Circle

// Image with tinting
ParticleShape.Image(ImageBitmap.imageResource(R.drawable.star))

// Custom path
ParticleShape.PathShape(myCustomPath)
```

## Building

```bash
# Build library
./gradlew :particle-emitter:assembleRelease

# Run sample app
./gradlew :sample:installDebug
```

## License

Apache License 2.0
