package com.rokucraft.rokubot.entities

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
data class DiscordInvite(
    @Required val name: String,
    @Required val inviteCode: String,
) {
    fun inviteUrl(): String = "https://discord.gg/$inviteCode"
}
