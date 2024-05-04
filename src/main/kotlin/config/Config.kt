package com.rokucraft.rokubot.config

import com.rokucraft.rokubot.entities.*
import net.dv8tion.jda.api.entities.MessageEmbed
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class Config(
    val githubClientId: String,
    val githubOrganization: String,
    val botActivity: String?,
    val defaultRepoName: String,
    val rulesFooter: String,
    val welcomeChannelMap: Map<String, String>,
    val trustedServerIds: List<String>,
    val publicInvites: List<DiscordInvite>,
    val privateInvites: MutableList<DiscordInvite>,
    val privateTags: List<Tag>,
    val plugins: List<Plugin>,
    val repositories: MutableList<Repository>,
    val markdownSections: List<MarkdownSection>,
    val rules: List<Rule>,
    val rootTagCommands: List<Tag>,
    val welcomeEmbeds: List<MessageEmbed>
) {
    init {
        privateInvites.addAll(
            plugins.mapNotNull { obj: Plugin -> obj.discordInvite() }
        )
    }
}
