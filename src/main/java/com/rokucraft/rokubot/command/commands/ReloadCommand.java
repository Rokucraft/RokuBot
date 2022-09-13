package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurateException;

import static com.rokucraft.rokubot.util.ColorConstants.GREEN;

public class ReloadCommand implements SlashCommand, GuildIndependentCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadCommand.class);
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
        MessageEmbed response;
        try {
            this.bot.reloadSettings();
            response = new EmbedBuilder()
                    .setColor(GREEN)
                    .setTitle("Successfully reloaded!")
                    .build();
        } catch (ConfigurateException e) {
            response = EmbedUtil.createErrorEmbed("Something went wrong while trying to reload the bot.");
            LOGGER.error("Something went wrong while trying to reload the bot.", e);
        }
        event.getHook()
                .setEphemeral(true)
                .sendMessageEmbeds(response)
                .queue();
    }

    @Override
    public @NonNull CommandData getData() {
        return this.data;
    }
}
