package io.github.aikovdp.RokuBot.commands;

import io.github.aikovdp.RokuBot.Settings;
import io.github.aikovdp.RokuBot.entities.DiscordInvite;
import io.github.aikovdp.RokuBot.entities.Plugin;
import io.github.aikovdp.RokuBot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


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
                response = new EmbedBuilder();
                response.setColor(0xFF7F00);
                response.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f419.png");
                response.setFooter("Plugin Info", event.getAuthor().getAvatarUrl());

                response.setTitle(plugin.getName(), plugin.getResourceUrl());
                if (plugin.getDescription() != null) {
                    response.setDescription(plugin.getDescription());
                }
                if (plugin.getCurrentVersion() != null){
                    response.addField("Current Version", plugin.getCurrentVersion(), true);

                    if (plugin.getCurrentVersion().equals(plugin.getLatestVersion())) {response.setColor(0x00ff00);}
                }
                if (plugin.getLatestVersion() != null) {
                    response.addField("Latest Version", plugin.getLatestVersion(), true);
                }
                if (plugin.getDownloadUrl() != null) {
                    response.addField("Download Link", plugin.getDownloadUrl(), false);
                }
                if (plugin.getDocsUrl() != null) {
                    response.addField("Documentation", plugin.getDocsUrl(), true);
                }
                if (plugin.getDependencies() != null) {
                    response.addField("Dependencies", plugin.getDependencies(), false);
                }
                if (plugin.getDiscordInviteCode() != null) {
                    response.addField("Discord", DiscordInvite.find(plugin.getName()).getInviteUrl(), false);
                }

            } else {
                response = EmbedUtil.createErrorEmbed();
                response.addField("❌ Plugin not found",
                        "**Usage: **`" + args[0] + " <name>`", true);
            }
        }

        if(content.startsWith(Settings.prefix + "version")) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            Plugin plugin = Plugin.find(args[1]);
            if (plugin != null && (plugin.getLatestVersion() != null || plugin.getCurrentVersion() != null)) {
                response = new EmbedBuilder();
                response.setColor(0xFF7F00);
                response.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f98e.png");
                response.setFooter("Plugin Version", event.getAuthor().getAvatarUrl());

                response.setTitle(plugin.getName() + " Version", plugin.getResourceUrl());
                if (plugin.getCurrentVersion() != null){
                    response.addField("Current Version", plugin.getCurrentVersion(), true);
                    if (plugin.getCurrentVersion().equals(plugin.getLatestVersion())) {response.setColor(0x00ff00);}
                }
                if (plugin.getLatestVersion() != null) {
                    response.addField("Latest Version", plugin.getLatestVersion(), true);
                }
            } else {
                response = EmbedUtil.createErrorEmbed();
                if (plugin == null) {
                    response.addField("❌ Plugin not found",
                            "**Usage: **`" + args[0] + " <name>`", true);
                } else {
                    response.setDescription("❌ Could not find versions for " + plugin.getName());
                }
            }
        }

        if(content.startsWith(Settings.prefix + "update") || content.startsWith(Settings.prefix + "download")) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            Plugin plugin = Plugin.find(args[1]);
            if (plugin != null && plugin.getDownloadUrl() != null) {
                response = new EmbedBuilder();
                response.setColor(0x00ff00);
                response.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f992.png");
                response.setTitle("Download " + plugin.getName(), plugin.getResourceUrl());
                response.setDescription("The latest version of **" + plugin.getName() + "** can be downloaded here:\n"
                        + plugin.getDownloadUrl());
                response.setFooter("Plugin Download Link", event.getAuthor().getAvatarUrl());
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

        if(content.startsWith(Settings.prefix + "docs") || content.startsWith(Settings.prefix + "wiki")) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            Plugin plugin = Plugin.find(args[1]);
            if (plugin != null && plugin.getDocsUrl() != null) {
                response = new EmbedBuilder();
                response.setColor(0x00ff00);
                response.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f989.png");
                response.setTitle(plugin.getName() + " Documentation", plugin.getResourceUrl());
                response.setDescription("The latest documentation for **" + plugin.getName() + "** can be found here:\n"
                        + plugin.getDocsUrl());
                response.setFooter("Plugin Documentation Link", event.getAuthor().getAvatarUrl());
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
                response = new EmbedBuilder();
                response.setColor(0x00ff00);
                response.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f9a5.png");
                response.setTitle(plugin.getName() + " Dependencies", plugin.getResourceUrl());
                response.setDescription("The dependencies for **" + plugin.getName() + "** are:\n"
                        + plugin.getDependencies());
                response.setFooter("Plugin Dependencies", event.getAuthor().getAvatarUrl());
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
