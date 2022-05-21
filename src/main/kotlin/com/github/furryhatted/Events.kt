package com.github.furryhatted

import javafx.beans.NamedArg
import javafx.event.Event
import javafx.event.EventTarget
import javafx.event.EventType

class GameEvent(eventType: EventType<out GameEvent>) : Event(eventType) {

    @Suppress("UNCHECKED_CAST")
    override fun getEventType(): EventType<out GameEvent> {
        return super.getEventType() as EventType<out GameEvent>
    }

    companion object {
        private const val serialVersionUID = 202205152L

        val ANY = EventType<GameEvent>(Event.ANY, "GAME")

        val GAME_WON = EventType(ANY, "GAME_WON")

        val GAME_LOST = EventType(ANY, "GAME_LOST")
    }
}


open class InterfaceEvent(
    @NamedArg("source") source: Any?,
    @NamedArg("target") target: EventTarget?,
    @NamedArg("eventType") eventType: EventType<out InterfaceEvent>
) : Event(source, target, eventType) {
    @Suppress("UNCHECKED_CAST")
    override fun getEventType(): EventType<out InterfaceEvent> {
        return super.getEventType() as EventType<out InterfaceEvent>
    }

    override fun toString(): String =
        "${this.javaClass.simpleName}@${this.hashCode()}[eventType=${this.eventType}, source=${this.source}, target=${this.target}]"

    companion object {
        private const val serialVersionUID = 202205151L

        val ANY = EventType<InterfaceEvent>(Event.ANY, "INTERFACE_EVENT")

    }
}

class TileEvent(
    @NamedArg("source") source: Any?,
    @NamedArg("target") target: EventTarget?,
    @NamedArg("eventType") eventType: EventType<out TileEvent>,
    @NamedArg("isMarked") val isMarked: Boolean,
    @NamedArg("isMarked") val isMined: Boolean
) : InterfaceEvent(source, target, eventType) {
    constructor(
        @NamedArg("eventType") eventType: EventType<out TileEvent>,
        tile: Tile
    ) : this(
        null,
        null,
        eventType,
        tile.isMarked,
        tile.isMined
    )

    @Suppress("UNCHECKED_CAST")
    override fun getEventType(): EventType<out TileEvent> {
        return super.getEventType() as EventType<out TileEvent>
    }

    override fun toString(): String =
        StringBuilder("TileEvent[").apply {
            this.append("source=$source")
            this.append(", target=$target")
            this.append(", eventType=$eventType")
            this.append(", consumed=$isConsumed")
            if (isMarked) this.append(", isMarked")
            if (isMined) this.append(", isMined")
        }.append("]").toString()

    companion object {
        private const val serialVersionUID = 202205153L

        val ANY = EventType<TileEvent>(InterfaceEvent.ANY, "TILE_EVENT")

        val TILE_CHEAT = EventType(ANY, "TILE_CHEAT")

        val TILE_OPENED = EventType(ANY, "TILE_OPENED")

        val TILE_MARKED = EventType(ANY, "TILE_MARKED")

        val TILE_UNMARKED = EventType(ANY, "TILE_UNMARKED")
    }
}