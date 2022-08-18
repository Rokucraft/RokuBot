package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface AutoCompletable {
    void autoComplete(@NonNull CommandAutoCompleteInteractionEvent event);
}
