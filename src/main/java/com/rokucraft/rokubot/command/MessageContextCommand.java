package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;

public interface MessageContextCommand extends Command {
    void execute(MessageContextInteractionEvent event);
}
