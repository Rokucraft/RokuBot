package com.rokucraft.RokuBot;

import com.rokucraft.RokuBot.entities.*;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Settings {

    private static BasicConfigurationNode root;

    public static String botToken;
    public static String botActivity;
    public static String prefix;
    public static List<String> staffCategoryIDs;
    public static String gitHubLogin;
    public static String gitHubOAuth;
    public static String defaultRepoName;
    public static List<TextCommand> textCommandList;
    public static List<DiscordInvite> discordInviteList;
    public static List<Plugin> pluginList;
    public static List<Repository> repositoryList;

    public static void load() {
        File settingsFile = new File("settings.json").getAbsoluteFile();

        final JacksonConfigurationLoader loader = JacksonConfigurationLoader.builder().path(settingsFile.toPath()).build();

        try {
            if (settingsFile.exists()) {
                root = loader.load();
            } else {
                settingsFile.getParentFile().mkdirs();
                settingsFile.createNewFile();

            }
            botToken = root.node("botToken").getString();
            prefix = root.node("prefix").getString("!");
            botActivity = root.node("botActivity").getString("Rokucraft");
            staffCategoryIDs = root.node("staffCategoryIDs").getList(String.class);
            gitHubLogin = root.node("gitHubLogin").getString();
            gitHubOAuth = root.node("gitHubOAuth").getString();
            defaultRepoName = root.node("repositories", "default").getString();

        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }
    }

    public static void loadTextCommands() {
        textCommandList = loadEntities("textcommands.json", TextCommand.class);
    }

    public static void loadDiscordInvites() {
        discordInviteList = loadEntities("discordinvites.json", DiscordInvite.class);
    }

    public static void loadRepositories() {
        repositoryList = loadEntities("repositories.json", Repository.class);
    }

    public static void loadPlugins() {
        pluginList = loadEntities("plugins.json", Plugin.class);
        for (Plugin plugin : pluginList) {
            if (plugin.getDiscordInviteCode() != null) {
                new DiscordInvite(plugin.getName(), plugin.getAliases(), true, plugin.getDiscordInviteCode());
            }
            if (plugin.getRepositoryUrl() != null) {
                new Repository(plugin.getName(), plugin.getAliases(), true, plugin.getRepositoryUrl());
            }
        }
    }

    private static <T extends AbstractEntity> List<T> loadEntities(String pathname, Class<T> tClass) {
        File entitiesFile = new File(pathname).getAbsoluteFile();

        final JacksonConfigurationLoader loader = JacksonConfigurationLoader.builder().path(entitiesFile.toPath()).build();

        try {
            if (entitiesFile.exists()) {
                root = loader.load();
            } else {
                entitiesFile.getParentFile().mkdirs();
                entitiesFile.createNewFile();
            }
            return root.getList(tClass);

        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
        }
        return null;
    }
}
