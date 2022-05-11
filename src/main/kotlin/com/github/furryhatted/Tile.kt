package com.github.furryhatted

import javafx.scene.control.Button
import kotlin.random.Random

class Tile : Button(if (Random.nextBoolean()) "X" else "0") {
    init {
        this.style =
        "    -fx-background-color: \n" +
                "        linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),\n" +
                "        linear-gradient(#020b02, #3a3a3a),\n" +
                "        linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),\n" +
                "        linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%);\n" +
                "    -fx-background-insets: 0,1,4,5;\n" +
                "    -fx-background-radius: 9,8,5,4;\n" +
                "    -fx-padding: 5 10 5 10;\n" +
                "    -fx-font-size: 18px;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-text-fill: #333333;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);"
    }
}