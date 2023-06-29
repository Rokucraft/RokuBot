package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.entities.Tag
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.Commands

class RootTagCommand @JvmOverloads constructor(
    private val tag: Tag,
    defaultPermissions: DefaultMemberPermissions = DefaultMemberPermissions.DISABLED
) : AbstractCommand() {
    override val data = Commands.slash(tag.name, tag.description ?: tag.name)
        .setDefaultPermissions(defaultPermissions)

    override fun execute(event: SlashCommandInteractionEvent) {
        event.reply(tag.message).queue()
    }
}
