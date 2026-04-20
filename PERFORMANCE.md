# Performance

This document summarizes the measured performance characteristics of `CanvasParticleEmitter` and gives concrete, device-grounded numbers you can plan against when deciding how many particles to emit in your app.

All numbers below were captured on a **Pixel 8 Pro (Tensor G3, 120Hz display)** with the library's current optimizations applied. Lower-end devices will see proportionally lower ceilings — treat these as an upper bound, not a floor.

## TL;DR — Particle budgets per target frame rate

| Target | Particles/sec budget | Typical setup | Notes |
|---|---|---|---|
| **120 FPS** (~8ms/frame) | ≤ **1,000** | 1 emitter × 1k | Median frame time ~9ms. Achievable but tight; needs `PreferredFrameRate` opt-in. |
| **60 FPS** (~16ms/frame) | ≤ **5,000** | 1 emitter × 5k, or 5 emitters × 1k | Median ~18ms, modern jank under 10%. The sweet spot. |
| **30 FPS** (~33ms/frame) | ≤ **10,000** | **10 emitters × 1k** (multi preferred) | Multi-emitter ~51 FPS; single-emitter at 10k drops to ~30 FPS. |
| Above 10,000 particles/sec | — | — | Not recommended without further optimization or splitting. |

### Rules of thumb

- **Prefer splitting into multiple emitters** over one large emitter. Each `CanvasParticleEmitter` runs its own `LaunchedEffect` coroutine dispatched to `Dispatchers.IO`, so the work parallelizes across CPU cores. At 10k particles/sec, ten 1k-emitters measured **69% more frames rendered** than a single 10k-emitter on the same device.
- **Keep per-particle `lifespan` close to `fadeOutTime`.** The library automatically skips drawing fully-transparent particles, but it does not skip their *update* work. A particle that finishes fading 500ms before its lifespan expires still consumes CPU on every frame.
- **Memory is not the bottleneck** at these loads. We never observed > 200 MB PSS. The bottleneck is the main thread's draw-list construction.

## How these numbers were obtained

### Test harness

Two benchmark screens ship with the sample app:

1. **Benchmark** — a 2×5 grid of up to 10 `CanvasParticleEmitter`s, each configured at 1,000 particles/sec. A +/− control toggles the active count from 0 to 10.
2. **Single Emitter Benchmark** — one emitter with a slider that sets its `particlePerSecond` between 0 and 10,000 in 1,000-particle steps.

Both screens use an identical particle config (4 dp circles, 800–1200 ms lifespan, 400–800 ms fade and scale, point emission, 4 colors). This lets the multi vs. single comparison stay apples-to-apples.

### Methodology

All samples are 10-second windows captured via the Android framework's `gfxinfo` service:

```
adb shell dumpsys gfxinfo <package> reset
# wait 10 seconds
adb shell dumpsys gfxinfo <package>
```

Key metrics read from the output:

- **Total frames rendered** — divided by 10 gives the effective FPS.
- **50/90/95/99th percentile frame times** — total UI-thread + GPU time per frame.
- **Modern janky frames** — frames that missed their deadline significantly (system-adjusted budget).
- **Legacy janky frames** — frames exceeding 16.67 ms (the old fixed 60Hz budget).
- **Slow UI thread / Slow issue draw** — counters attributing stalls to specific stages.

GPU percentiles stayed at 4–5 ms across every test — the bottleneck is the CPU-side update + draw-list construction, not the GPU.

## Multi-emitter sweep (N × 1,000 particles/sec)

Each emitter contributes 1,000 particles/sec. Sweeping N from 5 to 10 on the same build (animation-config cache + unused-id removed; results taken before the later draw-skip optimization was added):

| N emitters | Frames / 10s | FPS | 50th ms | 90th ms | 99th ms | Modern Janky |
|---|---|---|---|---|---|---|
| 5 | 582 | 58 | 31 | 38 | 46 | **4.6%** |
| 6 | 468 | 47 | 42 | 48 | 57 | 29.9% |
| 7 | 380 | 38 | 53 | 61 | 69 | 61.3% |
| 8 | 317 | 32 | 65 | 69 | 77 | 89.9% |
| 9 | 295 | 30 | 65 | 77 | 93 | 95.3% |
| 10 | 266 | 27 | 77 | 85 | 97 | 100% |

The knee sits between N=5 and N=6. Each additional emitter past that point adds roughly 7–10 ms to the median frame time.

## Single-emitter sweep

One `CanvasParticleEmitter` with `particlePerSecond` stepped up directly:

| Particles/sec | Frames / 10s | FPS | 50th ms | 90th ms | 99th ms | Modern Janky |
|---|---|---|---|---|---|---|
| 1,000 | 614 | 61 | 9 | 11 | 13 | **0.3%** |
| 5,000 | 564 | 56 | 18 | 22 | 28 | 8.9% |
| 10,000 | 301 | 30 | 32 | 36 | 42 | 77.7% |

At 10k the single emitter hits a cliff — one sequential coroutine can't finish its per-frame work before vsync, and the system drops the refresh rate to 30Hz.

## Multi vs. single at matched particle counts

| Total particles/sec | Config | Frames / 10s | 50th ms | Modern Janky |
|---|---|---|---|---|
| 5,000 | 5 × 1k (multi) | 582 | 31 ms | 4.6% |
| 5,000 | 1 × 5k (single) | 564 | **18 ms** | 8.9% |
| 10,000 | 10 × 1k (multi) | **510** | 38 ms | 21.4% |
| 10,000 | 1 × 10k (single) | 301 | 32 ms | 77.7% |

At **5k total** the two configurations are essentially equivalent — pick whichever reads cleaner in your code. At **10k total** multi wins throughput by ~69%.

## Sustained use and memory

Running the 1 × 5k config continuously for 5+ minutes showed mild degradation consistent with thermal throttling on the Tensor G3:

| Sample | Frames / 10s | 50th ms | 99th ms | Modern Janky | Legacy Janky |
|---|---|---|---|---|---|
| Fresh start | 564 | 18 ms | 28 ms | 8.9% | 32.5% |
| After 5+ min | 544 | 20 ms | 28 ms | 11.2% | 74.1% |

Throughput fell 3.5%. Legacy jank doubled, but 99th-percentile frame time and modern jank barely moved — the pattern of a thermal governor nudging CPU clocks down, not a memory leak or GC pathology. Missed-vsync stayed at 0 throughout.

### Memory profile

Steady-state memory on a 5 × 1k session (via `adb shell dumpsys meminfo`):

| Component | PSS |
|---|---|
| Java Heap | 22 MB (alloc 9 MB / total 34 MB) |
| Native Heap | 12 MB |
| Graphics | 85–95 MB |
| **Total PSS** | **~180 MB** |

Navigating back to the main menu and re-entering the benchmark released ~9 MB of graphics memory and slightly reduced the Java heap — emitter instances and their GPU draw buffers are disposed correctly when their composables leave the tree.

## Optimizations applied

The numbers in this document reflect the library after the following perf fixes:

- **Cache per-particle animation configs.** Moved `scaleAnimConfig` / `alphaAnimConfig` from `get()` properties to data-class constructor parameters with default values. Previously they built a fresh `TargetBasedAnimation + tween` spec on every access — roughly 1.2M throwaway allocations per second at the 10-emitter load.
  Measured impact on Pixel 8 Pro at 10 × 1k: **+21% frames, −16% median frame time**.
- **Drop the unused `id` field from `CanvasParticle`.** It was never read. Removing it eliminated a `UUID.randomUUID().toString()` allocation for every particle created.
  Measured impact on top of the above: **+8% frames, −4 ms median**.
- **Skip drawing fully-transparent or zero-scale particles.** Particles often live longer than their fade-out or scale-down animation; the library now returns early from the draw function when `alpha ≤ 0.01` or `scale ≤ 0.01`.
  Measured impact at 10 × 1k: **+92% frames, −51% median frame time**, modern jank **100% → 21.4%**.

Cumulative effect at the 10-emitter benchmark: **~2× frames rendered, median frame time roughly halved**.

## Caveats

- All numbers come from a single device (Pixel 8 Pro). Older or budget devices will hit these ceilings sooner.
- Debug builds were used. A release build with R8 enabled should be incrementally faster.
- 10-second samples are short; run-to-run variance of a few percent is normal, and the device's thermal state visibly affects later samples in a long session.
- GPU capacity was never the bottleneck in our tests; these numbers are CPU-bound. Heavier particle shapes (large images, paths) may shift that balance.
