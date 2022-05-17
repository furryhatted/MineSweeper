package com.github.furryhatted

import javafx.animation.FadeTransition
import javafx.application.Application
import javafx.application.Application.launch
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.ERROR
import javafx.scene.control.Alert.AlertType.INFORMATION
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import org.slf4j.LoggerFactory
import kotlin.random.Random

class MineSweeper : EventHandler<GameEvent>, Application() {
    private lateinit var mainStage: Stage

    init {
        if (logger.isDebugEnabled) logger.debug("Launching application")
    }

    private fun createScene() {
        val c = Random.nextInt(5, 25)
        val r = Random.nextInt(5, 25)
        val m = Random.nextInt(c * r / 25, c * r / 5)
        mainStage.scene = Scene(RootPane(c, r, m, this))
        //mainStage.scene.fill = Color.BLACK
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
        if (logger.isDebugEnabled) logger.debug("Received ${event.eventType} from ${event.source}")
        when (event.eventType) {
            GameEvent.GAME_WON ->
                Alert(INFORMATION, "Damn, you won... Your score is: ${(event.source as RootPane).score}")
                    .apply { this.headerText = "SUCCESS!" }
            else ->
                Alert(ERROR, "You lose, Gringo! Your score is: ${(event.source as RootPane).score}")
                    .apply { this.headerText = "FAIL!" }
        }.apply {
            this.initOwner(mainStage)
            this.dialogPane.stylesheets.add("default.css")
            this.dialogPane.scene.fill = Color.TRANSPARENT
            this.initStyle(StageStyle.TRANSPARENT)
            this.setOnHidden { createScene() }
            this.dialogPane.opacity = .0
            this.show()
            FadeTransition(Duration(750.0), this.dialogPane).apply {
                fromValue = .0
                toValue = 1.0
                cycleCount = 1
                play()
            }
        }

    }
    
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}

fun main(args: Array<String>) {
    launch(*args)
}


