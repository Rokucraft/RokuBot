package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import static com.rokucraft.rokubot.Constants.GREEN;

public class ReloadCommand implements SlashCommand {
    private final CommandData data;
    public ReloadCommand() {
        this.data = Commands.slash("reload", "Reload the bot configuration")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        RokuBot.reloadSettings();
        event.replyEmbeds(new EmbedBuilder().setColor(GREEN).setTitle("Successfully reloaded!").build())
                .setEphemeral(true)
                .queue();
    }

    @Override
    public CommandData getData(Guild guild) {
        return data;
    }
}
