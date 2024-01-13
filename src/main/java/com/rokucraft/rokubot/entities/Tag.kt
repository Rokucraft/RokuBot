package com.rokucraft.rokubot.entities

import net.dv8tion.jda.api.utils.messages.MessageCreateData
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
@JvmRecord
data class Tag(
    @Required val name: String,
    val description: String?,
    @Required val message: MessageCreateData
)
