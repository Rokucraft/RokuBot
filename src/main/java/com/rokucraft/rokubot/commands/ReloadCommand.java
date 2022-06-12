package com.rokucraft.rokubot.commands;

import com.rokucraft.rokubot.RokuBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import static com.rokucraft.rokubot.Constants.GREEN;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        this.data = Commands.slash("reload", "Reload the bot configuration").setDefaultEnabled(false);
        setGuildOnly(true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        RokuBot.reloadSettings();
        event.replyEmbeds(new EmbedBuilder().setColor(GREEN).setTitle("Successfully reloaded!").build())
                .setEphemeral(true)
                .queue();
    }
}
