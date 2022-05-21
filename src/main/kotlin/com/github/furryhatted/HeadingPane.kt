package com.github.furryhatted

import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import kotlin.time.Duration.Companion.seconds

class HeadingPane(
    private val moves: Int,
    private val mines: Int,
    private val time: Int
) : BorderPane() {
    private val mineLabel = Label()
    private val moveLabel = Label()
    private val timeLabel = Label()


    internal fun setTime(value: Int) {
        timeLabel.text = "$ALARMCLOCK: ${value.seconds}"
        shake(timeLabel, 3.0)
    }

    internal fun setMoves(value: Int) {
        moveLabel.text = "$FIELD: $value/$moves"
        //shake(moveLabel, 3.0)
    }

    internal fun setMines(value: Int) {
        mineLabel.text = "$BOMB: $value/$mines"
        shake(mineLabel, 3.0)
    }

    init {
        this.id = "heading"
        this.left = moveLabel
        this.center = timeLabel
        this.right = mineLabel
        this.setMoves(moves)
        this.setMines(mines)
        this.setTime(time)
    }

}