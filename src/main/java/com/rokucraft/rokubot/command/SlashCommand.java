package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommand extends Command {
    void execute(SlashCommandInteractionEvent event);
}
