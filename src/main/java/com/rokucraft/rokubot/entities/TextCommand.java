package com.rokucraft.rokubot.entities;

import com.rokucraft.rokubot.RokuBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

@SuppressWarnings("unused")
@ConfigSerializable
@Deprecated
public class TextCommand extends AbstractEntity {
    private String description;
    @Required
    private MessageCreateData message;

    @Nullable
    public String getDescription() {
        return description;
    }

    @NonNull
    public MessageCreateData getMessage() {
        return message;
    }

    public void execute(MessageChannel channel) {
        channel.sendMessage(getMessage()).queue();
    }

    @Nullable
    public static TextCommand find(String name) {
        return (TextCommand) find(name, RokuBot.getConfig().getTextCommands());
    }
}
