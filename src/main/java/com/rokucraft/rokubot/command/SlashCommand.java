package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface SlashCommand extends Command {
    void execute(@NonNull SlashCommandInteractionEvent event);
}
