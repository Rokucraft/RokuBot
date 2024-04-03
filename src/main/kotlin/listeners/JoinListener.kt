package com.rokucraft.rokubot.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class JoinListener(
    private val welcomeChannelMap: Map<String, String>,
    private val welcomeEmbeds: List<MessageEmbed>
) : ListenerAdapter() {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(JoinListener::class.java)
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        LOGGER.debug("Received join event for user {}", event.member)
        if (welcomeEmbeds.isEmpty()) return
        val welcomeChannel = welcomeChannelMap[event.guild.id]?.let { event.guild.getTextChannelById(it) } ?: return
        var welcomeEmbed = welcomeEmbeds.random()
        val description = welcomeEmbed.description
        if (description != null && description.contains("%member%")) {
            welcomeEmbed = EmbedBuilder(welcomeEmbed)
                .setDescription(description.replace("%member%", event.member.asMention))
                .build()
        }
        welcomeChannel.sendMessage("<:ds_join:884026626046828585>  Welcome, ${event.member.asMention}!")
            .setEmbeds(welcomeEmbed)
            .queue { LOGGER.debug("Sent join message for user {}", event.member) }
    }
}
