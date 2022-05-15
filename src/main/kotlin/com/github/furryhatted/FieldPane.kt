package com.github.furryhatted

import com.github.furryhatted.TileEvent.Companion.TILE_OPENED
import javafx.event.EventHandler
import javafx.scene.layout.Pane
import org.slf4j.LoggerFactory

class FieldPane(
    private val columns: Int = DEFAULT_FIELD_WIDTH,
    private val rows: Int = DEFAULT_FIELD_HEIGHT,
    private val mines: Int = DEFAULT_MINE_COUNT
) : EventHandler<TileEvent>, Pane() {
    private val neighbours: List<List<Int>> =
        (0 until columns * rows).map { adjacentTiles(it, columns, columns * rows) }
    private val tiles
        get() = this.children.map { it as Tile }

    internal val isFinished: Boolean
        get() = tiles.all { it.isDisable || it.isMarked }

    private fun minesNear(center: Tile): Int =
        neighbours[center.id].map { this.tiles[it].tooltip }.count { it == -1 }
            .apply { logger.trace("minesNear() is $this for $center") }

    private fun open(tile: Tile) {
        if (tile.tooltip != 0) return
        neighbours[tile.id]
            .map { this.tiles[it] }
            .filter { !(it.isMarked && it.isDisabled) }
            .forEach { it.doOpen() }
    }

    init {
        this.styleClass.add("field")
        (0 until columns * rows).map {
            Tile(it).apply {
                layoutX = (it % columns) * Tile.DEFAULT_SIZE
                layoutY = (it / columns) * Tile.DEFAULT_SIZE
            }
        }.forEach { this.children.add(it) }
        this.tiles.forEach { it.addEventHandler(TileEvent.ANY, this) }
        this.tiles.shuffled().take(mines).forEach { it.tooltip = -1 }
        this.tiles.filter { !it.isMined }.forEach { it.tooltip = minesNear(it) }
        logger.debug("Created $this")
    }

    override fun toString(): String =
        "${javaClass.simpleName}[columns=$columns; rows=$rows; mines=$mines]"

    override fun handle(event: TileEvent) {
        logger.debug("Received ${event.eventType} from ${event.source}")
        when (event.eventType) {
            TILE_OPENED -> open(event.source as Tile)
        }
    }

    companion object {
        private const val DEFAULT_FIELD_WIDTH: Int = 10
        private const val DEFAULT_FIELD_HEIGHT: Int = 10
        private const val DEFAULT_MINE_COUNT: Int = 10
        private val logger = LoggerFactory.getLogger(FieldPane::class.java)
    }
}