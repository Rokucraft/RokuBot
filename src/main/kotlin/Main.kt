package com.rokucraft.rokubot

import com.rokucraft.rokubot.di.DaggerRokuBotComponent

fun main() {
    val botComponent = DaggerRokuBotComponent.create()
    botComponent.bot()
}
