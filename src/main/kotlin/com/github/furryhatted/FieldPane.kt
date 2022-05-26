package com.github.furryhatted

import com.github.furryhatted.TileEvent.Companion.TILE_CHEAT
import com.github.furryhatted.TileEvent.Companion.TILE_OPENED
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.layout.GridPane
import javafx.util.Duration
import org.slf4j.LoggerFactory
import java.awt.Toolkit
import kotlin.math.pow

class FieldPane(
    private val columns: Int = DEFAULT_FIELD_WIDTH,
    private val rows: Int = DEFAULT_FIELD_HEIGHT,
    private val mines: Int = DEFAULT_MINE_COUNT
) : EventHandler<TileEvent>, GridPane() {
    private val tileMatrix: Map<Pair<Int, Int>, Tile>

    internal val isFinished: Boolean
        get() = this.tileMatrix.values.all { it.isDisable || it.isMarked }

    private fun getPosition(tile: Tile) = tileMatrix.entries.first { it.value == tile }.key

    private fun adjacentTiles(center: Tile): List<Tile> {
        val pos = getPosition(center)
        return tileMatrix.entries
            .asSequence()
            .filter { it.key.first in pos.first - 1..pos.first + 1 }
            .filter { it.key.second in pos.second - 1..pos.second + 1 }
            .filter { it.key != pos }
            .map { it.value }
            .toList()
    }

    private fun open(center: Tile) {
        val (x0, y0) = getPosition(center)
        val distance = { t1: Tile ->
            val (x1, y1) = getPosition(t1)
            ((x0 - x1).pow(2) + (y0 - y1).pow(2)).pow(-2)
        }
        val openRoute =
            buildOpenSequence(center)
                .asSequence()
                .minus(center)
                .groupBy(distance)
                .entries
                .sortedByDescending { it.key }
                .map { it.value }
        val iterator = openRoute.iterator()
        val animation = Timeline(KeyFrame(Duration(7.0), {
            if (iterator.hasNext()) iterator.next().forEach {
                fireEvent(TileEvent(TILE_OPENED, it))
                it.doOpen(true)
            }
        }))
        animation.cycleCount = openRoute.size
        animation.play()
    }

    private fun buildOpenSequence(center: Tile, tileList: ArrayList<Tile> = ArrayList()): ArrayList<Tile> {
        if (tileList.contains(center)) return tileList
        if (logger.isTraceEnabled) logger.trace("Building sequence for $center")
        tileList.add(center)
        if (center.tooltip != 0) return tileList
        adjacentTiles(center)
            .filter { it.canOpen }
            .forEach { buildOpenSequence(it, tileList) }
        return tileList
    }

    private fun reveal(tiles: Collection<Tile> = tileMatrix.values) {
        tiles.forEach { it.showTooltip() }
    }

    init {
        this.id = "field"
        this.tileMatrix = (0 until rows * columns)
            .map { if (it < mines) Tile(true) else Tile(false) }
            .shuffled()
            .mapIndexed { index, tile ->
                tile.addEventHandler(TileEvent.ANY, this)

                tile.setPrefSize(DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE)
                tile.setMaxSize(DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE)
                tile.setMinSize(DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE)
                val y = index / rows
                val x = index % rows
                this.add(tile, y, x, 1, 1)
                (y to x) to tile
            }.toMap()
        this.tileMatrix.values.filter { !it.isMined }.forEach { t -> t.tooltip = adjacentTiles(t).count { it.isMined } }
        if (logger.isDebugEnabled) logger.debug("Created $this {${this.tileMatrix}}")
    }

    override fun toString(): String =
        StringBuilder("FieldPane@${hashCode()}[").apply {
            this.append("columns=$columns")
            this.append(", rows=$rows")
            this.append(", mines=$mines")
            if (isFinished) this.append(", isFinished")
        }.append("]").toString()


    override fun handle(event: TileEvent) {
        if (logger.isDebugEnabled) logger.debug("Received $event")
        when (event.eventType) {
            TILE_OPENED ->
                if (event.isMined) shake(this, 15.0, 15, 700.0)
                else open(event.source as Tile)
            TILE_CHEAT -> reveal()
        }
    }


    companion object {
        private const val DEFAULT_TILE_SIZE: Double = 49.0
        private const val DEFAULT_FIELD_WIDTH: Int = 10
        private const val DEFAULT_FIELD_HEIGHT: Int = 10
        private const val DEFAULT_MINE_COUNT: Int = 10
        private val logger = LoggerFactory.getLogger(FieldPane::class.java)
    }
}