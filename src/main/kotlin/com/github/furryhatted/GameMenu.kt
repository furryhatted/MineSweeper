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

    fun newPressed() {
        fireEvent(GameEvent(GAME_FORFEIT))
    }

    fun restartPressed() {
        fireEvent(GameEvent(GAME_FORFEIT, true))
    }

    fun settingsPressed() {
        println("settings")

    }

    fun aboutPressed() {
        println("about")

    }

    fun exitPressed() = with(this.scene.window as Stage) {
        close()
    }
}