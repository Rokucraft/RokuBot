package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.checkerframework.checker.nullness.qual.NonNull;

public class SlashMessageCommand implements GuildIndependentCommand, SlashCommand {
    private final @NonNull CommandData data;
    private final @NonNull Message message;

    public SlashMessageCommand(@NonNull String name, @NonNull String description, @NonNull Message message) {
        this.data = Commands.slash(name, description);
        this.message = message;
    }

    public @NonNull Message getMessage() {
        return message;
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        event.reply(message).queue();
    }

    @Override
    public @NonNull CommandData getData() {
        return data;
    }
}
