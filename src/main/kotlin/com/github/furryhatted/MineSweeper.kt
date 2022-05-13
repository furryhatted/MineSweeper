package com.github.furryhatted

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.WARNING
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.slf4j.LoggerFactory

class MineSweeper : Application() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        logger.debug("Launching application")
    }

    private fun createScene(stage: Stage, c: Int, r: Int, m: Int) {
        stage.scene = Scene(Field(c, r, m)).apply {
            this.stylesheets.add("default.css")
            this.cursor = Cursor.HAND
        }
        stage.sizeToScene()
        stage.show()
    }


    override fun start(stage: Stage) {
        stage.addEventHandler(GAME_LOST) {
            Alert(WARNING, "You lose, Gringo! Your score is: ${it.source.score}").showAndWait()
            createScene(stage, it.source.columns, it.source.rows, it.source.mines)
        }
        stage.addEventHandler(GAME_WON) {
            Alert(WARNING, "Damn, you won... Your score is: ${it.source.score}").showAndWait()
            createScene(stage, it.source.columns, it.source.rows, it.source.mines)
        }

        stage.initStyle(StageStyle.UNIFIED)
        stage.isResizable = false
        stage.title = "Mine Sweeper"
        createScene(stage, 10, 10, 5)
    }
}

fun main(args: Array<String>) {
    launch(*args)
}


