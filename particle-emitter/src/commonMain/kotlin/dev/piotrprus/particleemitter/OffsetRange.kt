package dev.piotrprus.particleemitter

import androidx.compose.ui.geometry.Offset

class OffsetRange(
    private val startOffset: Offset,
    private val endOffset: Offset
) : Iterable<Pair<Float, Float>> {

    override fun iterator(): Iterator<Pair<Float, Float>> {
        return OffsetIterator()
    }

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

    fun random(): Offset {
        val xRandom = (startOffset.x.toInt()..endOffset.x.toInt()).random().toFloat()
        val yRandom = (startOffset.y.toInt()..endOffset.y.toInt()).random().toFloat()
        return Offset(xRandom, yRandom)
    }
}