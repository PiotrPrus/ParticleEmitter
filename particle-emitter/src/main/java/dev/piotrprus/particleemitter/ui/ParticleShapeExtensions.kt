package dev.piotrprus.particleemitter.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.toSize
import dev.piotrprus.particleemitter.CanvasParticle
import dev.piotrprus.particleemitter.ParticleShape

fun DrawScope.draw(
    canvasParticle: CanvasParticle
) {
    if (canvasParticle.alpha <= 0.01f || canvasParticle.scale <= 0.01f) return
    when (canvasParticle.shape) {
        ParticleShape.Circle -> {
            val centerX = canvasParticle.currentPosition.x.toPx()
            val centerY = canvasParticle.currentPosition.y.toPx()
            withTransform({
                scale(
                    scaleX = canvasParticle.scale,
                    scaleY = canvasParticle.scale,
                    pivot = Offset(centerX, centerY)
                )
            }) {
                drawCircle(
                    color = canvasParticle.color,
                    radius = canvasParticle.size.toSize().width / 2,
                    center = Offset(
                        x = centerX,
                        y = centerY
                    ), blendMode = canvasParticle.blendMode,
                    alpha = canvasParticle.alpha
                )
            }
        }

        is ParticleShape.Image -> {
            val centerX = canvasParticle.currentPosition.x.toPx()
            val centerY = canvasParticle.currentPosition.y.toPx()
            val colorFilter = ColorFilter.tint(color = canvasParticle.color)
            withTransform({
                scale(
                    scaleX = canvasParticle.scale,
                    scaleY = canvasParticle.scale,
                    pivot = Offset(centerX, centerY)
                )
                rotate(
                    degrees = canvasParticle.rotation * canvasParticle.scale,
                    Offset(centerX, centerY)
                )
                translate(
                    left = centerX - canvasParticle.shape.imageBitmap.width / 2,
                    top = centerY - canvasParticle.shape.imageBitmap.height / 2
                )
            }) {
                drawImage(
                    image = canvasParticle.shape.imageBitmap,
                    blendMode = canvasParticle.blendMode,
                    alpha = canvasParticle.alpha,
                    colorFilter = colorFilter,
                    filterQuality = FilterQuality.Low
                )
            }
        }

        is ParticleShape.PathShape -> {
            val centerX = canvasParticle.currentPosition.x.toPx()
            val centerY = canvasParticle.currentPosition.y.toPx()
            val width = canvasParticle.size.toSize().width
            val height = canvasParticle.size.toSize().height

            withTransform({
                scale(
                    scaleX = canvasParticle.scale,
                    scaleY = canvasParticle.scale,
                    pivot = Offset(centerX, centerY)
                )
                rotate(
                    degrees = canvasParticle.rotation * canvasParticle.scale,
                    Offset(centerX, centerY)
                )
                translate(left = centerX - width / 6, top = centerY - height / 6)
            }) {
                drawPath(
                    color = canvasParticle.color,
                    path = canvasParticle.shape.shapePath,
                    alpha = canvasParticle.alpha,
                    blendMode = canvasParticle.blendMode
                )
            }
        }

        is ParticleShape.Text -> {
            val centerX = canvasParticle.currentPosition.x.toPx()
            val centerY = canvasParticle.currentPosition.y.toPx()
            val layoutResult = canvasParticle.shape.layoutResult
            val textSize = layoutResult.size.toSize()
            withTransform({
                scale(
                    scaleX = canvasParticle.scale,
                    scaleY = canvasParticle.scale,
                    pivot = Offset(centerX, centerY)
                )
                rotate(
                    degrees = canvasParticle.rotation * canvasParticle.scale,
                    Offset(centerX, centerY)
                )
                translate(left = centerX, top = centerY)
            }) {
                drawText(
                    textLayoutResult = layoutResult,
                    color = canvasParticle.color,
                    topLeft = Offset(-textSize.width / 2f, -textSize.height / 2f),
                    alpha = canvasParticle.alpha,
                    blendMode = canvasParticle.blendMode,
                )
            }
        }
    }
}

fun ParticleShape.Image.draw(
    drawScope: DrawScope,
    canvasParticle: CanvasParticle,
) = with(drawScope) {
    val centerX = canvasParticle.currentPosition.x.toPx()
    val centerY = canvasParticle.currentPosition.y.toPx()
    val colorFilter = ColorFilter.tint(color = canvasParticle.color)
    withTransform({
        scale(
            scaleX = canvasParticle.scale,
            scaleY = canvasParticle.scale,
            pivot = Offset(centerX, centerY)
        )
        rotate(
            degrees = canvasParticle.rotation * canvasParticle.scale,
            Offset(centerX, centerY)
        )
        translate(
            left = centerX - imageBitmap.width / 2,
            top = centerY - imageBitmap.height / 2
        )
    }) {
        drawImage(
            image = imageBitmap,
            blendMode = canvasParticle.blendMode,
            alpha = canvasParticle.alpha,
            colorFilter = colorFilter,
            filterQuality = FilterQuality.Low
        )
    }
}

fun ParticleShape.Circle.draw(drawScope: DrawScope, canvasParticle: CanvasParticle) =
    with(drawScope) {
        val centerX = canvasParticle.currentPosition.x.toPx()
        val centerY = canvasParticle.currentPosition.y.toPx()
        withTransform({
            scale(
                scaleX = canvasParticle.scale,
                scaleY = canvasParticle.scale,
                pivot = Offset(centerX, centerY)
            )
        }) {
            drawCircle(
                color = canvasParticle.color,
                radius = canvasParticle.size.toSize().width / 2,
                center = Offset(
                    x = centerX,
                    y = centerY
                ), blendMode = canvasParticle.blendMode,
                alpha = canvasParticle.alpha
            )
        }
    }

fun ParticleShape.PathShape.draw(drawScope: DrawScope, canvasParticle: CanvasParticle) =
    with(drawScope) {
        val centerX = canvasParticle.currentPosition.x.toPx()
        val centerY = canvasParticle.currentPosition.y.toPx()
        val width = canvasParticle.size.toSize().width
        val height = canvasParticle.size.toSize().height

        withTransform({
            scale(
                scaleX = canvasParticle.scale,
                scaleY = canvasParticle.scale,
                pivot = Offset(centerX, centerY)
            )
            rotate(
                degrees = canvasParticle.rotation * canvasParticle.scale,
                Offset(centerX, centerY)
            )
            translate(left = centerX - width / 6, top = centerY - height / 6)
        }) {
            drawPath(
                color = canvasParticle.color,
                path = shapePath,
                alpha = canvasParticle.alpha,
                blendMode = canvasParticle.blendMode
            )
        }
    }