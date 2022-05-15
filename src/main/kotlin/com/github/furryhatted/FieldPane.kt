package com.github.furryhatted

import com.github.furryhatted.GameEvent.Companion.GAME_LOST
import com.github.furryhatted.GameEvent.Companion.GAME_WON
import com.github.furryhatted.InterfaceEvent.Companion.STOP_TIMER
import com.github.furryhatted.InterfaceEvent.Companion.TILE_MARKED
import com.github.furryhatted.InterfaceEvent.Companion.TILE_OPENED
import com.github.furryhatted.InterfaceEvent.Companion.TILE_UNMARKED
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import org.slf4j.LoggerFactory

class FieldPane(
    private val columns: Int = DEFAULT_FIELD_WIDTH,
    private val rows: Int = DEFAULT_FIELD_HEIGHT,
    private val mines: Int = DEFAULT_MINE_COUNT
) : EventHandler<MouseEvent>, Pane() {
    private val neighbours: List<List<Int>> =
        (0 until columns * rows).map { adjacentTiles(it, columns, columns * rows) }
    internal val tiles
        get() = this.children.map { it as Tile }

    internal var score: Int = 0
        private set(value) {
            field = value
            logger.debug("Score set: $field")
        }

    private fun minesNear(center: Tile): Int =
        neighbours[center.id].map { this.tiles[it].tooltip }.count { it == -1 }
            .apply { logger.trace("minesNear() is $this for $center") }

    internal fun open(tile: Tile) {
        if (!tile.doOpen()) return
        if (tile.isMined) score /= 2 else score++
        this.fireEvent(InterfaceEvent(TILE_OPENED))
        if (tile.tooltip != 0) return
        neighbours[tile.id]
            .map { this.tiles[it] }
            .filter { !(it.isMarked && it.isDisabled) }
            .forEach { open(it) }
    }

    internal fun mark(tile: Tile) {
        if (tile.doMark()) score += 10 else score -= 10
        if (tile.isMarked) this.fireEvent(InterfaceEvent(TILE_MARKED))
        else this.fireEvent(InterfaceEvent(TILE_UNMARKED))
    }

    init {
        this.styleClass.add("field")
        (0 until columns * rows).map {
            Tile(it).apply {
                layoutX = (it % columns) * Tile.DEFAULT_SIZE
                layoutY = (it / columns) * Tile.DEFAULT_SIZE
            }
        }.forEach { this.children.add(it) }
        this.tiles.forEach { it.onMouseClicked = this }
        this.tiles.shuffled().take(mines).forEach { it.tooltip = -1 }
        this.tiles.filter { !it.isMined }.forEach { it.tooltip = minesNear(it) }
        this.autosize()
        logger.debug("Created $this")
    }


    override fun handle(event: MouseEvent) {
        val tile = event.source as Tile
        when (event.button) {
            MouseButton.PRIMARY -> {
                this.open(tile)
                if (!tile.isMarked && tile.isMined) {
                    logger.debug("Sending event to ${this.parent}")
                    this.fireEvent(InterfaceEvent(STOP_TIMER))
                    this.fireEvent(GameEvent(GAME_LOST))
                }
            }
            MouseButton.SECONDARY -> this.mark(tile)
            else -> Alert(Alert.AlertType.CONFIRMATION, "Dude! Da fuk ur doing?!").showAndWait()
        }
        if (this.tiles.all { it.isMarked || it.isDisabled }) {
            this.fireEvent(InterfaceEvent(STOP_TIMER))
            this.fireEvent(GameEvent(GAME_WON))
        }
    }


    override fun toString(): String =
        "${javaClass.simpleName}[columns=$columns; rows=$rows; mines=$mines]"

    companion object {
        private const val DEFAULT_FIELD_WIDTH: Int = 10
        private const val DEFAULT_FIELD_HEIGHT: Int = 10
        private const val DEFAULT_MINE_COUNT: Int = 10
        private val logger = LoggerFactory.getLogger(FieldPane::class.java)
    }
}