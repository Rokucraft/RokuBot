package com.rokucraft.rokubot.util

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

object EmbedUtil {
    fun createErrorEmbed(description: String): MessageEmbed {
        return EmbedBuilder()
            .setColor(ColorConstants.RED)
            .setDescription("❌ $description")
            .build()
    }
}
