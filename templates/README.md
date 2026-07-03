# Starter templates

Ready-to-use starter projects with the
[ParticleEmitter](https://github.com/PiotrPrus/ParticleEmitter) library already wired in. Each one is
a self-contained Gradle project (own wrapper + version catalog) that depends on the published
`io.github.piotrprus:particle-emitter` artifact from Maven Central and shows the simplest possible
scene: one `CanvasParticleEmitter` centered on screen.

| Template | What it is | Quick run |
|----------|------------|-----------|
| [`android-only`](android-only) | A single-module Android app. | `./gradlew :app:installDebug` |
| [`kmp`](kmp) | Compose Multiplatform app for Android, iOS, Desktop (JVM), macOS, and Web (Wasm). | see [its README](kmp/README.md) |

Copy the folder you want and start building — or use it as a reference for wiring the library into
an existing project.
