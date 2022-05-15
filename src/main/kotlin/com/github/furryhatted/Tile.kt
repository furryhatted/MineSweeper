package com.github.furryhatted

import javafx.scene.control.Button
import javafx.scene.paint.Color
import org.slf4j.LoggerFactory

class Tile(
    val id: Int,
    side: Double = DEFAULT_SIZE,
) : Button() {

    internal var isMarked = false
        private set
    internal var tooltip = 0
    internal val isMined
        get() = this.tooltip == -1

    internal fun doOpen(): Boolean {
        logger.trace("doOpen() invoked for $this")
        if (isMarked || isDisabled) return false
        this.isDisable = true
        when (tooltip) {
            -1 -> {
                style = "-fx-background-color: #803333; -fx-text-fill: #eee;"
                text = BOMB
            }
            in 1..8 -> {
                textFill = Color.hsb(240.0 - tooltip * 30, 0.6, 0.95)
                text = "$tooltip"
            }
            else -> text = null
        }
        return true
    }

    internal fun doMark(): Boolean {
        logger.trace("doMark() invoked for $this")
        this.isMarked = !this.isMarked
        this.text = if (isMarked) FLAG else null
        textFill = Color.valueOf("#803333")
        return isMarked == isMined
    }

    init {
        this.setPrefSize(side, side)
        this.styleClass.add("tile")
    }

    override fun toString(): String =
        "${javaClass.simpleName}#$id[isMarked=$isMarked; isMined=$isMined; tooltip=$tooltip]"

    companion object {
        const val DEFAULT_SIZE: Double = 50.0
        private val logger = LoggerFactory.getLogger(Tile::class.java)
    }
}