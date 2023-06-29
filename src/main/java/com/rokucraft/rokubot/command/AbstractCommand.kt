package com.rokucraft.rokubot.command

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData

abstract class AbstractCommand {

    abstract val data: CommandData
    open fun autoComplete(event: CommandAutoCompleteInteractionEvent) {}

    open fun execute(event: SlashCommandInteractionEvent) {}

    open fun execute(event: MessageContextInteractionEvent) {}

    open fun execute(event: UserContextInteractionEvent) {}

    companion object {
        fun <T> filterCollectionByQueryString(
            collection: Collection<T>,
            mapper: (T) -> String,
            event: CommandAutoCompleteInteractionEvent
        ): Collection<String> {
            return collection
                .map(mapper)
                .filter { it.lowercase().contains(event.focusedOption.value.lowercase()) }
                .distinct()
                .take(OptionData.MAX_CHOICES)
        }
    }
}