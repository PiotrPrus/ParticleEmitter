package dev.piotrprus.particleemitter.presentation.deck

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * A single slide. [steps] is the number of reveal steps *beyond* the initial state:
 * a slide with steps = 3 needs 3 right-arrow presses before the next press moves
 * to the following slide.
 */
class Slide(
    val steps: Int = 0,
    val content: @Composable SlideScope.() -> Unit,
)

/**
 * Exposes the current reveal step to slide content. Content gated behind
 * [Reveal] fades in once the deck reaches its step.
 */
class SlideScope(val step: Int) {

    /**
     * Reserves its layout space immediately (Keynote-style "appear" build) and
     * fades/floats the content in when the deck reaches [at].
     */
    @Composable
    fun Reveal(
        at: Int,
        modifier: Modifier = Modifier,
        content: @Composable BoxScope.() -> Unit,
    ) {
        val visible = step >= at
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = tween(durationMillis = 600),
        )
        val shift by animateFloatAsState(
            targetValue = if (visible) 0f else 1f,
            animationSpec = tween(durationMillis = 600),
        )
        Box(
            modifier = modifier.graphicsLayer {
                this.alpha = alpha
                translationY = shift * 24.dp.toPx()
            },
            content = content,
        )
    }
}

class DeckState(val slides: List<Slide>) {
    var slideIndex by mutableStateOf(0)
        private set

    /** Current reveal step of the current slide. */
    var step by mutableStateOf(0)
        private set

    fun next() {
        val slide = slides[slideIndex]
        when {
            step < slide.steps -> step++
            slideIndex < slides.lastIndex -> {
                slideIndex++
                step = 0
            }
        }
    }

    fun previous() {
        if (slideIndex > 0) {
            slideIndex--
            step = slides[slideIndex].steps
        }
    }
}
