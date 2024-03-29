package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.data.PluginRepository
import com.rokucraft.rokubot.entities.Plugin
import com.rokucraft.rokubot.util.ColorConstants
import com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed
import com.rokucraft.rokubot.util.replyError
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import javax.inject.Inject

class PluginCommand @Inject constructor(
    private val pluginRepository: PluginRepository
) : AbstractCommand() {
    override val data = Commands.slash("plugin", "Get information about a plugin")
        .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        .addOption(OptionType.STRING, "name", "The name of the plugin", true, true)
        .addOptions(
            OptionData(OptionType.STRING, "info", "The type of information you want")
                .addChoice("docs", "docs")
                .addChoice("download", "download")
                .addChoice("discord", "discord")
        )

    override fun execute(event: SlashCommandInteractionEvent) {
        val name = event.getOption("name") { it.asString }
        val info = event.getOption("info") { it.asString }
        if (name == null) {
            event.replyError("You must provide a name")
            return
        }
        val plugin = pluginRepository.getByName(name)
        if (plugin == null) {
            event.replyError("Plugin `$name` not found")
            return
        }
        when (info) {
            "docs" -> event.replyEmbeds(createDocsEmbed(plugin)).queue()
            "download" -> event.replyEmbeds(createDownloadEmbed(plugin)).queue()
            "discord" -> event.reply(createInviteMessage(plugin)).queue()
            else -> event.replyEmbeds(createOverviewEmbed(plugin)).queue()
        }
    }

    override fun autoComplete(event: CommandAutoCompleteInteractionEvent) {
        event.replyChoiceStrings(
            pluginRepository.getNamesContaining(event.focusedOption.value)
        ).queue()
    }
}

private fun createInviteMessage(plugin: Plugin): MessageCreateData {
    return if (plugin.discordInviteUrl() == null) {
        MessageCreateBuilder()
            .setEmbeds(createErrorEmbed("Could not find an invite link for " + plugin.name))
            .build()
    } else MessageCreateBuilder().setContent(plugin.discordInviteUrl()).build()
}

private fun createOverviewEmbed(plugin: Plugin): MessageEmbed {
    val response: EmbedBuilder = EmbedBuilder()
        .setColor(ColorConstants.GREEN)
        .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f419.png")
        .setTitle(plugin.name, plugin.resourceUrl)
        .setDescription(plugin.description)
    if (plugin.downloadUrl != null) {
        response.addField("Download Link", plugin.downloadUrl, false)
    }
    if (plugin.docsUrl != null) {
        response.addField("Documentation", plugin.docsUrl, true)
    }
    if (plugin.repositoryUrl != null) {
        response.addField("Repository", plugin.repositoryUrl, true)
    }
    if (plugin.dependencies.isNotEmpty()) {
        response.addField("Dependencies", java.lang.String.join(", ", plugin.dependencies), false)
    }
    if (plugin.discordInviteUrl() != null) {
        response.addField("Discord", plugin.discordInviteUrl()!!, false)
    }
    return response.build()
}

private fun createDocsEmbed(plugin: Plugin): MessageEmbed {
    return if (plugin.docsUrl == null) {
        createErrorEmbed("Could not find a documentation link for " + plugin.name)
    } else EmbedBuilder()
        .setColor(ColorConstants.GREEN)
        .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f989.png")
        .setTitle(plugin.name + " Documentation", plugin.resourceUrl)
        .setDescription(
            """
                The latest documentation for **${plugin.name}** can be found here:
                ${plugin.docsUrl}
                """.trimIndent()
        )
        .build()
}

private fun createDownloadEmbed(plugin: Plugin): MessageEmbed {
    return if (plugin.downloadUrl == null) {
        createErrorEmbed("Could not find a download link for " + plugin.name)
    } else EmbedBuilder()
        .setColor(ColorConstants.GREEN)
        .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f992.png")
        .setTitle("Download " + plugin.name, plugin.resourceUrl)
        .setDescription(
            """
                The latest version of **${plugin.name}** can be downloaded here:
                ${plugin.downloadUrl}
                """.trimIndent()
        )
        .build()
}
