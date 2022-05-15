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


class InterfaceEvent : Event {
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

        val START_TIMER = EventType(ANY, "START_TIMER")

        val STOP_TIMER = EventType(ANY, "STOP_TIMER")

        val TILE_OPENED = EventType(ANY, "TILE_OPENED")

        val TILE_MARKED = EventType(ANY, "TILE_MARKED")

        val TILE_UNMARKED = EventType(ANY, "TILE_UNMARKED")

    }
}