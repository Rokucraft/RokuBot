package com.rokucraft.rokubot.entities;

import com.rokucraft.rokubot.config.Settings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
@Deprecated
public class TextCommand extends AbstractEntity {
    private String description;
    private Message message;
    private List<Button> buttons;

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public Message getMessage() {
        return message;
    }

    public void execute(MessageChannel channel) {
        MessageAction messageAction = channel.sendMessage(getMessage());
        if (buttons.isEmpty()) {
            messageAction.queue();
        } else {
            messageAction.setActionRow(buttons).queue();
        }
    }

    @Nullable
    public static TextCommand find(String name) {
        return (TextCommand) find(name, Settings.textCommandList);
    }
}
