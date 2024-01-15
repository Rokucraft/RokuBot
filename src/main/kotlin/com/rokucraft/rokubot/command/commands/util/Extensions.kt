package com.rokucraft.rokubot.command.commands.util

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData

fun <T> Iterable<T>.filterByQueryString(
    event: CommandAutoCompleteInteractionEvent,
    mapper: (T) -> String
): Collection<String> {
    return this
        .map(mapper)
        .filter { it.lowercase().contains(event.focusedOption.value.lowercase()) }
        .distinct()
        .take(OptionData.MAX_CHOICES)
}
