package dev.piotrprus.particleemitter

import androidx.compose.ui.geometry.Offset

/**
 * A rectangular range of [Offset] points spanning from [startOffset] to [endOffset].
 *
 * Iterating walks the integer grid row by row (incrementing x, then wrapping to the next y), while
 * [random] returns a uniformly random point inside the rectangle.
 *
 * @param startOffset the inclusive top-left corner of the range.
 * @param endOffset the bottom-right corner of the range.
 */
internal class OffsetRange(
    private val startOffset: Offset,
    private val endOffset: Offset
) : Iterable<Pair<Float, Float>> {

    override fun iterator(): Iterator<Pair<Float, Float>> {
        return OffsetIterator()
    }

    /** Iterates the integer grid of points in the range, row by row. */
    inner class OffsetIterator : Iterator<Pair<Float, Float>> {
        private var currentX = startOffset.x
        private var currentY = startOffset.y

        override fun hasNext(): Boolean {
            return currentY < endOffset.y || (currentY == endOffset.y && currentX < endOffset.x)
        }

        override fun next(): Pair<Float, Float> {
            val result = Pair(currentX, currentY)
            currentX++
            if (currentX > endOffset.x) {
                currentX = startOffset.x
                currentY++
            }
            return result
        }
    }

    /** Returns a uniformly random [Offset] within the range. */
    fun random(): Offset {
        val xRandom = (startOffset.x.toInt()..endOffset.x.toInt()).random().toFloat()
        val yRandom = (startOffset.y.toInt()..endOffset.y.toInt()).random().toFloat()
        return Offset(xRandom, yRandom)
    }
}