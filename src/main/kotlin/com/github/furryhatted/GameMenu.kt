package com.github.furryhatted

import com.github.furryhatted.GameEvent.Companion.GAME_LOST
import javafx.application.Platform
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem

object GameMenu : MenuBar() {
    init {
        val exit = MenuItem("Exit").apply {
            setOnAction { Platform.exit() }
        }
        val new = MenuItem("New").apply {
            setOnAction {
                fireEvent(GameEvent(GAME_LOST))
            }
        }


        val menu = Menu("Game").apply {
            items.add(new)
            items.add(exit)
        }
        this.menus.add(menu)
    }
}