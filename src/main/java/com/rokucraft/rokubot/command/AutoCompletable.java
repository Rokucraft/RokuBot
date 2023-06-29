package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.function.Function;

public interface AutoCompletable {
    void autoComplete(@NonNull CommandAutoCompleteInteractionEvent event);

    static <T> Collection<String> filterCollectionByQueryString(
            Collection<T> collection,
            Function<? super T, String> mapper,
            CommandAutoCompleteInteractionEvent event
    ) {
        return collection.stream()
                .map(mapper)
                .filter(string -> string.toLowerCase().contains(event.getFocusedOption().getValue().toLowerCase()))
                .distinct()
                .limit(OptionData.MAX_CHOICES)
                .toList();
    }
}
