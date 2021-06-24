package com.rokucraft.RokuBot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SlashMessageCommand extends Command {

    private final Message message;

    public SlashMessageCommand(String name, String description, Message message) {
        this.data = new CommandData(name, description);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.reply(message).queue();
    }
}
