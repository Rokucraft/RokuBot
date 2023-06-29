package com.rokucraft.rokubot.command

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent

interface MessageContextCommand : Command {
    fun execute(event: MessageContextInteractionEvent)
}
