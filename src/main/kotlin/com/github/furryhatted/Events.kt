package com.github.furryhatted

import javafx.beans.NamedArg
import javafx.event.Event
import javafx.event.EventTarget
import javafx.event.EventType

class GameEvent : Event {
    constructor(eventType: EventType<out GameEvent>) : super(eventType)

    constructor(
        @NamedArg("source") source: Any,
        @NamedArg("target") target: EventTarget,
        @NamedArg("eventType") eventType: EventType<out GameEvent>
    ) : super(source, target, eventType)

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


open class InterfaceEvent : Event {
    constructor(eventType: EventType<out InterfaceEvent>) : super(eventType)
    constructor(
        @NamedArg("source") source: Any,
        @NamedArg("target") target: EventTarget,
        @NamedArg("eventType") eventType: EventType<out InterfaceEvent>
    ) : super(source, target, eventType)

    @Suppress("UNCHECKED_CAST")
    override fun getEventType(): EventType<out InterfaceEvent> {
        return super.getEventType() as EventType<out InterfaceEvent>
    }

    companion object {
        private const val serialVersionUID = 202205151L

        val ANY = EventType<InterfaceEvent>(Event.ANY, "INTERFACE")

    }
}

class TileEvent : InterfaceEvent {
    constructor(eventType: EventType<out TileEvent>) : super(eventType)
    constructor(
        @NamedArg("source") source: Tile,
        @NamedArg("target") target: EventTarget,
        @NamedArg("eventType") eventType: EventType<out TileEvent>
    ) : super(source, target, eventType)

    @Suppress("UNCHECKED_CAST")
    override fun getEventType(): EventType<out TileEvent> {
        return super.getEventType() as EventType<out TileEvent>
    }

    companion object {
        private const val serialVersionUID = 202205153L

        val ANY = EventType<TileEvent>(InterfaceEvent.ANY, "TILE_EVENT")

        val TILE_OPENED = EventType(ANY, "TILE_OPENED")

        val TILE_MARKED = EventType(ANY, "TILE_MARKED")

        val TILE_UNMARKED = EventType(ANY, "TILE_UNMARKED")

        val MINE_OPENED = EventType(ANY, "MINE_OPENED")

        val MINE_MARKED = EventType(ANY, "MINE_MARKED")

        val MINE_UNMARKED = EventType(ANY, "MINE_UNMARKED")


    }
}