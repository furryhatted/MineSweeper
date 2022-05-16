package com.github.furryhatted

import com.github.furryhatted.TileEvent.Companion.MINE_MARKED
import com.github.furryhatted.TileEvent.Companion.MINE_OPENED
import com.github.furryhatted.TileEvent.Companion.MINE_UNMARKED
import com.github.furryhatted.TileEvent.Companion.TILE_MARKED
import com.github.furryhatted.TileEvent.Companion.TILE_OPENED
import com.github.furryhatted.TileEvent.Companion.TILE_UNMARKED
import javafx.event.EventHandler
import javafx.scene.layout.VBox
import org.slf4j.LoggerFactory

class RootPane(
    c: Int,
    r: Int,
    m: Int,
    eventHandler: EventHandler<GameEvent>
) : EventHandler<InterfaceEvent>, VBox() {
    private val fieldPane = FieldPane(c, r, m)
    private val topPane = TopPane(c * r, m)
    internal var score: Int = 0
        private set(value) {
            field = value
            logger.debug("Score set: $field")
        }


    init {
        this.stylesheets.add("default.css")
        this.styleClass.add("scene")
        this.addEventHandler(GameEvent.ANY, eventHandler)
        fieldPane.addEventHandler(InterfaceEvent.ANY, this)
        children.add(topPane)
        children.add(fieldPane)
        topPane.startTimer()
    }

    private fun finishGame(state: GameState) {
        topPane.stopTimer()
        when (state) {
            GameState.WIN -> fireEvent(GameEvent(GameEvent.GAME_WON))
            GameState.LOSS -> fireEvent(GameEvent(GameEvent.GAME_LOST))
        }
    }

    override fun handle(event: InterfaceEvent) {
        logger.debug("Received ${event.eventType} from ${event.source}")
        when (event.eventType) {
            MINE_MARKED -> {
                score += 10; topPane.tilesLeft--; topPane.minesLeft--
            }
            MINE_UNMARKED -> {
                score -= 10; topPane.tilesLeft++; topPane.minesLeft++
            }
            TILE_MARKED -> {
                score -= 10; topPane.tilesLeft--; topPane.minesLeft--
            }
            TILE_UNMARKED -> {
                score += 10; topPane.tilesLeft++; topPane.minesLeft++
            }

            TILE_OPENED -> {
                topPane.tilesLeft--; score++
            }

            MINE_OPENED -> {
                score /= 2; topPane.tilesLeft--; topPane.minesLeft--; finishGame(GameState.LOSS); return
            }
        }
        if (fieldPane.isFinished) finishGame(GameState.WIN)
    }


    companion object {
        private val logger = LoggerFactory.getLogger(RootPane::class.java)
    }
}