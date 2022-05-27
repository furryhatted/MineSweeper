package com.github.furryhatted

import javafx.animation.FadeTransition
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.ERROR
import javafx.scene.control.Alert.AlertType.INFORMATION
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import org.slf4j.LoggerFactory
import kotlin.random.Random

class MineSweeper : EventHandler<GameEvent>, Application() {
    private val root = BorderPane()

    private fun createGame(
        columns: Int = 21,//Random.nextInt(3, 20),
        rows: Int = 21,//Random.nextInt(3, 20),
        mines: Int = Random.nextInt(columns * rows / 25, columns * rows / 5),
    ) {
        RootPane(columns, rows, mines)
            .also {
                it.addEventHandler(GameEvent.ANY, this)
                root.center = it
            }
    }

    init {
        if (logger.isDebugEnabled) logger.debug("Launching application")
    }

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UTILITY)
        stage.title = "Mine Sweeper"
        stage.scene = Scene(root)
//        stage.isFullScreen = true
        stage.isResizable = false
        root.id = "screen"
        root.stylesheets.add(parameters.named["stylesheet"] ?: "default.css")
        root.top = GameMenu
        root.addEventHandler(GameEvent.ANY, this)
        createGame()
        stage.show()
        //FIXME: Remove this or leave it - changes fade color for root pane
//        mainStage.scene.fill = Color.BLACK
    }

    //FIXME: Refactor this spaghetti code
    override fun handle(event: GameEvent) {
        if (logger.isDebugEnabled) logger.debug("Received $event")
        when (event.eventType) {
            GameEvent.GAME_WON -> {
                Alert(INFORMATION, "Damn, you won... Your score is: ${(event.source as RootPane).score}")
                    .apply { this.headerText = "SUCCESS!" }
            }
            else -> {
                Alert(ERROR, "You lose, Gringo! Your score is: ${(event.source as RootPane).score}")
                    .apply { this.headerText = "FAIL!" }
            }
        }.apply {
            this.initOwner((event.source as Node).scene.window)
            this.dialogPane.stylesheets.add("default.css")
            this.dialogPane.scene.fill = Color.TRANSPARENT
            this.initStyle(StageStyle.TRANSPARENT)
            this.setOnHidden { createGame() }
            this.dialogPane.opacity = .0
            this.show()
            FadeTransition(Duration.seconds(.3), this.dialogPane).apply {
                fromValue = .0
                toValue = 1.0
                cycleCount = 1
                play()
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MineSweeper::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            launch(MineSweeper::class.java, *args)
        }
    }
}