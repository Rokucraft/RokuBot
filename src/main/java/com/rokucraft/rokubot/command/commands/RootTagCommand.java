package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.entities.Tag;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.checkerframework.checker.nullness.qual.NonNull;

import static net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions.DISABLED;
import static net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions.ENABLED;

public class RootTagCommand implements GuildIndependentCommand, SlashCommand {
    private final @NonNull CommandData data;
    private final @NonNull MessageCreateData message;

    public RootTagCommand(@NonNull Tag tag, boolean defaultEnabled) {
        this.data = Commands.slash(tag.name(), (tag.description() != null) ? tag.description() : tag.name())
                .setDefaultPermissions(defaultEnabled ? ENABLED : DISABLED);
        this.message = tag.message();
    }

    public RootTagCommand(@NonNull Tag tag) {
        this(tag, true);
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
