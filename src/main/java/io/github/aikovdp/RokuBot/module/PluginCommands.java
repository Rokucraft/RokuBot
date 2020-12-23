package io.github.aikovdp.RokuBot.module;

import io.github.aikovdp.RokuBot.Main;
import io.github.aikovdp.RokuBot.Plugin;
import io.github.aikovdp.RokuBot.Settings;
import io.github.aikovdp.RokuBot.util.EmbedUtil;
import io.github.aikovdp.RokuBot.util.PluginUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.FileNotFoundException;

public class PluginCommands extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        if (!Settings.staffCategoryIDs.contains(message.getCategory().getId())) return;
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();


        if(content.startsWith("!plugin")) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            try {
                Plugin plugin = PluginUtil.getPlugin(args[1]);
                if (plugin != null) {
                    EmbedBuilder pluginEmbed = EmbedUtil.createPluginEmbed();
                    pluginEmbed.setTitle(plugin.name);
                    pluginEmbed.addField("Download Link", plugin.downloadURL, true);
                    // pluginEmbed.addField("Documentation", plugin.docsURL, false);

                    pluginEmbed.setFooter("Plugin Info", event.getAuthor().getAvatarUrl());

                    channel.sendMessage(pluginEmbed.build()).queue();
                    pluginEmbed.clear();
                } else {
                    EmbedBuilder errorEmbed = EmbedUtil.createErrorEmbed();
                    errorEmbed.addField("❌ Plugin not found", " \n**Usage: **`" + Settings.prefix + "plugin <name>`", true);

                    channel.sendMessage(errorEmbed.build()).queue();
                    errorEmbed.clear();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
