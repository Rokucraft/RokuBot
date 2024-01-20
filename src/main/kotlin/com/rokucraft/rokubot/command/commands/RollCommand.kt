package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.util.ColorConstants
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands

class RollCommand : AbstractCommand() {
    override val data = Commands.slash("roll", "Roll a 20-sided dice")

    override fun execute(event: SlashCommandInteractionEvent) {
        event.replyEmbeds(
            EmbedBuilder()
                .setColor(ColorConstants.GREEN)
                .addField("Roll", "1d20", true)
                .addField("Result", (1..21).random().toString(), true)
                .build()
        ).queue()
    }
}
