package com.github.furryhatted

import com.github.furryhatted.TileEvent.Companion.CHEAT_OPENED
import com.github.furryhatted.TileEvent.Companion.MINE_MARKED
import com.github.furryhatted.TileEvent.Companion.MINE_OPENED
import com.github.furryhatted.TileEvent.Companion.MINE_UNMARKED
import com.github.furryhatted.TileEvent.Companion.TILE_MARKED
import com.github.furryhatted.TileEvent.Companion.TILE_OPENED
import com.github.furryhatted.TileEvent.Companion.TILE_UNMARKED
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.CONFIRMATION
import javafx.scene.control.Button
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color.*
import org.slf4j.LoggerFactory

class Tile(
    val id: Int,
    side: Double = DEFAULT_SIZE,
) : Button() {

    internal var isMarked = false
        private set
    internal var tooltip = 0
    internal val isMined
        get() = this.tooltip == -1

    private fun showMineTooltip() {
        style = "-fx-background-color: #803333; -fx-text-fill: #eee;"
        text = BOMB
    }

    private fun showProximityTooltip() {
        //textFill = hsb(240.0 - tooltip * 30, 0.70, 1.0)
        textFill = colors[tooltip]
        text = "$tooltip"
    }

    private fun showMarkedTooltip() {
        text = FLAG
        textFill = colors[0]
    }

    private fun showEmptyTooltip() {
        text = null
        textFill = null
    }

    internal fun doOpen(triggerOpenEvent: Boolean = true) {
        logger.trace("doOpen() invoked for $this")
        if (isMarked || isDisabled) return
        this.isDisable = true
        when (tooltip) {
            -1 -> showMineTooltip()
            in 1..8 -> showProximityTooltip()
            else -> showEmptyTooltip()
        }
        val event = if (isMined) TileEvent(MINE_OPENED) else TileEvent(TILE_OPENED)
        logger.trace("Sending ${event.eventType} for $this")
        if (triggerOpenEvent) fireEvent(event)
    }

    private fun doMark() {
        logger.trace("doMark() invoked for $this")
        isMarked = !isMarked
        if (isMarked) showMarkedTooltip() else showEmptyTooltip()
        val event = when (isMined) {
            true -> if (isMarked) TileEvent(MINE_MARKED) else TileEvent(MINE_UNMARKED)
            false -> if (isMarked) TileEvent(TILE_MARKED) else TileEvent(TILE_UNMARKED)
        }
        fireEvent(event)
    }

    init {
        this.setPrefSize(side, side)
        this.styleClass.add("tile")
        this.onMouseClicked = EventHandler { event ->
            when (event.button) {
                MouseButton.PRIMARY -> doOpen()
                MouseButton.SECONDARY -> doMark()
                MouseButton.MIDDLE ->
                    if (event.isShiftDown && event.isControlDown && event.isAltDown) fireEvent(TileEvent(CHEAT_OPENED))
                else -> Alert(CONFIRMATION, "Dude! Da fuk ur doing?!").showAndWait()
            }
        }

    }

    override fun toString(): String =
        "${javaClass.simpleName}#$id[isMarked=$isMarked; isMined=$isMined; tooltip=$tooltip]"

    companion object {
        const val DEFAULT_SIZE: Double = 49.0
        private val logger = LoggerFactory.getLogger(Tile::class.java)
        private val colors = listOf(
            valueOf("#803333"),
            LIGHTSKYBLUE,
            PALEGREEN,
            GREENYELLOW,
            KHAKI,
            NAVAJOWHITE,
            LIGHTSALMON,
            SALMON,
            TOMATO
        )

    }
}