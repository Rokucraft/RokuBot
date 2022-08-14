package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.command.GlobalCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.util.List;

public class SlashMessageCommand implements GlobalCommand, SlashCommand {
    private final CommandData data;

    private final Message message;
    private final List<Button> buttons;

    public SlashMessageCommand(String name, String description, Message message, List<Button> buttons) {
        this.data = Commands.slash(name, description);
        this.message = message;
        this.buttons = buttons;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        ReplyCallbackAction replyAction = event.reply(message);
        if (buttons.isEmpty()) {
            replyAction.queue();
        } else {
            replyAction.addActionRows(ActionRow.of(buttons)).queue();
        }
    }

    @Override
    public CommandData getData() {
        return data;
    }
}
