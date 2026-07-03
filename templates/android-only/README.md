# ParticleEmitter — Android Starter

A minimal, single-module Android app with the
[ParticleEmitter](https://github.com/PiotrPrus/ParticleEmitter) library already wired in. It shows
the simplest possible use: one `CanvasParticleEmitter` centered on screen emitting a gentle upward
burst.

## Requirements

- JDK 17+
- Android SDK (set `ANDROID_HOME`, or add a `local.properties` with `sdk.dir=/path/to/Android/sdk`)
- An emulator or a device with USB debugging

## Run

```bash
./gradlew :app:installDebug
```

Then launch **Particle Emitter Starter** from the launcher, or:

```bash
adb shell am start -n com.example.particleemitter/.MainActivity
```

## Where the particles come from

Everything lives in
[`app/src/main/kotlin/com/example/particleemitter/MainActivity.kt`](app/src/main/kotlin/com/example/particleemitter/MainActivity.kt).
The `CenteredParticles` composable creates a single `CanvasParticleEmitter` and centers it with
`BoxWithConstraints`. Tweak the `CanvasEmitterConfig` (colors, `spread`, `gravityStrength`,
`startRegionShape`, …) to make it your own.

## The dependency

The library is pulled from Maven Central in
[`gradle/libs.versions.toml`](gradle/libs.versions.toml):

```toml
particle-emitter = { module = "io.github.piotrprus:particle-emitter", version.ref = "particle-emitter" }
```

and added in [`app/build.gradle.kts`](app/build.gradle.kts) via `implementation(libs.particle.emitter)`.
