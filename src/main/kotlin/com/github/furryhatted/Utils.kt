package com.github.furryhatted

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import java.io.File

const val BOMB: String = "\uD83D\uDCA3"
const val FLAG: String = "\uD83D\uDEA9"
const val SKULL: String = "\uD83D\uDD71"
const val FIELD: String = "\uD83D\uDF96"
const val STOPWATCH: String = "\u23F1"
const val WATCHES: String = "\u231A"
const val ALARMCLOCK: String = "\u23F0"
const val TIMER: String = "\u23F2"


enum class GameState {
    LOSS,
    WIN
}


private val recycleMedia = { media: MediaPlayer -> media.stop() }

val PALERED = Color.valueOf("#833")

val boom: MediaPlayer =
    MediaPlayer(Media(File("src/main/resources/boom.wav").toURI().toString())).apply {
        rate = 2.5
        volume = 0.6
        setOnEndOfMedia { recycleMedia(this) }
    }

val beep: MediaPlayer =
    MediaPlayer(Media(File("src/main/resources/beep.wav").toURI().toString())).apply {
        rate = 1.0
        volume = 1.0
        setOnEndOfMedia { recycleMedia(this) }
    }

val tick: MediaPlayer =
    MediaPlayer(Media(File("src/main/resources/tick.wav").toURI().toString())).apply {
        rate = 1.0
        volume = 1.0
        setOnEndOfMedia { recycleMedia(this) }
    }

val click: MediaPlayer =
    MediaPlayer(Media(File("src/main/resources/click.wav").toURI().toString())).apply {
        rate = 1.0
        volume = 1.0
        setOnEndOfMedia { recycleMedia(this) }
    }
