package com.rokucraft.rokubot.util

import com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback

object EmbedUtil {
    fun createErrorEmbed(description: String): MessageEmbed {
        return EmbedBuilder()
            .setColor(ColorConstants.RED)
            .setDescription("❌ $description")
            .build()
    }
}

fun IReplyCallback.replyError(description: String) {
    replyEmbeds(createErrorEmbed(description)).setEphemeral(true).queue()
}
