package com.rokucraft.rokubot

import com.rokucraft.rokubot.command.CommandManager
import com.rokucraft.rokubot.config.Config
import com.rokucraft.rokubot.listeners.JoinListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import javax.inject.Inject

class RokuBot @Inject constructor(
    jda: JDA,
    config: Config,
    commandManager: CommandManager
) {

    init {
        if (config.botActivity != null) {
            jda.presence.activity = Activity.playing(config.botActivity)
        }
        jda.addEventListener(JoinListener(config.welcomeChannelMap, config.welcomeEmbeds))

        commandManager.register(jda)
    }
}
