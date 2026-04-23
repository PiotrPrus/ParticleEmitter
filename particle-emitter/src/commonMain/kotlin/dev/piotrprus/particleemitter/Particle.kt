package dev.piotrprus.particleemitter

import androidx.compose.runtime.Composable
import kotlin.math.PI

data class Particle(
    val id: String,
    val angle: Int,
    val initialForce: Int,
    val gravityStrength: Float,
    val gravityAngle: Int,
    val lifespanMillis: Long,
    val maxHorizontalDisplacementDp: Int,
    val rotationMultiplier: Float,
    val content: @Composable () -> Unit
) {
    val radiants
        get() = angle.toDouble() * PI / 180.0

    val gravityRadians
        get() = gravityAngle.toDouble() * PI / 180.0
}
