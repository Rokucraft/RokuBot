package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.util.ColorConstants
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import java.security.SecureRandom
import java.util.*

class RollCommand : AbstractCommand() {
    override val data = Commands.slash("roll", "Roll a 20-sided dice")
    private val random: Random = SecureRandom()

    override fun execute(event: SlashCommandInteractionEvent) {
        event.replyEmbeds(
            EmbedBuilder()
                .setColor(ColorConstants.GREEN)
                .addField("Roll", "1d20", true)
                .addField("Result", random.nextInt(1, 21).toString(), true)
                .build()
        ).queue()
    }
}
