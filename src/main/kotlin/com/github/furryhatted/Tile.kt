package com.github.furryhatted

import javafx.scene.control.ToggleButton
import javafx.scene.paint.Color

class Tile(
    val id: Int,
    side: Double = DEFAULT_SIZE,
) : ToggleButton() {
    private var isMarked = false
    internal var tooltip = 0
    internal val isMined
        get() = this.tooltip == -1

    internal fun open() {
        if (isMarked) return
        when (tooltip) {
            -1 -> {
                text = "\uD83D\uDCA3"
                textFill = Color.LIGHTGRAY
            }
            in 1..8 -> {
                text = "$tooltip"
                textFill = Color.hsb(240.0 - tooltip * 30, 0.6, 0.8)
            }
        }
        this.isDisable = true
    }

    internal fun mark() {
        this.isMarked = !this.isMarked
        this.text = if (isMarked) "\uD83D\uDEA9" else null
    }


    init {
        this.setPrefSize(side, side)
        this.styleClass.add("tile")
        this.isSelected = false
    }


    companion object {
        const val DEFAULT_SIZE: Double = 50.0
    }
}