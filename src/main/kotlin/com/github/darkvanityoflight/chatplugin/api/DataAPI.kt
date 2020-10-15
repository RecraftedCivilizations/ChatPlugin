package com.github.darkvanityoflight.chatplugin.api

import com.github.darkvanityoflight.chatplugin.chats.PlayerChat

object DataAPI {
    var initialized = false
    lateinit var channels: Map<String, PlayerChat>

}