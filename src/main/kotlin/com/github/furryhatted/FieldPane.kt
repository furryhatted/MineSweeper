package com.github.furryhatted

import com.github.furryhatted.TileEvent.Companion.CHEAT_OPENED
import com.github.furryhatted.TileEvent.Companion.MINE_OPENED
import com.github.furryhatted.TileEvent.Companion.TILE_OPENED
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.layout.Pane
import javafx.util.Duration
import org.slf4j.LoggerFactory
import kotlin.math.pow

class FieldPane(
    private val columns: Int = DEFAULT_FIELD_WIDTH,
    private val rows: Int = DEFAULT_FIELD_HEIGHT,
    private val mines: Int = DEFAULT_MINE_COUNT
) : EventHandler<TileEvent>, Pane() {
    private val tileMatrix: Map<Pair<Int, Int>, Tile>

    internal val isFinished: Boolean
        get() = this.tileMatrix.values.all { it.isDisable || it.isMarked }

    private fun getPosition(tile: Tile) = tileMatrix.entries.first { it.value == tile }.key

    private fun adjacentTiles(center: Tile): List<Tile> {
        val pos = getPosition(center)
        return tileMatrix.entries
            .filter {
                (it.key != pos) &&
                        (it.key.first in pos.first - 1..pos.first + 1)
                        && (it.key.second in pos.second - 1..pos.second + 1)
            }
            .map { it.value }
    }

    private fun openGroup(group: List<Tile>) {
        group.forEach {
            fireEvent(TileEvent(TILE_OPENED))
            it.doOpen(true)
        }
    }

    private fun open(center: Tile) {
        val (x0, y0) = getPosition(center)
        val distance = { t1: Tile ->
            val (x1, y1) = getPosition(t1)
            val d1 = ((x0 - x1).toDouble().pow(2) + (y0 - y1).toDouble().pow(2)).pow(-2)
            d1
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
        val animation = Timeline(KeyFrame(Duration(7.0), { if (iterator.hasNext()) openGroup(iterator.next()) }))
        animation.cycleCount = openRoute.size
        animation.play()
    }

    private fun buildOpenSequence(center: Tile, tileList: ArrayList<Tile> = ArrayList()): ArrayList<Tile> {
        if (tileList.contains(center)) return tileList
        if (logger.isTraceEnabled) logger.trace("Building sequence: $center")
        tileList.add(center)
        if (center.tooltip != 0) return tileList
        adjacentTiles(center)
            .filter { it.canOpen }
            .forEach { buildOpenSequence(it, tileList) }
        return tileList
    }

    init {
        this.styleClass.add("field")
        this.tileMatrix = (0 until rows).map { r -> (0 until columns).map { c -> r to c } }.flatten().associateWith {
            Tile(it.first to it.second).apply {
                this.layoutX = it.second * Tile.DEFAULT_SIZE
                this.layoutY = it.first * Tile.DEFAULT_SIZE
            }
        }
        this.children.addAll(this.tileMatrix.values)
        this.tileMatrix.values.forEach { it.addEventHandler(TileEvent.ANY, this) }
        this.tileMatrix.values.shuffled().take(mines).forEach { it.tooltip = -1 }
        this.tileMatrix.values.filter { !it.isMined }.forEach { t -> t.tooltip = adjacentTiles(t).count { it.isMined } }
        if (logger.isDebugEnabled) logger.debug("Created $this")
    }

    override fun toString(): String =
        "${javaClass.simpleName}[columns=$columns; rows=$rows; mines=$mines] $tileMatrix"

    override fun handle(event: TileEvent) {
        if (logger.isDebugEnabled) logger.debug("Received ${event.eventType} from ${event.source}")
        when (event.eventType) {
            TILE_OPENED -> open(event.source as Tile)
            MINE_OPENED -> shake(this, 150.0, 5.0)
            CHEAT_OPENED -> tileMatrix.values.forEach { it.doOpen(false) }
        }
    }

    companion object {
        private const val DEFAULT_FIELD_WIDTH: Int = 10
        private const val DEFAULT_FIELD_HEIGHT: Int = 10
        private const val DEFAULT_MINE_COUNT: Int = 10
        private val logger = LoggerFactory.getLogger(FieldPane::class.java)
    }
}