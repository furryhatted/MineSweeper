package com.github.furryhatted

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.stage.Stage
import org.slf4j.LoggerFactory

class MineSweeper : Application() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        logger.debug("Launching application")
    }

    override fun start(stage: Stage) {
        stage.title = "Mine Sweeper"
        val scene = MineField()
        scene.setPrefSize(238.0, 200.0)
        stage.scene = Scene(scene)
        stage.show()
    }
}

fun main(args: Array<String>) {
    launch(*args)
}


