package com.github.darkvanityoflight.chatplugin.api

import com.github.darkvanityoflight.chatplugin.parser.ConfigParser
import com.github.darkvanityoflight.chatplugin.parser.DataParser

object ParserAPI {
    var initialized : Boolean = false
    lateinit var dataParser : DataParser
    lateinit var configParser: ConfigParser
    
}