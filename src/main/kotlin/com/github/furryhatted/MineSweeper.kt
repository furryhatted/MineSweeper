package com.github.furryhatted

import javafx.application.Application
import javafx.application.Application.launch
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.ERROR
import javafx.scene.control.Alert.AlertType.INFORMATION
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.slf4j.LoggerFactory
import kotlin.random.Random

class MineSweeper : EventHandler<GameEvent>, Application() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private lateinit var mainStage: Stage

    init {
        logger.debug("Launching application")
    }

    private fun createScene() {
        val c = 15//Random.nextInt(5, 25)
        val r = 15//Random.nextInt(5, 25)
        val m = Random.nextInt(c * r / 25, c * r / 5)
        mainStage.scene = Scene(RootPane(c, r, m, this))
        mainStage.sizeToScene()
        mainStage.centerOnScreen()
        mainStage.show()
    }


    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UTILITY)
        stage.isResizable = false
        stage.title = "Mine Sweeper"
        this.mainStage = stage
        createScene()
    }

    override fun handle(event: GameEvent) {
        logger.debug("Received ${event.eventType} from ${event.source}")
        when (event.eventType) {
            GameEvent.GAME_WON ->
                Alert(INFORMATION, "Damn, you won... Your score is: ${(event.source as RootPane).score}")
                    .apply { this.headerText = "SUCCESS!" }
            else ->
                Alert(ERROR, "You lose, Gringo! Your score is: ${(event.source as RootPane).score}")
                    .apply { this.headerText = "FAIL!" }
        }.apply {
            this.dialogPane.stylesheets.add("default.css")
            this.dialogPane.styleClass.add("dialog-pane")
            this.initStyle(StageStyle.UNDECORATED)
            showAndWait()
        }
        createScene()
    }
}

fun main(args: Array<String>) {
    launch(*args)
}


