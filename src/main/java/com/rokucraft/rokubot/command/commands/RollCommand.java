package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.util.ColorConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.security.SecureRandom;
import java.util.Random;

public class RollCommand implements GuildIndependentCommand, SlashCommand {

    private final Random random = new SecureRandom();
    @Override
    public @NonNull CommandData getData() {
        return Commands.slash("roll", "Roll a 20-sided dice");
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        event.replyEmbeds(
                new EmbedBuilder()
                        .setColor(ColorConstants.GREEN)
                        .addField("Roll", "1d20", true)
                        .addField("Result", String.valueOf(this.random.nextInt(1, 21)), true)
                        .build()
        ).queue();
    }
}
