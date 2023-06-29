package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.GuildIndependentCommand
import com.rokucraft.rokubot.command.SlashCommand
import com.rokucraft.rokubot.entities.Tag
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class RootTagCommand @JvmOverloads constructor(
    private val tag: Tag,
    private val defaultPermissions: DefaultMemberPermissions = DefaultMemberPermissions.DISABLED
) : GuildIndependentCommand, SlashCommand {
    override fun execute(event: SlashCommandInteractionEvent) {
        event.reply(tag.message).queue()
    }

    override fun getData(): CommandData {
        return Commands.slash(tag.name, tag.description ?: tag.name)
            .setDefaultPermissions(defaultPermissions)
    }
}
