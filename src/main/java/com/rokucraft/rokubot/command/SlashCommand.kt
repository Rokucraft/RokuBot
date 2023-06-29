package com.rokucraft.rokubot.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

interface SlashCommand : Command {
    fun execute(event: SlashCommandInteractionEvent)
}
