package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.rokucraft.rokubot.util.ColorConstants.GREEN;

public class ReloadCommand implements SlashCommand, GuildIndependentCommand {
    private final @NonNull CommandData data;
    private final @NonNull RokuBot bot;

    public ReloadCommand(@NonNull RokuBot bot) {
        this.bot = bot;
        this.data = Commands.slash("reload", "Reload the bot configuration")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        this.bot.reloadSettings();
        event.getHook()
                .setEphemeral(true)
                .sendMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(GREEN)
                                .setTitle("Successfully reloaded!")
                                .build()
                ).queue();
    }

    @Override
    public @NonNull CommandData getData() {
        return this.data;
    }
}
