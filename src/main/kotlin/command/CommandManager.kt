package com.rokucraft.rokubot.command

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload

class CommandManager(
    private val globalCommands: Set<AbstractCommand> = emptySet(),
    private val guildCommands: Map<String, Set<AbstractCommand>> = emptyMap()
) {

    private fun findMatchingCommands(
        name: String,
        guild: String?
    ): List<AbstractCommand> {
        return guildCommands.getOrDefault(guild, mutableSetOf())
            .plus(globalCommands)
            .filter { it.data.name == name }
    }

    private fun findMatchingCommands(event: CommandInteractionPayload) =
        findMatchingCommands(event.name, event.guild?.id)

    private fun handleInteraction(
        event: CommandInteractionPayload,
        handler: (AbstractCommand) -> Unit
    ) {
        findMatchingCommands(event).forEach(handler)
    }

    fun register(jda: JDA) {
        jda.updateCommands().addCommands(globalCommands.map { it.data }).queue()

        jda.awaitReady()
        guildCommands.forEach { (guildId, commands) ->
            val guild = jda.getGuildById(guildId) ?: return@forEach
            guild.updateCommands().addCommands(commands.map { it.data }).queue()
        }

        jda.addEventListener(Listener())
    }

    private inner class Listener : ListenerAdapter() {
        override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) =
            handleInteraction(event) { it.execute(event) }

        override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) =
            handleInteraction(event) { it.autoComplete(event) }

        override fun onUserContextInteraction(event: UserContextInteractionEvent) =
            handleInteraction(event) { it.execute(event) }

        override fun onMessageContextInteraction(event: MessageContextInteractionEvent) =
            handleInteraction(event) { it.execute(event) }
    }
}
