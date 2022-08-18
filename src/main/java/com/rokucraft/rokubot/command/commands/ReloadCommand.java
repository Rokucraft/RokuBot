package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.command.GlobalCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.rokucraft.rokubot.Constants.GREEN;

public class ReloadCommand implements SlashCommand, GlobalCommand {
    private final CommandData data;
    public ReloadCommand() {
        this.data = Commands.slash("reload", "Reload the bot configuration")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        RokuBot.reloadSettings();
        event.replyEmbeds(new EmbedBuilder().setColor(GREEN).setTitle("Successfully reloaded!").build())
                .setEphemeral(true)
                .queue();
    }

    @Override
    public @NonNull CommandData getData() {
        return data;
    }
}
