package com.github.furryhatted

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File

object SoundCache {
    val boom: MediaPlayer =
        MediaPlayer(Media(File("src/main/resources/boom.wav").toURI().toString())).apply {
            rate = 2.5
            volume = 0.2
            setOnEndOfMedia { recycleMedia(this) }
        }
    val beep: MediaPlayer =
        MediaPlayer(Media(File("src/main/resources/beep.wav").toURI().toString())).apply {
            rate = 1.0
            volume = 0.5
            setOnEndOfMedia { recycleMedia(this) }
        }

    val tick: MediaPlayer =
        MediaPlayer(Media(File("src/main/resources/tick.wav").toURI().toString())).apply {
            rate = 1.0
            volume = 0.5
            setOnEndOfMedia { recycleMedia(this) }
        }

    val click: MediaPlayer =
        MediaPlayer(Media(File("src/main/resources/click.wav").toURI().toString())).apply {
            rate = 1.0
            volume = 0.5
            setOnEndOfMedia { recycleMedia(this) }
        }


    private val recycleMedia = { media: MediaPlayer -> media.stop() }
}