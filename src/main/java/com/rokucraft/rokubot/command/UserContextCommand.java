package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;

public interface UserContextCommand extends Command {
    void execute(UserContextInteractionEvent event);
}
