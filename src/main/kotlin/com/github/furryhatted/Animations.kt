package com.github.furryhatted

import javafx.animation.SequentialTransition
import javafx.animation.TranslateTransition
import javafx.scene.Node
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

fun shake(node: Node, duration: Double, magnitude: Double) {
    val cycles = Random.nextInt(4, 6)
    val animation = SequentialTransition(node)
    repeat(cycles) { animation.children.add(swingAnimation(duration / cycles * 2, magnitude)) }
    animation.play()
}

