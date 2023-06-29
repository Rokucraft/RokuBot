package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.entities.Repository
import com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class RepoCommand(
    private val repositories: List<Repository>,
    private val defaultRepository: Repository?
) : AbstractCommand() {
    override val data = createCommandData()

    override fun autoComplete(event: CommandAutoCompleteInteractionEvent) {
        event.replyChoiceStrings(
            filterCollectionByQueryString(repositories, Repository::name, event)
        ).queue()
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val name = event.getOption("name") { obj: OptionMapping -> obj.asString }
        val repo = if (name == null) defaultRepository else repositories.firstOrNull { it.name == name }
        if (repo == null) {
            val error = if (name != null) "Repository `$name` not found" else "You must provide a name"
            event.replyEmbeds(createErrorEmbed(error)).setEphemeral(true).queue()
            return
        }
        event.reply(repo.repositoryUrl).queue()
    }

    private fun createCommandData(): CommandData {
        val nameRequired = defaultRepository == null // A name is required when no default is provided
        val useAutocomplete = this.repositories.size > OptionData.MAX_CHOICES
        val nameOption = OptionData(
            OptionType.STRING,
            "name",
            "The name of the repository",
            nameRequired,
            useAutocomplete
        )
        if (!useAutocomplete) {
            nameOption.addChoices(this.repositories.map { Command.Choice(it.name, it.name) })
        }
        return Commands.slash("repo", "Shows a link for the named repository")
            .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
            .addOptions(nameOption)
    }
}
