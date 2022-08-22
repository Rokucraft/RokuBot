package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.checkerframework.checker.nullness.qual.NonNull;

import static net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions.DISABLED;
import static net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions.ENABLED;

public class SlashMessageCommand implements GuildIndependentCommand, SlashCommand {
    private final @NonNull CommandData data;
    private final @NonNull MessageCreateData message;

    public SlashMessageCommand(
            @NonNull String name,
            @NonNull String description,
            @NonNull MessageCreateData message,
            boolean defaultEnabled
    ) {
        this.data = Commands.slash(name, description)
                .setDefaultPermissions(defaultEnabled ? ENABLED : DISABLED);
        this.message = message;
    }

    public SlashMessageCommand(@NonNull String name, @NonNull String description, @NonNull MessageCreateData message) {
        this(name, description, message, true);
    }

    public @NonNull MessageCreateData getMessage() {
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
