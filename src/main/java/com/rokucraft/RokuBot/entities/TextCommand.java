package com.rokucraft.RokuBot.entities;

import com.rokucraft.RokuBot.Settings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@Deprecated
public class TextCommand extends AbstractEntity {
    private String description;
    private Message message;

    public String getDescription() {
        return description;
    }

    public Message getMessage() {
        return message;
    }

    public void execute(MessageChannel channel) {
        channel.sendMessage(getMessage()).queue();
    }

    public static TextCommand find(String name) {
        return (TextCommand) find(name, Settings.textCommandList);
    }
}
