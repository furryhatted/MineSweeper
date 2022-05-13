package com.github.furryhatted

fun adjacentTiles(index: Int, width: Int, size: Int): List<Int> {
    val row = index / width
    val col = index % width
    return (0 until size)
        .filter { (it / width - row) in -1..1 }
        .filter { (it % width - col) in -1..1 }
}
