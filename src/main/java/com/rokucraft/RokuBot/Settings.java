package com.rokucraft.RokuBot;

import com.rokucraft.RokuBot.commands.SlashMessageCommand;
import com.rokucraft.RokuBot.entities.*;
import com.rokucraft.RokuBot.serializers.ButtonSerializer;
import com.rokucraft.RokuBot.serializers.MessageEmbedSerializer;
import com.rokucraft.RokuBot.serializers.MessageSerializer;
import com.rokucraft.RokuBot.serializers.SlashMessageCommandSerializer;
import io.leangen.geantyref.TypeToken;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Settings {

    private static CommentedConfigurationNode root;

    public static String botToken;
    public static String botActivity;
    public static String prefix;
    public static List<String> staffCategoryIDs;
    public static String gitHubLogin;
    public static String gitHubOAuth;
    public static String defaultRepoName;
    public static String rulesFooter;
    public static List<TextCommand> textCommandList;
    public static List<DiscordInvite> discordInviteList;
    public static List<Plugin> pluginList;
    public static List<Repository> repositoryList;
    public static List<MarkdownSection> markdownSectionList;
    public static List<Rule> rulesList;
    public static List<SlashMessageCommand> slashMessageCommandList;
    public static Map<String, String> voiceChannelRoleMap;

    public static void load() {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of("settings.yml"))
                .build();

        try {
            root = loader.load();

            botToken = root.node("botToken").getString();
            prefix = root.node("prefix").getString("!");
            botActivity = root.node("botActivity").getString("Rokucraft");
            staffCategoryIDs = root.node("staffCategoryIDs").getList(String.class);
            gitHubLogin = root.node("gitHubLogin").getString();
            gitHubOAuth = root.node("gitHubOAuth").getString();
            defaultRepoName = root.node("defaultRepository").getString();
            rulesFooter = root.node("rulesFooter").getString();
            voiceChannelRoleMap = root.node("voice-channel-roles").get(new TypeToken<Map<String, String>>(){});
        } catch (IOException e) {
            System.err.println("An error occurred while loading settings: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }
    }

    public static void loadTextCommands() {
        textCommandList = loadEntities("text-commands", TextCommand.class);
    }

    public static void loadDiscordInvites() {
        discordInviteList = loadEntities("discord-invites", DiscordInvite.class);
    }

    public static void loadRepositories() {
        repositoryList = loadEntities("repositories", Repository.class);
    }

    public static void loadPlugins() {
        pluginList = loadEntities("plugins", Plugin.class);
        if (pluginList != null) {
            for (Plugin plugin : pluginList) {
                if (plugin.getDiscordInviteCode() != null) {
                    new DiscordInvite(plugin.getName(), plugin.getAliases(), true, plugin.getDiscordInviteCode());
                }
                if (plugin.getRepositoryUrl() != null) {
                    new Repository(plugin.getName(), plugin.getAliases(), true, plugin.getRepositoryUrl());
                }
            }
        }
    }

    public static void loadMarkdownSections() {
        markdownSectionList = loadEntities("markdown-sections", MarkdownSection.class);
    }

    public static void loadRules() {
        rulesList = loadEntities("rules", Rule.class);
    }

    public static void loadSlashMessageCommands() {
        slashMessageCommandList = loadEntities("slash-message-commands", SlashMessageCommand.class);
    }

    private static <T> List<T> loadEntities(String entitytype, Class<T> tClass) {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of(entitytype + ".yml"))
                .defaultOptions(options -> options.serializers(
                        builder -> builder.register(Message.class, MessageSerializer.INSTANCE)
                                .register(MessageEmbed.class, MessageEmbedSerializer.INSTANCE)
                                .register(SlashMessageCommand.class, SlashMessageCommandSerializer.INSTANCE)
                                .register(Button.class, ButtonSerializer.INSTANCE)
                )).build();

        try {
            root = loader.load();
            return root.node(entitytype).getList(tClass);
        } catch (IOException e) {
            System.err.println("An error occurred while loading " + entitytype + ": " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            return null;
        }
    }
}
