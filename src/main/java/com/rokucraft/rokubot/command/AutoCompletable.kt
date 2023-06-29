package com.rokucraft.rokubot.command

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.util.*
import java.util.function.Function

interface AutoCompletable {
    fun autoComplete(event: CommandAutoCompleteInteractionEvent)

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
