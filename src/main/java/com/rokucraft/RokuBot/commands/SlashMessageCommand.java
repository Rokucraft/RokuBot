package com.rokucraft.RokuBot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import java.util.List;

public class SlashMessageCommand extends Command {

    private final Message message;
    private final List<Button> buttons;

    public SlashMessageCommand(String name, String description, Message message, List<Button> buttons) {
        this.data = new CommandData(name, description);
        this.message = message;
        this.buttons = buttons;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        ReplyAction replyAction = event.reply(message);
        if (buttons.isEmpty()) {
            replyAction.queue();
        } else {
            replyAction.addActionRows(ActionRow.of(buttons)).queue();
        }
    }
}
