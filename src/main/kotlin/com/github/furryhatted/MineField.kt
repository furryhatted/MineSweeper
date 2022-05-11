package com.github.furryhatted

import javafx.scene.layout.FlowPane
import org.slf4j.LoggerFactory

class MineField(
    width: Int = DEFAULT_FIELD_WIDTH,
    height: Int = DEFAULT_FIELD_HEIGHT
) : FlowPane() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val tiles: Array<Array<Tile>> = Array(height) { Array(width) { Tile() } }

    init {
        this.setPrefSize(DEFAULT_TILE_SIZE * width, DEFAULT_TILE_SIZE * height)
        tiles.forEach { row -> row.forEach { tile -> this.children.add(tile); logger.debug("Added tile: $tile") } }
    }


    companion object {
        private const val DEFAULT_TILE_SIZE: Double = 20.0
        private const val DEFAULT_FIELD_WIDTH: Int = 10
        private const val DEFAULT_FIELD_HEIGHT: Int = 10
    }
}