package com.github.furryhatted

import com.github.furryhatted.GameEvent.Companion.GAME_FORFEIT
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.stage.Stage

class GameMenu : MenuBar() {
    private val newItem = MenuItem("New").apply { setOnAction { newPressed() } }
    private val restartItem = MenuItem("Restart").apply { setOnAction { restartPressed() } }
    private val settingsItem = MenuItem("Settings").apply { setOnAction { settingsPressed() } }
    private val aboutItem = MenuItem("About").apply { setOnAction { aboutPressed() } }
    private val exitItem = MenuItem("Exit").apply { setOnAction { exitPressed() } }

    init {
        this.menus.add(
            Menu("Game")
                .apply { items.addAll(newItem, restartItem, exitItem) })
        this.menus.add(
            Menu("Options")
                .apply { items.addAll(settingsItem) }
        )
        this.menus.add(
            Menu("Help")
                .apply { items.addAll(aboutItem) }
        )
    }

    private fun newPressed() {
        fireEvent(GameEvent(GAME_FORFEIT))
    }

    private fun restartPressed() {
        fireEvent(GameEvent(GAME_FORFEIT, true))
    }

    private fun settingsPressed() {
        println("settings")

    }

    private fun aboutPressed() {
        println("about")

    }

    private fun exitPressed() = with(this.scene.window as Stage) {
        close()
    }
}