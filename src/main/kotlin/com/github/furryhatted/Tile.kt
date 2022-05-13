package com.github.furryhatted

import javafx.scene.control.ToggleButton
import javafx.scene.paint.Color
import org.slf4j.LoggerFactory

class Tile(
    val id: Int,
    side: Double = DEFAULT_SIZE,
) : ToggleButton() {

    internal var isMarked = false
        private set
    internal var tooltip = 0
    internal val isMined
        get() = this.tooltip == -1

    private val label: String?
        get() = when {
            isMarked -> "\uD83D\uDEA9"
            isMined -> "\uD83D\uDCA3"
            tooltip in 1..8 -> "$tooltip"
            else -> null
        }

    internal fun doOpen(): Boolean {
        logger.debug("doOpen() invoked for $this")
        if (isDisabled) return false
        if (isMarked) return false
        this.text = label
        when (tooltip) {
            -1 -> style = "-fx-background-color: #803333; -fx-text-fill: #cccccc;"
            in 1..8 -> textFill = Color.hsb(240.0 - tooltip * 30, 0.6, 0.8)
        }
        this.isDisable = true
        return true
    }


    internal fun doMark(): Boolean {
        logger.debug("doMark() invoked for $this")
        this.isMarked = !this.isMarked
        this.text = if (isMarked) label else null
        return isMarked == isMined
    }


    init {
        this.setPrefSize(side, side)
        this.styleClass.add("tile")
        this.isSelected = false
    }

    override fun toString(): String =
        "${this.javaClass.simpleName}[isMarked=$isMarked; isMined=$isMined; tooltip=$tooltip]"

    companion object {
        const val DEFAULT_SIZE: Double = 50.0
        private val logger = LoggerFactory.getLogger(Tile::class.java)
    }
}