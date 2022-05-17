package com.github.furryhatted

import javafx.animation.*
import javafx.scene.Node
import javafx.scene.image.ImageView
import javafx.util.Duration
import kotlin.random.Random

fun swingAnimation(duration: Double, maxSwing: Double): TranslateTransition {
    val animation = TranslateTransition(Duration(duration))
    animation.byX = Random.nextDouble(-maxSwing, maxSwing)
    animation.byY = Random.nextDouble(-maxSwing, maxSwing)
    animation.isAutoReverse = true
    animation.cycleCount = 2
    return animation
}

fun shake(node: Node, duration: Double, magnitude: Double): Animation {
    val cycles = Random.nextInt(4, 6)
    val animation = SequentialTransition(node)
    repeat(cycles) { animation.children.add(swingAnimation(duration / (cycles * 2), magnitude)) }
    animation.play()
    return animation
}

fun explode(tile: Tile, duration: Double): Animation {
    val icon = ImageView("boom.gif")
    icon.fitWidth = (tile.width / 16) * 9
    icon.fitHeight = (tile.height / 16) * 9
    tile.graphic = icon
    val result = Timeline(KeyFrame(Duration(duration), {}))
    result.cycleCount = 1
    result.play()
    return result
}