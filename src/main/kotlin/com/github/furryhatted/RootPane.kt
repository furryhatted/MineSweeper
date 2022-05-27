package com.github.furryhatted

import com.github.furryhatted.TileEvent.Companion.TILE_MARKED
import com.github.furryhatted.TileEvent.Companion.TILE_OPENED
import com.github.furryhatted.TileEvent.Companion.TILE_UNMARKED
import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.layout.VBox
import javafx.util.Duration
import org.slf4j.LoggerFactory


class RootPane(
    c: Int,
    r: Int,
    m: Int
) : EventHandler<TileEvent>, VBox() {

    private var duration: Int = 0
    private var movesLeft: Int = c * r
    private var minesLeft: Int = m

//    private val bar: MenuBar
    private val heading: HeadingPane
    private val field: FieldPane
    private val timer: Timeline


    internal var score: Int = 0
        private set(value) {
            field = value
            if (logger.isDebugEnabled) logger.debug("Score set: $field")
        }


    init {
        this.id = "root"
        this.isCache = true
/*        val exit = MenuItem("Exit").apply {
            setOnAction { Platform.exit() }
        }
        val menu = Menu("Game").apply {
            items.add(exit)
        }

        this.bar = MenuBar(menu)*/
        this.field = FieldPane(c, r, m)
        this.heading = HeadingPane(c * r, m, 0)
        this.timer = Timeline(KeyFrame(Duration(1000.0), { heading.setTime(++duration); tick.play() })).also {
            it.cycleCount = Animation.INDEFINITE
        }

        field.addEventHandler(TileEvent.ANY, this)
//        children.add(bar)
        children.add(heading)
        children.add(field)
        timer.play()
    }

    private fun finishGame(state: GameState) {
        timer.stop()
        when (state) {
            GameState.WIN -> fireEvent(GameEvent(GameEvent.GAME_WON))
            GameState.LOSS -> fireEvent(GameEvent(GameEvent.GAME_LOST))
        }
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
                    finishGame(GameState.LOSS)
                    return
                }
            }
        }
        if (field.isFinished) finishGame(GameState.WIN)
    }

    override fun toString(): String = "${this.javaClass.simpleName}@${this.hashCode()}"

    companion object {
        private val logger = LoggerFactory.getLogger(RootPane::class.java)
    }
}