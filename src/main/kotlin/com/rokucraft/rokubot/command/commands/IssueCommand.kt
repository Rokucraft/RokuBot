package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.github.RepositoryCache
import com.rokucraft.rokubot.util.ColorConstants.ISSUE_CLOSED_COLOR
import com.rokucraft.rokubot.util.ColorConstants.ISSUE_OPEN_COLOR
import com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import org.kohsuke.github.GHIssue
import org.kohsuke.github.GHIssueState
import org.kohsuke.github.GitHub
import java.io.IOException
import java.util.*

class IssueCommand(
    private val github: GitHub,
    private val repositoryCache: RepositoryCache,
    private val defaultRepoName: String?
) : AbstractCommand() {

    override val data = Commands.slash("issue", "Preview an issue")
        .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        .addOptions(OptionData(OptionType.INTEGER, "number", "The issue number").setMinValue(1))
        .addOption(OptionType.STRING, "repo", "The repository the issue belongs to", false, true)

    override fun execute(event: SlashCommandInteractionEvent) {
        try {
            val number = event.getOption("number") { it.asInt }
                ?: throw IllegalArgumentException("You must enter a number")
            val repo = event.getOption("repo", defaultRepoName) { it.asString }
            val issue = github.getRepository(repo).getIssue(number)
            event.replyEmbeds(createIssueEmbed(issue)).queue()
        } catch (e: IOException) {
            event.replyEmbeds(createErrorEmbed("Unable to find issue")).setEphemeral(true).queue()
        } catch (e: IllegalArgumentException) {
            event.replyEmbeds(createErrorEmbed(e.message ?: "Something went wrong")).setEphemeral(true).queue()
        }
    }

    override fun autoComplete(event: CommandAutoCompleteInteractionEvent) {
        val query = event.focusedOption.value.lowercase()
        event.replyChoices(
            repositoryCache.repositories
                .filter { it.hasIssues() }
                .filter { it.name.lowercase().contains(query) }
                .take(25)
                .map { Command.Choice(it.name, it.fullName) }
        ).queue()
    }
}

@Throws(IOException::class)
private fun createIssueEmbed(issue: GHIssue): MessageEmbed {
    val open = issue.state == GHIssueState.OPEN
    val author = issue.user
    val authorName = if (author.name != null) author.name else author.login
    var issueBody = issue.body
        .replace(Regex("(?s)<!--.*?-->"), "") // Removes HTML comments
        .replace(Regex("###(.*)"), "**$1**") //Makes Markdown titles bold
        .trim()
    if (issueBody.length > MessageEmbed.DESCRIPTION_MAX_LENGTH) {
        issueBody = issueBody.substring(0, MessageEmbed.DESCRIPTION_MAX_LENGTH - 1) + "…"
    }
    return EmbedBuilder()
        .setColor(if (open) ISSUE_OPEN_COLOR else ISSUE_CLOSED_COLOR)
        .setThumbnail(if (open) ICON_ISSUE_OPEN else ICON_ISSUE_CLOSED)
        .setAuthor(authorName, author.htmlUrl.toString(), author.avatarUrl)
        .setTitle(issue.title, issue.htmlUrl.toString())
        .setDescription(issueBody)
        .setTimestamp(issue.createdAt.toInstant())
        .build()
}

private const val ICON_ISSUE_OPEN =
    "https://cdn.discordapp.com/attachments/786216721065050112/787721554992824360/issue-opened72px.png"
private const val ICON_ISSUE_CLOSED =
    "https://cdn.discordapp.com/attachments/786216721065050112/787721551285059614/issue-closed72px.png"
