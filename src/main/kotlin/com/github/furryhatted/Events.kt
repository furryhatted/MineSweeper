package com.github.furryhatted

import javafx.event.Event
import javafx.event.EventType

val GAME_WON = EventType<GameWonEvent>("GAME_WON")
val GAME_LOST = EventType<GameLostEvent>("GAME_LOST")

data class GameLostEvent(val source: Field) : Event(GAME_LOST)
data class GameWonEvent(val source: Field) : Event(GAME_WON)
