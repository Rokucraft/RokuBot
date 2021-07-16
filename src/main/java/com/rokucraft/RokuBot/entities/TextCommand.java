package com.rokucraft.RokuBot.entities;

import com.rokucraft.RokuBot.Settings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@Deprecated
public class TextCommand extends AbstractEntity {
    private String description;
    private Message message;

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public Message getMessage() {
        return message;
    }

    public void execute(MessageChannel channel) {
        channel.sendMessage(getMessage()).queue();
    }

    @Nullable
    public static TextCommand find(String name) {
        return (TextCommand) find(name, Settings.textCommandList);
    }
}
