package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.RokuBot
import com.rokucraft.rokubot.command.GuildIndependentCommand
import com.rokucraft.rokubot.command.SlashCommand
import com.rokucraft.rokubot.util.ColorConstants.GREEN
import com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.slf4j.LoggerFactory
import org.spongepowered.configurate.ConfigurateException

class ReloadCommand(private val bot: RokuBot) : SlashCommand, GuildIndependentCommand {

    override fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply(true).queue()
        var response: MessageEmbed
        try {
            bot.reloadSettings()
            response = EmbedBuilder()
                .setColor(GREEN)
                .setTitle("Successfully reloaded!")
                .build()
        } catch (e: ConfigurateException) {
            response = createErrorEmbed("Something went wrong while trying to reload the bot.")
            LOGGER.error("Something went wrong while trying to reload the bot.", e)
        }
        event.hook
            .setEphemeral(true)
            .sendMessageEmbeds(response)
            .queue()
    }

    override fun getData(): CommandData {
        return Commands.slash("reload", "Reload the bot configuration")
            .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ReloadCommand::class.java)
    }
}
