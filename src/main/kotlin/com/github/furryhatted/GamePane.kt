package com.github.furryhatted

import com.github.furryhatted.TileEvent.Companion.TILE_MARKED
import com.github.furryhatted.TileEvent.Companion.TILE_OPENED
import com.github.furryhatted.TileEvent.Companion.TILE_UNMARKED
import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.layout.VBox
import javafx.util.Duration
import org.slf4j.LoggerFactory


class GamePane(val settings: Triple<Int, Int, Int>) : EventHandler<TileEvent>, VBox() {


    private var duration: Int = 0
    private var movesLeft: Int = settings.first * settings.second
    private var minesLeft: Int = settings.third

    private val heading: HeadingPane
    private val field: FieldPane
    private val timer: Timeline

    private var score: Int = 0
        private set(value) {
            field = value
            if (logger.isDebugEnabled) logger.debug("Score set: $field")
        }


    init {
        this.id = "game"
        this.isCache = true
        this.field = FieldPane(settings.first, settings.second, settings.third)
        this.heading = HeadingPane(movesLeft, minesLeft, 0)
        this.timer = Timeline(KeyFrame(Duration(1000.0), { heading.setTime(++duration); tick.play() })).also {
            it.cycleCount = Animation.INDEFINITE
        }

        field.addEventHandler(TileEvent.ANY, this)
        children.add(heading)
        children.add(field)
        timer.play()
    }

    internal fun finish() {
        timer.stop()
        FadeTransition(Duration.seconds(1.2), this).apply {
            fromValue = 1.0
            toValue = .4
            play()
        }
    }

    override fun handle(event: TileEvent) {
        if (logger.isDebugEnabled) logger.debug("Received $event")
        when (event.eventType) {
            TILE_MARKED -> {
                if (event.isMined) score += 10 else score -= 10
                heading.setMines(--minesLeft)
                heading.setMoves(--movesLeft)
            }
            TILE_UNMARKED -> {
                if (event.isMined) score -= 10 else score += 10
                heading.setMines(++minesLeft)
                heading.setMoves(++movesLeft)
            }
            TILE_OPENED -> {
                heading.setMoves(--movesLeft)
                if (!event.isMined) score++
                else {
                    score /= 2
                    heading.setMines(--minesLeft)
                    fireEvent(GameEvent(GameEvent.GAME_LOST, score))
                    return
                }
            }
        }
        if (field.isFinished)
            fireEvent(GameEvent(GameEvent.GAME_WON, score))
    }

    override fun toString(): String = "${this.javaClass.simpleName}@${this.hashCode()}"

    companion object {
        private val logger = LoggerFactory.getLogger(GamePane::class.java)
    }
}