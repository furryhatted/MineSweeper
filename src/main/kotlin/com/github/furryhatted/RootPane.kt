package com.github.furryhatted

import com.github.furryhatted.InterfaceEvent.Companion.START_TIMER
import com.github.furryhatted.InterfaceEvent.Companion.STOP_TIMER
import com.github.furryhatted.InterfaceEvent.Companion.TILE_MARKED
import com.github.furryhatted.InterfaceEvent.Companion.TILE_OPENED
import com.github.furryhatted.InterfaceEvent.Companion.TILE_UNMARKED
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


    init {
        this.stylesheets.add("default.css")
        this.styleClass.add("scene")
        fieldPane.addEventHandler(InterfaceEvent.ANY, this)
        fieldPane.addEventHandler(GameEvent.ANY, eventHandler)
        children.add(topPane)
        children.add(fieldPane)
    }

    override fun handle(event: InterfaceEvent) {
        logger.debug("Received ${event.eventType} from ${event.source}")
        when (event.eventType) {
            STOP_TIMER -> topPane.timeline.stop()

            START_TIMER -> topPane.timeline.play()

            TILE_OPENED -> topPane.tilesLeft--

            TILE_MARKED -> {
                topPane.tilesLeft--; topPane.minesLeft--
            }

            TILE_UNMARKED -> {
                topPane.tilesLeft++; topPane.minesLeft++
            }

        }
    }


    companion object {
        private val logger = LoggerFactory.getLogger(RootPane::class.java)
    }
}