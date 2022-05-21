package com.github.furryhatted

import com.github.furryhatted.TileEvent.Companion.TILE_CHEAT
import com.github.furryhatted.TileEvent.Companion.TILE_MARKED
import com.github.furryhatted.TileEvent.Companion.TILE_OPENED
import com.github.furryhatted.TileEvent.Companion.TILE_UNMARKED
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.CONFIRMATION
import javafx.scene.control.Button
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color.*
import org.slf4j.LoggerFactory

class Tile(
    val isMined: Boolean
) : Button() {
    internal var tooltip = 0
    internal var isMarked = false
        private set

    internal val canOpen
        get() = !(this.isMarked || this.isDisabled)

    init {
        this.id = "tile"
        this.isCache = true
        this.setOnMouseClicked { event ->
            when (event.button) {
                MouseButton.PRIMARY -> doOpen(false)
                MouseButton.SECONDARY -> doMark()
                MouseButton.MIDDLE -> {
                    if (event.isShiftDown && event.isControlDown && event.isAltDown) fireEvent(TileEvent(TILE_CHEAT, this))
                }
                else -> Alert(CONFIRMATION, "Dude! Da fuk ur doing?!").showAndWait()
            }
        }
        this.arm()
        if (logger.isTraceEnabled) logger.trace("Created $this[isMined=$isMined]")
    }

    internal fun showTooltip() {
        textFill = if (isMined || isMarked) colors[9] else colors[tooltip]
        text = when {
            isMarked -> FLAG
            isMined -> BOMB
            else -> "$tooltip"
        }
    }

    internal fun hideTooltip() {
        this.text = null
        textFill = null
    }

    internal fun doOpen(quietly: Boolean) {
        if (logger.isTraceEnabled) logger.trace("doOpen($quietly) invoked for $this")
        if (!canOpen) {
            this.toFront()
            shake(this, 3.0)
            return
        }
        this.isDisable = true
        when {
            isMined -> {
                explode(this).apply {
                    setOnFinished { style = "-fx-background-color: #803333; -fx-text-fill: #eee;" }
                }.play()
                boom.play()
            }
            else -> {
                click.play()
                showTooltip()
            }
        }
        if (quietly) return
        TileEvent(TILE_OPENED, this).let {
            if (logger.isTraceEnabled) logger.trace("Sending $it")
            fireEvent(it)
        }
    }

    private fun doMark() {
        click.play()
        if (logger.isTraceEnabled) logger.trace("doMark() invoked for $this")
        isMarked = !isMarked
        if (isMarked) showTooltip() else hideTooltip()
        val event = when (isMarked) {
            true -> TileEvent(TILE_MARKED, this)
            false -> TileEvent(TILE_UNMARKED, this)
        }
        if (logger.isTraceEnabled) logger.trace("Sending ${event.eventType} for $this")
        fireEvent(event)
    }

    override fun toString(): String =
        StringBuilder("Tile@${hashCode()}[").apply {
            this.append("tooltip=$tooltip")
            if (isMarked) this.append(", isMarked")
            if (isMined) this.append(", isMined")
        }.append("]").toString()

    companion object {
        private val logger = LoggerFactory.getLogger(Tile::class.java)
        private val colors = listOf(
            TRANSPARENT,
            LIGHTSKYBLUE,
            PALEGREEN,
            GREENYELLOW,
            KHAKI,
            NAVAJOWHITE,
            LIGHTSALMON,
            SALMON,
            TOMATO,
            PALERED
        )
    }
}