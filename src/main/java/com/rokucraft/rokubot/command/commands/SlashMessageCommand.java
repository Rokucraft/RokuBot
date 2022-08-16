package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.GlobalCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SlashMessageCommand implements GlobalCommand, SlashCommand {
    private final CommandData data;

    private final Message message;

    public SlashMessageCommand(String name, String description, Message message) {
        this.data = Commands.slash(name, description);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply(message).queue();
    }

    @Override
    public CommandData getData() {
        return data;
    }
}
