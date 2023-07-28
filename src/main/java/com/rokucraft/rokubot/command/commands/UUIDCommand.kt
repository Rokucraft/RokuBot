package com.rokucraft.rokubot.command.commands

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.util.ColorConstants
import com.rokucraft.rokubot.util.EmbedUtil
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*


class UUIDCommand : AbstractCommand() {
    private val httpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    override val data = Commands.slash("uuid", "Get the UUID of a Minecraft player by their username")
        .addOption(OptionType.STRING, "username", "The username of the player", true)
        .setDefaultPermissions(DefaultMemberPermissions.DISABLED)

    override fun execute(event: SlashCommandInteractionEvent) {
        val username = event.getOption("username") { it.asString }
        if (username == null) {
            event.replyEmbeds(EmbedUtil.createErrorEmbed("You must provide a username"))
                .queue()
            return
        }
        val profile = findProfileByUsername(username)
        if (profile == null) {
            event.replyEmbeds(EmbedUtil.createErrorEmbed("No user found with username `$username`"))
                .queue()
            return
        }
        event.replyEmbeds(
            EmbedBuilder()
                .setColor(ColorConstants.GREEN)
                .setAuthor(profile.name, null, "https://crafatar.com/avatars/${profile.uuid}?overlay=true")
                .addField("UUID", "`${profile.uuid}`", false)
                .build()
        ).queue()
    }

    private fun findProfileByUsername(username: String): Profile? {
        val request = Request.Builder()
            .url("https://api.mojang.com/users/profiles/minecraft/$username")
            .build()
        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                return null
            }
            val body = response.body!!.string()
            return objectMapper.readValue(body)
        }
    }
}

data class Profile(val name: String, val id: String) {
    val uuid: UUID = UUID.fromString(fromTrimmed(id))

    private fun fromTrimmed(trimmedUUID: String): String {
        return StringBuilder(trimmedUUID)
            .insert(20, "-")
            .insert(16, "-")
            .insert(12, "-")
            .insert(8, "-")
            .toString()
    }
}