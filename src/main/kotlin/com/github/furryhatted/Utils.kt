package com.github.furryhatted

fun adjacentTiles(index: Int, width: Int, size: Int): List<Int> {
    val row = index / width
    val col = index % width
    return (0 until size)
        .filter { (it / width - row) in -1..1 }
        .filter { (it % width - col) in -1..1 }
        .minus(index)
}

const val BOMB: String = "\uD83D\uDCA3"
const val FLAG: String = "\uD83D\uDEA9"
const val SKULL: String = "\uD83D\uDD71"
const val FIELD: String = "\uD83D\uDF96"
const val STOPWATCH: String = "\u23F1"

enum class GameState {
    LOSS,
    WIN
}