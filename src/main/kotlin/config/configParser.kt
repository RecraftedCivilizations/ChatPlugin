package com.github.DarkVanityOfLight.ChattPlugin.config

import com.github.DarkVanityOfLight.ChattPlugin.Main

class configParser(val main: Main) {
    lateinit var chats: List<String>

    init {
        read()
    }

    private fun read(){
        chats = main.config.getList("Channels") as List<String>

    }

}