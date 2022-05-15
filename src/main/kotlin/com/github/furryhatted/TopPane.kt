package com.github.furryhatted

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.util.Duration
import org.slf4j.LoggerFactory

class TopPane(
    val tilesTotal: Int,
    val minesTotal: Int
) : BorderPane() {
    private val mineLabel: Label = Label()
    private val timeLabel: Label = Label()
    private val tileLabel: Label = Label()
    internal val timeline: Timeline = Timeline(KeyFrame(Duration(1000.0), { this.duration++ }))
        .apply { cycleCount = Animation.INDEFINITE }

    internal var duration: Long = 0
        set(value) {
            field = value
            timeLabel.text = "$STOPWATCH: $value"
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
        }

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