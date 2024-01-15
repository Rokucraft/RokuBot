package com.rokucraft.rokubot.entities

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
@JvmRecord
data class Rule(
    @Required val name: String,
    val description: String?
)
