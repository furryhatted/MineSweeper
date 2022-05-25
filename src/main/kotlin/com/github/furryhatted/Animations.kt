package com.github.furryhatted

import javafx.animation.*
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.control.ContentDisplay.GRAPHIC_ONLY
import javafx.scene.control.ContentDisplay.TEXT_ONLY
import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
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
        with(ImageView(MineSweeper::class.java.getResource("/images/boom.gif")?.toExternalForm())) {
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

fun highlight(tile: Tile): Animation {
    val radius = SimpleObjectProperty(.0)
    val startValue = KeyValue(radius, .0)
    val endValue = KeyValue(radius, 25.0)
    val startFrame = KeyFrame(Duration.ZERO, startValue)
    val endFrame = KeyFrame(Duration.seconds(.5), endValue)
    val timeLine = Timeline(startFrame, endFrame)
    radius.addListener { _, _, value ->
        tile.viewOrder = 0.0
        tile.style = "-fx-background-color: radial-gradient(center 50% 50%, radius 200%, #ccc, #33f);"
        tile.effect = DropShadow(BlurType.THREE_PASS_BOX, Color.valueOf("#33fc"), value, .15, .0, .0)
    }
    timeLine.isAutoReverse = true
    timeLine.cycleCount = Animation.INDEFINITE
    timeLine.setOnFinished {
        tile.effect = null
    }
    return timeLine
}