package com.rokucraft.rokubot.command

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload
import kotlin.reflect.KClass
import kotlin.reflect.cast

class CommandManager(private val jda: JDA) : ListenerAdapter() {
    private val globalCommands: MutableSet<GuildIndependentCommand> = mutableSetOf()
    private val guildCommands: MutableMap<Guild?, MutableSet<Command>> = mutableMapOf()

    init {
        jda.addEventListener(this)
    }

    fun addGuildCommands(guild: Guild, vararg commands: Command) {
        addGuildCommands(guild, commands.asList())
    }

    fun addGuildCommands(guild: Guild, commands: List<Command>) {
        guildCommands.computeIfAbsent(guild) { mutableSetOf() }.addAll(commands)
    }

    fun addCommands(vararg commands: GuildIndependentCommand) {
        addCommands(commands.asList())
    }

    fun addCommands(commands: Collection<GuildIndependentCommand>) {
        globalCommands.addAll(commands)
    }

    fun clearCommands() {
        globalCommands.clear()
    }

    fun clearGuildCommands() {
        guildCommands.clear()
    }

    fun clearAll() {
        clearCommands()
        clearGuildCommands()
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        findFirstMatchingCommand(SlashCommand::class, event)?.execute(event)
    }

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        findFirstMatchingCommand(AutoCompletable::class, event)?.autoComplete(event)
    }

    override fun onUserContextInteraction(event: UserContextInteractionEvent) {
        findFirstMatchingCommand(UserContextCommand::class, event)?.execute(event)
    }

    override fun onMessageContextInteraction(event: MessageContextInteractionEvent) {
        findFirstMatchingCommand(MessageContextCommand::class, event)?.execute(event)
    }

    private fun <T : Any> findFirstMatchingCommand(clazz: KClass<T>, event: CommandInteractionPayload): T? {
        return findFirstMatchingCommand(clazz, event.name, event.guild)
    }

    private fun <T : Any> findFirstMatchingCommand(
        clazz: KClass<T>,
        name: String,
        guild: Guild?
    ): T? {
        val commandsInGuild: Set<Command> = guildCommands.computeIfAbsent(guild) { mutableSetOf() }
        return commandsInGuild.asSequence()
            .plus(globalCommands)
            .filter(clazz::isInstance)
            .filter {
                if (guild != null)
                    it.getData(guild).name == name
                else
                    it is GuildIndependentCommand && it.getData().name == name
            }
            .map(clazz::cast)
            .firstOrNull()
    }

    fun registerCommands() {
        jda.updateCommands().addCommands(globalCommands.map { it.getData() }).queue()
        guildCommands.forEach { (guild, commands) ->
            if (guild == null) return
            guild.updateCommands().addCommands(commands.map { it.getData(guild) }).queue()
        }
    }
}
