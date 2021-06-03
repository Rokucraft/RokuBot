package com.rokucraft.RokuBot;

import com.rokucraft.RokuBot.entities.*;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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

    private static <T extends AbstractEntity> List<T> loadEntities(String entitytype, Class<T> tClass) {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of(entitytype + ".yml"))
                .build();

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
