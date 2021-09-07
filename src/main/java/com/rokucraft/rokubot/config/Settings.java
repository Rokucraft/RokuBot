package com.rokucraft.rokubot.config;

import com.rokucraft.rokubot.commands.SlashMessageCommand;
import com.rokucraft.rokubot.config.serializers.ButtonSerializer;
import com.rokucraft.rokubot.config.serializers.MessageEmbedSerializer;
import com.rokucraft.rokubot.config.serializers.MessageSerializer;
import com.rokucraft.rokubot.config.serializers.SlashMessageCommandSerializer;
import com.rokucraft.rokubot.entities.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class Settings {
    public String botToken;
    public String botActivity;
    public String prefix;
    @Setting("staff-category-ids")
    public List<String> staffCategoryIDs;
    public String githubLogin;
    @Setting("github-oauth")
    public String githubOAuth;
    public String defaultRepoName;
    public String rulesFooter;
    public Map<String, String> voiceChannelRoleMap;
    public Map<String, String> welcomeChannelMap;

    public transient List<TextCommand> textCommandList = loadEntities("text-commands", TextCommand.class);;
    public transient List<DiscordInvite> discordInviteList = loadEntities("discord-invites", DiscordInvite.class);;
    public transient List<Plugin> pluginList = loadEntities("plugins", Plugin.class);;
    public transient List<Repository> repositoryList = loadEntities("repositories", Repository.class);
    public transient List<MarkdownSection> markdownSectionList = loadEntities("markdown-sections", MarkdownSection.class);;
    public transient List<Rule> rulesList = loadEntities("rules", Rule.class);;
    public transient List<SlashMessageCommand> slashMessageCommandList = loadEntities("slash-message-commands", SlashMessageCommand.class);

    public transient List<MessageEmbed> welcomeEmbeds = loadEntities("welcome-embeds", MessageEmbed.class);;

    public Settings() {
        if (pluginList != null) {
            for (Plugin plugin : pluginList) {
                if (plugin.getDiscordInviteCode() != null) {
                    discordInviteList.add(new DiscordInvite(plugin.getName(), plugin.getAliases(), true, plugin.getDiscordInviteCode()));
                }
                if (plugin.getRepositoryUrl() != null) {
                    repositoryList.add(new Repository(plugin.getName(), plugin.getAliases(), true, plugin.getRepositoryUrl()));
                }
            }
        }
    }

    private <T> List<T> loadEntities(String entitytype, Class<T> tClass) {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of(entitytype + ".yml"))
                .defaultOptions(options -> options.serializers(
                        builder -> builder.register(Message.class, MessageSerializer.INSTANCE)
                                .register(MessageEmbed.class, MessageEmbedSerializer.INSTANCE)
                                .register(SlashMessageCommand.class, SlashMessageCommandSerializer.INSTANCE)
                                .register(Button.class, ButtonSerializer.INSTANCE)
                )).build();
        try {
            return loader.load().node(entitytype).getList(tClass);
        } catch (IOException e) {
            System.err.println("An error occurred while loading " + entitytype + ": " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            return null;
        }
    }
}
