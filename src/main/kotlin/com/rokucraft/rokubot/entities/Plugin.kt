package com.rokucraft.rokubot.entities

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
data class Plugin(
    @Required val name: String,
    val resourceUrl: String?,
    val description: String?,
    val downloadUrl: String?,
    val docsUrl: String?,
    val repositoryUrl: String?,
    val discordInviteCode: String?,
    val dependencies: List<String>
) {
    fun discordInviteUrl(): String? {
        if (this.discordInviteCode == null) return null
        return "https://discord.gg/$discordInviteCode"
    }

    fun discordInvite(): DiscordInvite? {
        if (this.discordInviteCode == null) return null
        return DiscordInvite(this.name, this.discordInviteCode)
    }

    fun repository(): Repository? {
        if (this.repositoryUrl == null) return null
        return Repository(this.name, this.repositoryUrl)
    }
}
