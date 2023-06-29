package com.rokucraft.rokubot.command

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface GuildIndependentCommand : Command {
    override fun getData(guild: Guild): CommandData {
        return getData()
    }

    fun getData(): CommandData
}
