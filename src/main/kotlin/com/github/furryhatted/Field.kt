package com.github.furryhatted

import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.input.MouseButton
import javafx.scene.layout.FlowPane
import org.slf4j.LoggerFactory

class Field(
    private val width: Int = DEFAULT_FIELD_WIDTH,
    private val height: Int = DEFAULT_FIELD_HEIGHT,
    private val mines: Int = DEFAULT_MINE_COUNT
) : FlowPane() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val neighbours: List<List<Int>> = (0 until width * height).map { adjacentTiles(it, width, width * height) }

    private fun setMines() =
        this.children.shuffled().take(mines).forEach { (it as Tile).tooltip = -1 }

    private fun setTooltips() {
        this.children.map { it as Tile }
            .filter { !it.isMined }
            .forEach { t ->
                t.tooltip =
                    neighbours[t.id].count { (this.children[it] as Tile).isMined }
            }
    }

    private fun cascadeOpen(tile: Tile) {
        if (tile.isDisabled) return
        tile.open()
        if (tile.tooltip == 0) neighbours[tile.id].forEach { cascadeOpen(this.children[it] as Tile) }
    }

    init {
        this.stylesheets.add("default.css")
        this.styleClass.add("field")
        this.children.addAll((0 until width * height).map { Tile(it) })
        this.children.map { it as Tile }.forEach { tile ->
            tile.onMouseClicked = EventHandler { event ->
                when (event.button) {
                    MouseButton.PRIMARY -> cascadeOpen(tile)
                    MouseButton.SECONDARY -> tile.mark()
                    else -> Alert(Alert.AlertType.CONFIRMATION, "Dude! Da fuk ur doing?!").showAndWait()
                }
            }
        }

        this.setMines()
        this.setTooltips()
        this.setPrefSize(Tile.DEFAULT_SIZE * (width + 1), Tile.DEFAULT_SIZE * (height + 1))

    }


    companion object {
        private const val DEFAULT_FIELD_WIDTH: Int = 10
        private const val DEFAULT_FIELD_HEIGHT: Int = 10
        private const val DEFAULT_MINE_COUNT: Int = 10
    }
}