package dev.piotrprus.particleemitter

/**
 * Defines how particles behave when they reach the boundary of the emitter's composable.
 */
sealed class EdgeBehavior {
    /**
     * Particles pass through edges freely and disappear off-screen. This is the default behavior.
     */
    data object None : EdgeBehavior()

    /**
     * Particles bounce off edges, reversing their velocity component perpendicular to the boundary.
     * @param damping fraction of velocity retained after each bounce. 1.0 = perfect elastic bounce,
     * 0.5 = half the speed after each bounce. Must be in range [0, 1].
     */
    data class Bounce(val damping: Float = 0.7f) : EdgeBehavior()

    /**
     * Particles stop at the edge and remain there for the rest of their lifespan.
     * Velocity is zeroed on contact with the boundary.
     */
    data object Stick : EdgeBehavior()

    /**
     * Particles that exit one edge reappear on the opposite edge, maintaining their velocity.
     */
    data object Wrap : EdgeBehavior()
}
