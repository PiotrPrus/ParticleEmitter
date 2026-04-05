package dev.piotrprus.particleemitter

import androidx.compose.runtime.Composable

data class Particle(
    val id: String,
    val angle: Int,
    val initialForce: Int,
    val gravityMultiplier: Float,
    val lifespanMillis: Long,
    val maxHorizontalDisplacementDp: Int,
    val rotationMultiplier: Float,
    val content: @Composable () -> Unit
) {
    val radiants
        get() = Math.toRadians(angle.toDouble())
}
