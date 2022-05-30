package com.github.furryhatted

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.slf4j.LoggerFactory

class MineSweeper : Application() {
    private val root = RootPane()


    init {
        if (logger.isDebugEnabled) logger.debug("Launching application")
    }

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UTILITY)
        stage.title = "Mine Sweeper"
        stage.scene = Scene(root)
//        stage.isFullScreen = true
//        stage.isResizable = false
        root.stylesheets.add(parameters.named["stylesheet"] ?: "default.css")
//        root.addEventHandler(GameEvent.ANY, this)
        stage.show()
        //FIXME: Remove this or leave it - changes fade color for root pane
//        mainStage.scene.fill = Color.BLACK
    }


    companion object {
        private val logger = LoggerFactory.getLogger(MineSweeper::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            launch(MineSweeper::class.java, *args)
        }
    }
}