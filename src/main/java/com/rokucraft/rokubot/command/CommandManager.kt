package com.rokucraft.rokubot.command

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload

class CommandManager {
    private val globalCommands: MutableSet<AbstractCommand> = mutableSetOf()
    private val guildCommands: MutableMap<Guild?, MutableSet<AbstractCommand>> = mutableMapOf()

    fun addGuildCommands(guild: Guild, commands: List<AbstractCommand>) {
        guildCommands.computeIfAbsent(guild) { mutableSetOf() }.addAll(commands)
    }

    fun addCommands(commands: Collection<AbstractCommand>) {
        globalCommands.addAll(commands)
    }

    private fun findMatchingCommands(
        name: String,
        guild: Guild?
    ): List<AbstractCommand> {
        return guildCommands.getOrDefault(guild, mutableSetOf())
            .plus(globalCommands)
            .filter { it.data.name == name }
    }

    private fun findMatchingCommands(event: CommandInteractionPayload) = findMatchingCommands(event.name, event.guild)

    private fun handleInteraction(
        event: CommandInteractionPayload,
        handler: (AbstractCommand) -> Unit
    ) {
        findMatchingCommands(event).forEach(handler)
    }

    fun register(jda: JDA) {
        jda.updateCommands().addCommands(globalCommands.map { it.data }).queue()
        guildCommands.forEach { (guild, commands) ->
            if (guild == null) return
            guild.updateCommands().addCommands(commands.map { it.data }).queue()
        }

        jda.addEventListener(Listener())
    }

    private inner class Listener : ListenerAdapter() {
        override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
            handleInteraction(event) { it.execute(event) }
        }

        override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
            handleInteraction(event) { it.autoComplete(event) }
        }

        override fun onUserContextInteraction(event: UserContextInteractionEvent) {
            handleInteraction(event) { it.execute(event) }
        }

        override fun onMessageContextInteraction(event: MessageContextInteractionEvent) {
            handleInteraction(event) { it.execute(event) }
        }
    }
}
