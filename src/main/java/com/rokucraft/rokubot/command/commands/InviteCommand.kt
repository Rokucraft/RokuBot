package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.entities.DiscordInvite
import com.rokucraft.rokubot.util.EmbedUtil
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class InviteCommand(
    name: String,
    invites: Collection<DiscordInvite>,
    private val defaultInvite: DiscordInvite?,
    defaultEnabled: Boolean
) : AbstractCommand() {
    override val data: CommandData
    private val invites: Set<DiscordInvite>

    init {
        this.invites = HashSet(invites)
        val nameRequired = defaultInvite == null // A name is required when no default is provided
        val useAutocomplete = this.invites.size > OptionData.MAX_CHOICES
        val nameOption = OptionData(
            OptionType.STRING,
            "name",
            "The name of the server",
            nameRequired,
            useAutocomplete
        )
        if (!useAutocomplete) {
            nameOption.addChoices(this.invites.map { Command.Choice(it.name, it.name) })
        }
        data = Commands.slash(name, "Shows a Discord invite link for the named server")
            .setDefaultPermissions(if (defaultEnabled) DefaultMemberPermissions.ENABLED else DefaultMemberPermissions.DISABLED)
            .addOptions(nameOption)
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val name = event.getOption("name") { it.asString }
        val invite = if (name == null) defaultInvite else invites.firstOrNull { it.name == name }
        if (invite == null) {
            val error = if (name != null) "Invite `$name` not found" else "You must provide a name"
            event.replyEmbeds(EmbedUtil.createErrorEmbed(error)).setEphemeral(true).queue()
            return
        }
        event.reply(invite.inviteUrl()).queue()
    }

    override fun autoComplete(event: CommandAutoCompleteInteractionEvent) {
        event.replyChoiceStrings(
            filterCollectionByQueryString(invites, DiscordInvite::name, event)
        ).queue()
    }
}
