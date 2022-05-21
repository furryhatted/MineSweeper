package com.github.furryhatted

import javafx.animation.*
import javafx.scene.Node
import javafx.scene.control.ContentDisplay.GRAPHIC_ONLY
import javafx.scene.control.ContentDisplay.TEXT_ONLY
import javafx.scene.image.ImageView
import javafx.util.Duration
import kotlin.random.Random

fun swingAnimation(maxSwing: Double, duration: Double): TranslateTransition {
    val animation = TranslateTransition(Duration(duration))
    animation.byX = Random.nextDouble(-maxSwing, maxSwing)
    animation.byY = Random.nextDouble(-maxSwing, maxSwing)
    animation.isAutoReverse = true
    animation.cycleCount = 2
    return animation
}

fun shake(node: Node, magnitude: Double, cycles: Int = Random.nextInt(6, 12), duration: Double = 50.0): Animation {
    val animation = SequentialTransition(node)
    repeat(cycles) { animation.children.add(swingAnimation(magnitude, duration / (cycles * 2))) }
    animation.play()
    return animation
}

fun explode(tile: Tile): Animation {
    val f1 = KeyFrame(Duration.seconds(.0), {
        tile.hideTooltip()
        with(ImageView("boom.gif")) {
            this.fitWidth = tile.width
            this.fitHeight = tile.height
            tile.graphic = this
        }
        tile.contentDisplay = GRAPHIC_ONLY
    })
    val f2 = KeyFrame(Duration.seconds(.5), {
        tile.graphic = null
        tile.contentDisplay = TEXT_ONLY
        tile.showTooltip()
    })
    return Timeline(f1, f2)
}