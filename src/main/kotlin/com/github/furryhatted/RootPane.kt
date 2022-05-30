package com.github.furryhatted

import javafx.animation.FadeTransition
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.*
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import org.slf4j.LoggerFactory
import kotlin.random.Random

class RootPane : EventHandler<GameEvent>, BorderPane() {
    private val gameSettings: Triple<Int, Int, Int>
        get() {
            val columns: Int = Random.nextInt(3, 21)
            val rows: Int = Random.nextInt(3, 21)
            val mines: Int = Random.nextInt(1, columns * rows / 3)
            return Triple(columns, rows, mines)
        }

    init {
        this.id = "root"
        this.top = GameMenu()
            .apply { addEventHandler(GameEvent.ANY, this@RootPane) }
        this.center = GamePane(gameSettings)
            .apply { addEventHandler(GameEvent.ANY, this@RootPane) }
    }

    //FIXME: Refactor this spaghetti code
    override fun handle(event: GameEvent) {
        if (logger.isDebugEnabled) logger.debug("Received $event")
        event.consume()
        (this.center as GamePane).finish()
        when (event.eventType) {
            GameEvent.GAME_WON ->
                Alert(INFORMATION, "Damn, you won...")
                    .apply { this.headerText = "SUCCESS!" }
            GameEvent.GAME_LOST ->
                Alert(ERROR, "You lose, Gringo!")
                    .apply { this.headerText = "FAIL!" }
            GameEvent.GAME_FORFEIT ->
                Alert(WARNING, "Well... if you insist...")
                    .apply { this.headerText = "FORFEIT!" }
            else -> return
        }.apply {
            if (event.score != null) this.contentText += "\nYour score is: ${event.score}"
            if (event.isRestart) this.contentText += "\nRestarting with same parameters!"
            this.initOwner((event.source as Node).scene.window)
            this.dialogPane.stylesheets.add("default.css")
            this.dialogPane.scene.fill = Color.TRANSPARENT
            this.initStyle(StageStyle.TRANSPARENT)
            this.setOnHidden {
                center =
                    if (event.isRestart) GamePane((center as GamePane).settings)
                    else GamePane(gameSettings)
                center.apply { addEventHandler(GameEvent.ANY, this@RootPane) }
                with(scene.window as Stage) {
                    if (isFullScreen || !isResizable) return@with
                    sizeToScene()
                    centerOnScreen()
                }
            }
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
    }
}