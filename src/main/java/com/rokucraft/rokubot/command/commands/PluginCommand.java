package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.AutoCompletable;
import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.entities.Plugin;
import com.rokucraft.rokubot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

import static com.rokucraft.rokubot.util.ColorConstants.GREEN;

public class PluginCommand implements SlashCommand, AutoCompletable, GuildIndependentCommand {
    private final @NonNull CommandData data;
    private final @NonNull List<Plugin> plugins;

    public PluginCommand(@NonNull List<Plugin> plugins) {
        this.plugins = plugins;
        this.data = Commands.slash("plugin", "Get information about a plugin")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                .addOption(OptionType.STRING, "name", "The name of the plugin", true, true)
                .addOptions(
                        new OptionData(OptionType.STRING, "info", "The type of information you want")
                                .addChoice("docs", "docs")
                                .addChoice("download", "download")
                                .addChoice("discord", "discord")
                );
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        String name = event.getOption("name", OptionMapping::getAsString);
        String info = event.getOption("info", OptionMapping::getAsString);
        this.plugins.stream()
                .filter(p -> p.name().equals(name))
                .findFirst().ifPresentOrElse(
                        plugin -> {
                            if (info == null) {
                                event.replyEmbeds(createOverviewEmbed(plugin)).queue();
                            } else {
                                switch (info) {
                                    case "docs" -> event.replyEmbeds(createDocsEmbed(plugin)).queue();
                                    case "download" -> event.replyEmbeds(createDownloadEmbed(plugin)).queue();
                                    case "discord" -> event.reply(createInviteMessage(plugin)).queue();
                                    default -> event.replyEmbeds(createOverviewEmbed(plugin)).queue();
                                }
                            }
                        },
                        () -> event.replyEmbeds(createNotFoundEmbed(name)).setEphemeral(true).queue()
                );
    }

    @Override
    public void autoComplete(@NonNull CommandAutoCompleteInteractionEvent event) {
        event.replyChoiceStrings(
                this.plugins.stream()
                        .map(Plugin::name)
                        .filter(name -> name.toLowerCase().contains(event.getFocusedOption().getValue().toLowerCase()))
                        .limit(25)
                        .toList()
        ).queue();
    }

    private static @NonNull MessageCreateData createInviteMessage(@NonNull Plugin plugin) {
        if (plugin.discordInviteUrl() == null) {
            return new MessageCreateBuilder()
                    .setEmbeds(EmbedUtil.createErrorEmbed("Could not find an invite link for " + plugin.name()))
                    .build();
        }
        return new MessageCreateBuilder().setContent(plugin.discordInviteUrl()).build();
    }

    private static @NonNull MessageEmbed createOverviewEmbed(@NonNull Plugin plugin) {
        EmbedBuilder response = new EmbedBuilder()
                .setColor(GREEN)
                .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f419.png")
                .setTitle(plugin.name(), plugin.resourceUrl())
                .setDescription(plugin.description());
        if (plugin.downloadUrl() != null) {
            response.addField("Download Link", plugin.downloadUrl(), false);
        }
        if (plugin.docsUrl() != null) {
            response.addField("Documentation", plugin.docsUrl(), true);
        }
        if (plugin.repositoryUrl() != null) {
            response.addField("Repository", plugin.repositoryUrl(), true);
        }
        if (!plugin.dependencies().isEmpty()) {
            response.addField("Dependencies", String.join(", ", plugin.dependencies()), false);
        }
        if (plugin.discordInviteUrl() != null) {
            response.addField("Discord", plugin.discordInviteUrl(), false);
        }
        return response.build();
    }

    private static @NonNull MessageEmbed createDocsEmbed(@NonNull Plugin plugin) {
        if (plugin.docsUrl() == null) {
            return EmbedUtil.createErrorEmbed("Could not find a documentation link for " + plugin.name());
        }
        return new EmbedBuilder()
                .setColor(GREEN)
                .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f989.png")
                .setTitle(plugin.name() + " Documentation", plugin.resourceUrl())
                .setDescription("The latest documentation for **" + plugin.name() + "** can be found here:\n"
                        + plugin.docsUrl())
                .build();
    }

    private static @NonNull MessageEmbed createDownloadEmbed(@NonNull Plugin plugin) {
        if (plugin.downloadUrl() == null) {
            return EmbedUtil.createErrorEmbed("Could not find a download link for " + plugin.name());
        }
        return new EmbedBuilder()
                .setColor(GREEN)
                .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f992.png")
                .setTitle("Download " + plugin.name(), plugin.resourceUrl())
                .setDescription("The latest version of **" + plugin.name() + "** can be downloaded here:\n"
                        + plugin.downloadUrl())
                .build();
    }

    private static @NonNull MessageEmbed createNotFoundEmbed(@Nullable String name) {
        return EmbedUtil.createErrorEmbed("Plugin `" + name + "` not found");
    }

    @Override
    public @NonNull CommandData getData() {
        return this.data;
    }
}
