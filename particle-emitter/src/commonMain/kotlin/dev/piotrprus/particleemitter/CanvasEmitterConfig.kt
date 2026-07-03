package dev.piotrprus.particleemitter

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Configuration for [CanvasParticleEmitter] — describes how many particles to emit, where and in
 * what shape they originate, how they look, and the physics that governs their motion.
 *
 * Ranges (such as [lifespanRange], [fadeOutTime], or [spread]) and list properties (such as
 * [colors], [particleShapes], or [particleSizes]) are sampled randomly per particle, so a single
 * config can produce a varied, natural-looking effect. All defaults are tuned for a gentle upward
 * burst with no gravity; override [gravityStrength]/[gravityAngle] for confetti, rain, or bubbles,
 * and [edgeBehavior] to make particles bounce, stick, or wrap at the emitter bounds.
 *
 * @param particlePerSecond is number of particles emitted by this source in 1sec. The emission happens every 100ms = 0,1s, so value less then 10 will be neglected.
 * @param emitterCenter is DpOffset for the center of Emitter
 * @param startRegionShape is the shape(path) for emitter. For example Point means every particle will be created at the same place [emitterCenter]
 * @param startRegionSize is DpSize of region where emission happens. The source for each particle will be picked randomly from the [startRegionShape] — along its circumference for outline shapes ([Shape.OVAL], [Shape.RECT]) or from anywhere inside for solid shapes ([Shape.SOLID_OVAL], [Shape.SOLID_RECT]).
 * @param particleShapes - list of shapes for particles. The emitter will pick randomly from the list of available shapes
 * @param lifespanRange - duration of one particle. It is an IntRange to randomize the particle life
 * @param fadeOutTime - duration of fadeOut animation. Each particle can have random fadeOut time, so it is IntRange
 * @param scaleTime - duration of scaling animation. Use [startScaleRange] and [targetScaleRange] to control the parameters of particle scale
 * @param colors - a list of colors that each particle will randomly pick from
 * @param particleSizes - a list of sizes in [DpSize] for each particle. It will be picked randomly from available sizes. The size applies to every [ParticleShape] — including [ParticleShape.Image], whose bitmap is resized to this size regardless of the source asset's intrinsic resolution. The size can be also manipulated using [startScaleRange] and [targetScaleRange]
 * @param spread - range of angles (in degrees) that describe the direction of path for each particle. 0 degrees points upward (top of the screen)
 * @param blendMode - applied [BlendMode] on each particle
 * @param alphaEasing - an easing curve that is applied for alpha animation on each particle
 * @param scaleEasing - an easing curve that is applied for scale animation on each particle
 * @param initialForce - initial velocity magnitude for each particle. Higher values make particles move faster from the emission point. The value is picked randomly from the provided range.
 * @param rotationRange - range of angles (in degrees) that each particle will rotate during translation
 * @param targetScaleRange - end scale for each particle
 * @param startScaleRange - start scale for each particle
 * @param gravityStrength - strength of gravitational force applied to particles in Dp/s². A value of 0 means no gravity. Higher values create stronger pull.
 * @param gravityAngle - direction of gravity in degrees. 0 degrees points downward (bottom of the screen), 90 degrees points left, -90 degrees points right, 180 degrees points upward.
 * @param edgeBehavior - defines how particles behave when they reach the composable boundary. See [EdgeBehavior] for options: [EdgeBehavior.None] (default), [EdgeBehavior.Bounce], [EdgeBehavior.Stick], [EdgeBehavior.Wrap].
 * @param hideInStartRegion - when `true`, particles whose current position falls inside the start region (as defined by [startRegionShape] and [startRegionSize]) are not drawn. Useful for ring emitters with 360° spread where particles crossing the interior would otherwise clutter the center. Default is `false`.
 *
 */

data class CanvasEmitterConfig(
    val particlePerSecond: Int,
    val emitterCenter: DpOffset,
    val startRegionShape: Shape,
    val startRegionSize: DpSize,
    val particleShapes: List<ParticleShape>,
    val lifespanRange: IntRange,
    val fadeOutTime: IntRange,
    val scaleTime: IntRange,
    val colors: List<Color>,
    val particleSizes: List<DpSize>,
    val spread: IntRange = IntRange(-180, 180),
    val blendMode: BlendMode = BlendMode.SrcOver,
    val alphaEasing: Easing = LinearEasing,
    val scaleEasing: Easing = LinearEasing,
    val initialForce: IntRange = IntRange(10, 100),
    val rotationRange: IntRange = IntRange(-180, 180),
    val startScaleRange: IntRange = IntRange(0,1),
    val targetScaleRange: IntRange = IntRange(1,2),
    val gravityStrength: Float = 0f,
    val gravityAngle: Int = 0,
    val edgeBehavior: EdgeBehavior = EdgeBehavior.None,
    val hideInStartRegion: Boolean = false,
) {
    /**
     * Returns `true` if [pos] currently falls inside the start region described by
     * [startRegionShape] and [startRegionSize], centered on [emitterCenter]. Used by the emitter
     * to honor [hideInStartRegion]. [Shape.POINT], [Shape.H_LINE], and [Shape.V_LINE] have no
     * interior, so this always returns `false` for them.
     */
    fun isInsideStartRegion(pos: DpOffset): Boolean {
        val dx = (pos.x - emitterCenter.x).value
        val dy = (pos.y - emitterCenter.y).value
        return when (startRegionShape) {
            Shape.POINT -> false
            Shape.OVAL, Shape.SOLID_OVAL -> {
                val rx = startRegionSize.width.value / 2f
                val ry = startRegionSize.height.value / 2f
                if (rx <= 0f || ry <= 0f) return false
                val nx = dx / rx
                val ny = dy / ry
                nx * nx + ny * ny < 1f
            }
            Shape.RECT, Shape.SOLID_RECT -> {
                val halfW = startRegionSize.width.value / 2f
                val halfH = startRegionSize.height.value / 2f
                kotlin.math.abs(dx) < halfW && kotlin.math.abs(dy) < halfH
            }
            Shape.H_LINE, Shape.V_LINE -> false
        }
    }

    /**
     * A fresh emission point for the next particle, sampled from the start region. For [Shape.POINT]
     * this is always [emitterCenter]; for outline shapes it is a random point along the region's
     * circumference, and for the solid shapes it is a random point uniformly within the region's
     * area, so reading this property repeatedly yields a spread of origins across the shape.
     */
    val startPoint: DpOffset
        get() = when (startRegionShape) {
            Shape.OVAL -> getRandomOffsetOnCircle(emitterCenter, startRegionSize)
            Shape.RECT -> getRandomOffsetOnRect(emitterCenter, startRegionSize)
            Shape.SOLID_OVAL -> getRandomOffsetInOval(emitterCenter, startRegionSize)
            Shape.SOLID_RECT -> getRandomOffsetInRect(emitterCenter, startRegionSize)
            Shape.V_LINE -> getRandomOffsetOnVertLine(emitterCenter, startRegionSize)
            Shape.H_LINE -> getRandomOffsetOnHorizontalLine(emitterCenter, startRegionSize)
            Shape.POINT -> emitterCenter
        }

    private fun getRandomOffsetInRect(emitterCenter: DpOffset, startRegionSize: DpSize): DpOffset {
        val halfW = startRegionSize.width.value / 2f
        val halfH = startRegionSize.height.value / 2f
        val x = emitterCenter.x.value + (Random.nextFloat() * 2f - 1f) * halfW
        val y = emitterCenter.y.value + (Random.nextFloat() * 2f - 1f) * halfH
        return DpOffset(x.dp, y.dp)
    }

    private fun getRandomOffsetInOval(emitterCenter: DpOffset, startRegionSize: DpSize): DpOffset {
        val rx = startRegionSize.width.value / 2f
        val ry = startRegionSize.height.value / 2f
        // sqrt(random) radius keeps the distribution uniform over area instead of clustering near the center.
        val radius = sqrt(Random.nextFloat())
        val angle = Random.nextFloat() * 2f * PI.toFloat()
        val x = emitterCenter.x.value + radius * rx * cos(angle)
        val y = emitterCenter.y.value + radius * ry * sin(angle)
        return DpOffset(x.dp, y.dp)
    }

    private fun getRandomOffsetOnVertLine(emitterCenter: DpOffset, startRegionSize: DpSize): DpOffset {
        val distance = startRegionSize.height.value / 100
        val points = ((emitterCenter.y - startRegionSize.height / 2).value.toInt() until
                (emitterCenter.y + startRegionSize.height).value.toInt() step distance.toInt()
            .coerceAtLeast(1)).toList()
        return DpOffset(x = emitterCenter.x, y = points.random().dp)
    }

    private fun getRandomOffsetOnHorizontalLine(
        emitterCenter: DpOffset,
        startRegionSize: DpSize
    ): DpOffset {
        val distance = startRegionSize.width.value / 100
        val points = ((emitterCenter.x - startRegionSize.width / 2).value.toInt() until
                (emitterCenter.x + startRegionSize.width).value.toInt() step distance.toInt()
            .coerceAtLeast(1)).toList()
        return DpOffset(x = points.random().dp, y = emitterCenter.y)
    }

    private fun getRandomOffsetOnRect(emitterCenter: DpOffset, startRegionSize: DpSize): DpOffset {
        val points = mutableListOf<DpOffset>()
        val circumference = (startRegionSize.width + startRegionSize.height) * 2
        val step = circumference/100
        val pointsInWidth = (startRegionSize.width / step).toInt()
        val pointsInHeight = (startRegionSize.height / step).toInt()

        repeat(pointsInWidth) { i ->
            points.add(DpOffset(x = step * i, y = 0.dp))
        }
        repeat(pointsInHeight) { i ->
            points.add(DpOffset(x = startRegionSize.width, y = step * i))
        }
        repeat(pointsInWidth) { i ->
            points.add(DpOffset(x = startRegionSize.width - step * i, y = startRegionSize.height))
        }
        repeat(pointsInHeight) { i ->
            points.add(DpOffset(x = 0.dp, y = startRegionSize.height - step * i))
        }

        return points.random() + emitterCenter - startRegionSize.center
    }

    private fun getRandomOffsetOnCircle(emitterCenter: DpOffset, startRegionSize: DpSize): DpOffset {
        val radius = minOf(startRegionSize.height, startRegionSize.width)/2
        return (0..100).map { pointIndex ->
            val angle = pointIndex * 2 * PI / 100
            val x = emitterCenter.x + radius * cos(angle).toFloat()
            val y = emitterCenter.y + radius * sin(angle).toFloat()
            DpOffset(x, y)
        }.random()
    }


    /**
     * The geometry of the emitter's start region — the locus from which particles originate.
     *
     * - [OVAL] — particles are emitted from the perimeter of an ellipse (a ring emitter).
     * - [RECT] — particles are emitted from the perimeter of a rectangle.
     * - [SOLID_OVAL] — particles are emitted from anywhere inside the ellipse (uniform over its area).
     * - [SOLID_RECT] — particles are emitted from anywhere inside the rectangle (uniform over its area).
     * - [V_LINE] — a vertical line segment.
     * - [H_LINE] — a horizontal line segment.
     * - [POINT] — every particle starts at [emitterCenter].
     */
    enum class Shape {
        OVAL, RECT, V_LINE, H_LINE, POINT, SOLID_OVAL, SOLID_RECT
    }
}
