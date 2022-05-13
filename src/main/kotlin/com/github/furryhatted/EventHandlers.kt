package com.github.furryhatted

import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.CONFIRMATION
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

object TileEventHandler : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        val tile = event.source as Tile
        val field = tile.parent as Field
        when (event.button) {
            MouseButton.PRIMARY -> {
                field.open(tile)
                if (tile.isMined) field.fireEvent(GameLostEvent(field))
            }
            MouseButton.SECONDARY -> field.mark(tile)
            else -> Alert(CONFIRMATION, "Dude! Da fuk ur doing?!").showAndWait()
        }
        if (field.tiles.all { it.isMarked || it.isDisabled }) field.fireEvent(GameWonEvent(field))
    }
}