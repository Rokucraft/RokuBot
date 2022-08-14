package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

public interface AutoCompletable {
    void autoComplete(CommandAutoCompleteInteractionEvent event);
}
