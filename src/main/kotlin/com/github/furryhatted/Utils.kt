package com.github.furryhatted

import javafx.scene.effect.Bloom
import javafx.scene.image.ImageView
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import kotlin.math.pow

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
    MediaPlayer(Media(MineSweeper::class.java.getResource("/sounds/boom.wav")?.toExternalForm())).apply {
        rate = 2.5
        volume = 0.6
        setOnEndOfMedia { recycleMedia(this) }
    }

val beep: MediaPlayer =
    MediaPlayer(Media(MineSweeper::class.java.getResource("/sounds/beep.wav")?.toExternalForm())).apply {
        rate = 1.0
        volume = 1.0
        setOnEndOfMedia { recycleMedia(this) }
    }

val tick: MediaPlayer =
    MediaPlayer(Media(MineSweeper::class.java.getResource("/sounds/tick.wav")?.toExternalForm())).apply {
        rate = 1.0
        volume = 1.0
        setOnEndOfMedia { recycleMedia(this) }
    }

val click: MediaPlayer =
    MediaPlayer(Media(MineSweeper::class.java.getResource("/sounds/click.wav")?.toExternalForm())).apply {
        rate = 1.0
        volume = 1.0
        setOnEndOfMedia { recycleMedia(this) }
    }

val explosion: ImageView
    get() =
        ImageView(MineSweeper::class.java.getResource("/images/boom.gif")?.toExternalForm())
            .apply {
                isSmooth = true
                isCache = true
            }

fun Int.pow(value: Int) = this.toDouble().pow(value)