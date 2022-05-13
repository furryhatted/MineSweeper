package com.github.furryhatted

import javafx.scene.layout.Pane
import org.slf4j.LoggerFactory

class Field(
    val columns: Int = DEFAULT_FIELD_WIDTH,
    val rows: Int = DEFAULT_FIELD_HEIGHT,
    val mines: Int = DEFAULT_MINE_COUNT
) : Pane() {
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
            .apply { logger.debug("minesNear() is $this for $center") }

    internal fun open(tile: Tile) {
        if (!tile.doOpen()) return
        if (tile.isMined) score /= 2 else score++
        if (tile.tooltip != 0) return
        neighbours[tile.id]
            .map { this.tiles[it] }
            .filter { !(it.isMarked && it.isDisabled) }
            .forEach { open(it) }
    }

    internal fun mark(tile: Tile) {
        if (tile.doMark()) score += 10 else score -= 10
    }

    init {
        this.styleClass.add("field")
        (0 until columns * rows).map {
            Tile(it).apply {
                layoutX = (it % columns) * Tile.DEFAULT_SIZE
                layoutY = (it / columns) * Tile.DEFAULT_SIZE
            }
        }.forEach { this.children.add(it) }
        this.tiles.forEach { it.onMouseClicked = TileEventHandler }
        this.tiles.shuffled().take(mines).forEach { it.tooltip = -1 }
        this.tiles.filter { !it.isMined }.forEach { it.tooltip = minesNear(it) }
    }


    companion object {
        private const val DEFAULT_FIELD_WIDTH: Int = 10
        private const val DEFAULT_FIELD_HEIGHT: Int = 10
        private const val DEFAULT_MINE_COUNT: Int = 10
        private val logger = LoggerFactory.getLogger(Field::class.java)
    }
}