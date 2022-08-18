package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.AutoCompletable;
import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.entities.Plugin;
import com.rokucraft.rokubot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

import static com.rokucraft.rokubot.Constants.GREEN;

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
                .filter(p -> p.getName().equals(name))
                .findFirst().ifPresentOrElse(
                        plugin -> {
                            Message response = (info == null)
                                    ? toMessage(createOverviewEmbed(plugin))
                                    : switch (info) {
                                        case "docs" -> toMessage(createDocsEmbed(plugin));
                                        case "download" -> toMessage(createDownloadEmbed(plugin));
                                        case "discord" -> createInviteMessage(plugin);
                                        default -> toMessage(createOverviewEmbed(plugin));
                                    };
                            event.reply(response).queue();
                        },
                        () -> event.replyEmbeds(createNotFoundEmbed(name)).setEphemeral(true).queue()
                );
    }

    @Override
    public void autoComplete(@NonNull CommandAutoCompleteInteractionEvent event) {
        event.replyChoiceStrings(
                this.plugins.stream()
                        .map(Plugin::getName)
                        .filter(name -> name.toLowerCase().contains(event.getFocusedOption().getValue().toLowerCase()))
                        .limit(25)
                        .toList()
        ).queue();
    }

    private @NonNull Message createInviteMessage(@NonNull Plugin plugin) {
        if (plugin.getDiscordInviteUrl() == null) {
            return toMessage(EmbedUtil.createErrorEmbed("Could not find an invite link for " + plugin.getName()));
        }
        return new MessageBuilder(plugin.getDiscordInviteUrl()).build();
    }

    private static @NonNull MessageEmbed createOverviewEmbed(@NonNull Plugin plugin) {
        EmbedBuilder response = new EmbedBuilder()
                .setColor(GREEN)
                .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f419.png")
                .setTitle(plugin.getName(), plugin.getResourceUrl())
                .setDescription(plugin.getDescription());
        if (plugin.getDownloadUrl() != null) {
            response.addField("Download Link", plugin.getDownloadUrl(), false);
        }
        if (plugin.getDocsUrl() != null) {
            response.addField("Documentation", plugin.getDocsUrl(), true);
        }
        if (plugin.getRepositoryUrl() != null) {
            response.addField("Repository", plugin.getRepositoryUrl(), true);
        }
        if (plugin.getDependencies() != null) {
            response.addField("Dependencies", plugin.getDependencies(), false);
        }
        if (plugin.getDiscordInviteUrl() != null) {
            response.addField("Discord", plugin.getDiscordInviteUrl(), false);
        }
        return response.build();
    }

    private static @NonNull MessageEmbed createDocsEmbed(@NonNull Plugin plugin) {
        if (plugin.getDocsUrl() == null) {
            return EmbedUtil.createErrorEmbed("Could not find a documentation link for " + plugin.getName());
        }
        return new EmbedBuilder()
                .setColor(GREEN)
                .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f989.png")
                .setTitle(plugin.getName() + " Documentation", plugin.getResourceUrl())
                .setDescription("The latest documentation for **" + plugin.getName() + "** can be found here:\n"
                        + plugin.getDocsUrl())
                .build();
    }

    private static @NonNull MessageEmbed createDownloadEmbed(@NonNull Plugin plugin) {
        if (plugin.getDownloadUrl() == null) {
            return EmbedUtil.createErrorEmbed("Could not find a download link for " + plugin.getName());
        }
        return new EmbedBuilder()
                .setColor(GREEN)
                .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f992.png")
                .setTitle("Download " + plugin.getName(), plugin.getResourceUrl())
                .setDescription("The latest version of **" + plugin.getName() + "** can be downloaded here:\n"
                        + plugin.getDownloadUrl())
                .build();
    }

    private static @NonNull MessageEmbed createNotFoundEmbed(@Nullable String name) {
        return EmbedUtil.createErrorEmbed("Plugin `" + name + "` not found");
    }

    private static @NonNull Message toMessage(@NonNull MessageEmbed embed) {
        return new MessageBuilder(embed).build();
    }

    @Override
    public @NonNull CommandData getData() {
        return this.data;
    }
}
