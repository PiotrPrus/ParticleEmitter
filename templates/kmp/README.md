# ParticleEmitter — Kotlin Multiplatform Starter

A minimal Compose Multiplatform app with the
[ParticleEmitter](https://github.com/PiotrPrus/ParticleEmitter) library already wired in, running on
**Android, iOS, Desktop (JVM), macOS (native), and Web (Wasm)**. Every platform shows the same
shared UI: one `CanvasParticleEmitter` centered on screen emitting a gentle upward burst.

## Structure

| Module | Purpose |
|--------|---------|
| `shared` | The shared UI (`App.kt`) + the library dependency. Targets Android, JVM, iOS, macOS, Wasm. |
| `androidApp` | Android application (`MainActivity`). |
| `desktopApp` | Desktop (JVM) launcher. |
| `webApp` | Web (Wasm) launcher. |
| `macosApp` | Native macOS launcher. |
| `iosApp` | Xcode project that embeds the `shared` framework. |

The single source of truth for the UI is
[`shared/src/commonMain/kotlin/com/example/particleemitter/App.kt`](shared/src/commonMain/kotlin/com/example/particleemitter/App.kt).
Edit it once and every platform updates.

## Requirements

- JDK 17+
- Android SDK (set `ANDROID_HOME` or add `local.properties` with `sdk.dir=…`) for Android
- Xcode 16+ for iOS and macOS
- A modern browser for Web

## Run each target

```bash
# Android (device/emulator)
./gradlew :androidApp:installDebug

# Desktop (JVM)
./gradlew :desktopApp:run

# Web (Wasm) — opens http://localhost:8080 in your browser
./gradlew :webApp:wasmJsBrowserDevelopmentRun

# macOS (native, Apple silicon)
./gradlew :macosApp:runDebugExecutableMacosArm64

# iOS — open the Xcode project and run on a simulator or device
open iosApp/iosApp.xcodeproj
```

> The iOS build runs `./gradlew :shared:embedAndSignAppleFrameworkForXcode` automatically as a build
> phase — no extra step needed. Set your signing team in `iosApp/Configuration/Config.xcconfig`
> (`TEAM_ID`) if you run on a physical device.

## The dependency

The library is pulled from Maven Central in
[`gradle/libs.versions.toml`](gradle/libs.versions.toml):

```toml
particle-emitter = { module = "io.github.piotrprus:particle-emitter", version.ref = "particle-emitter" }
```

and added to `shared`'s `commonMain` in
[`shared/build.gradle.kts`](shared/build.gradle.kts) via `implementation(libs.particle.emitter)`.
