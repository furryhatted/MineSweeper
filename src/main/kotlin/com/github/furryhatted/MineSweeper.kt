package com.github.furryhatted

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.slf4j.LoggerFactory
import kotlin.random.Random

class MineSweeper : Application() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        logger.debug("Launching application")
    }

    private val root: VBox
        get() = VBox().apply {
            this.stylesheets.add("default.css")
            this.styleClass.add("root")
        }

    private fun createScene(stage: Stage) {
        val c = Random.nextInt(5, 30)
        val r = Random.nextInt(5, 30)
        val m = Random.nextInt(c * r / 25, c * r / 5)
        stage.scene = Scene(root.apply { children.add(Field(c, r, m)) })
        stage.sizeToScene()
        stage.centerOnScreen()
        stage.show()
    }


    override fun start(stage: Stage) {
        stage.addEventHandler(GAME_LOST) {
            Alert(ERROR, "You lose, Gringo! Your score is: ${it.source.score}")
                .apply {
                    this.headerText= "FAIL!"
                    this.dialogPane.stylesheets.add("default.css")
                    this.dialogPane.styleClass.add("dialog-pane")
                    this.initStyle(StageStyle.UNDECORATED)
                    showAndWait()
                }
            createScene(stage)
        }
        stage.addEventHandler(GAME_WON) {
            Alert(INFORMATION, "Damn, you won... Your score is: ${it.source.score}")
                .apply {
                    this.headerText= "SUCCESS!"
                    this.dialogPane.stylesheets.add("default.css")
                    this.dialogPane.styleClass.add("dialog-pane")
                    this.initStyle(StageStyle.UNDECORATED)
                    showAndWait()
                }
            createScene(stage)
        }

        stage.initStyle(StageStyle.UTILITY)
        stage.isResizable = false
        stage.title = "Mine Sweeper"



        createScene(stage)
    }
}

fun main(args: Array<String>) {
    launch(*args)
}


