package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface UserContextCommand extends Command {
    void execute(@NonNull UserContextInteractionEvent event);
}
