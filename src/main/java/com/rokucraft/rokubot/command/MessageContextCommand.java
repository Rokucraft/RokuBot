package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface MessageContextCommand extends Command {
    void execute(@NonNull MessageContextInteractionEvent event);
}
