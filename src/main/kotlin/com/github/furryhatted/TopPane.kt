package com.github.furryhatted

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.util.Duration
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

class TopPane(
    private val tilesTotal: Int,
    private val minesTotal: Int
) : BorderPane() {
    private val mineLabel: Label = Label()

    private val timeLabel: Label = Label()
    internal val tileLabel: Label = Label()
    private val timeline: Timeline =
        Timeline(KeyFrame(Duration(1000.0), { this.duration++ }))
            .apply { cycleCount = Animation.INDEFINITE }
    private var duration: Long = 0
        set(value) {
            field = value
            timeLabel.text = "$ALARMCLOCK: ${value.seconds}"
            shake(timeLabel, 50.0, 3.0)
        }

    internal var tilesLeft: Int = 0
        set(value) {
            field = value
            tileLabel.text = "$FIELD: $value/$tilesTotal"
        }

    internal var minesLeft: Int = 0
        set(value) {
            field = value
            mineLabel.text = "$BOMB: $value/$minesTotal"
            shake(mineLabel, 50.0, 3.0)
        }

    fun startTimer() = timeline.play()
    fun stopTimer() = timeline.stop()

    init {
        this.styleClass.add("panel")
        this.tilesLeft = tilesTotal
        this.minesLeft = minesTotal
        this.duration = 0
        this.left = tileLabel
        this.center = timeLabel
        this.right = mineLabel
        timeline.play()
    }


    companion object {
        private val logger = LoggerFactory.getLogger(TopPane::class.java)
    }
}