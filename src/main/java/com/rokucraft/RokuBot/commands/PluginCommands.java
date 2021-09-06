package com.rokucraft.RokuBot.commands;

import com.rokucraft.RokuBot.config.Settings;
import com.rokucraft.RokuBot.entities.DiscordInvite;
import com.rokucraft.RokuBot.entities.Plugin;
import com.rokucraft.RokuBot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static com.rokucraft.RokuBot.Constants.GREEN;


public class PluginCommands extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        if (!Settings.staffCategoryIDs.contains(message.getCategory().getId())) return;
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        EmbedBuilder response = null;


        if(content.startsWith(Settings.prefix + "plugin")) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            Plugin plugin = Plugin.find(args[1]);
            if (plugin != null) {
                response = new EmbedBuilder()
                        .setColor(GREEN)
                        .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f419.png")
                        .setFooter("Plugin Info", event.getAuthor().getAvatarUrl())
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
                if (plugin.getDiscordInviteCode() != null) {
                    response.addField("Discord", DiscordInvite.find(plugin.getName()).getInviteUrl(), false);
                }
            } else {
                response = EmbedUtil.createErrorEmbed()
                        .addField("❌ Plugin not found",
                        "**Usage: **`" + args[0] + " <name>`", true);
            }
        }

        if(content.startsWith(Settings.prefix + "download")) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            Plugin plugin = Plugin.find(args[1]);
            if (plugin != null && plugin.getDownloadUrl() != null) {
                response = new EmbedBuilder()
                        .setColor(GREEN)
                        .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f992.png")
                        .setTitle("Download " + plugin.getName(), plugin.getResourceUrl())
                        .setDescription("The latest version of **" + plugin.getName() + "** can be downloaded here:\n"
                                + plugin.getDownloadUrl())
                        .setFooter("Plugin Download Link", event.getAuthor().getAvatarUrl());
            } else {
                response = EmbedUtil.createErrorEmbed();
                if (plugin == null) {
                    response.addField("❌ Plugin not found",
                            "**Usage: **`" + args[0] + " <name>`", true);
                } else {
                    response.setDescription("❌ Could not find download link for " + plugin.getName());
                }
            }
        }

        if(content.startsWith(Settings.prefix + "docs")) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            Plugin plugin = Plugin.find(args[1]);
            if (plugin != null && plugin.getDocsUrl() != null) {
                response = new EmbedBuilder()
                        .setColor(GREEN)
                        .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f989.png")
                        .setTitle(plugin.getName() + " Documentation", plugin.getResourceUrl())
                        .setDescription("The latest documentation for **" + plugin.getName() + "** can be found here:\n"
                                + plugin.getDocsUrl())
                        .setFooter("Plugin Documentation Link", event.getAuthor().getAvatarUrl());
            } else {
                response = EmbedUtil.createErrorEmbed();
                if (plugin == null) {
                    response.addField("❌ Plugin not found",
                            "**Usage: **`" + args[0] + " <name>`", true);
                } else {
                    response.setDescription("❌ Could not find documentation link for " + plugin.getName());
                }
            }
        }

        if(content.startsWith(Settings.prefix + "dependencies")) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            Plugin plugin = Plugin.find(args[1]);
            if (plugin != null && plugin.getDependencies() != null) {
                response = new EmbedBuilder()
                        .setColor(GREEN)
                        .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f9a5.png")
                        .setTitle(plugin.getName() + " Dependencies", plugin.getResourceUrl())
                        .setDescription("The dependencies for **" + plugin.getName() + "** are:\n"
                                + plugin.getDependencies())
                        .setFooter("Plugin Dependencies", event.getAuthor().getAvatarUrl());
            } else {
                response = EmbedUtil.createErrorEmbed();
                if (plugin == null) {
                    response.addField("❌ Plugin not found",
                            "**Usage: **`" + args[0] + " <name>`", true);
                } else {
                    response.setDescription("❌ No known dependencies for " + plugin.getName());
                }
            }
        }

        if (response != null) {
            channel.sendMessage(response.build()).queue();
            response.clear();
        }
    }
}
