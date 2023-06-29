package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.GuildIndependentCommand
import com.rokucraft.rokubot.command.SlashCommand
import com.rokucraft.rokubot.entities.Tag
import com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class TagCommand(private val tags: List<Tag>) : SlashCommand, GuildIndependentCommand {

    override fun getData(): CommandData {
        return Commands.slash("tag", "Show tagged information")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                .addOptions(
                        OptionData(OptionType.STRING, "name", "The tag name", true)
                                .addChoices(this.tags.map { Command.Choice(it.name, it.name) })
                )
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val name = event.getOption("name") { it.asString }
        val tag = tags.firstOrNull { it.name == name }
        if (tag == null) event.replyEmbeds(createErrorEmbed("Tag `$name` does not exist")).setEphemeral(true).queue()
        else event.reply(tag.message).queue()
    }
}
