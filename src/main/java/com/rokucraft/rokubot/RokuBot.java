package com.rokucraft.rokubot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.rokucraft.rokubot.commands.*;
import com.rokucraft.rokubot.commands.PluginCommand;
import com.rokucraft.rokubot.config.Settings;
import com.rokucraft.rokubot.listeners.JoinListener;
import com.rokucraft.rokubot.listeners.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

public class RokuBot {
    private static JDA jda;
    private static GitHub github;
    private static GHRepository defaultRepo;
    private static User botOwner;
    private static Settings config;

    public static void main(String[] arguments) throws Exception {
        loadSettings();

        EventWaiter waiter = new EventWaiter();

        jda = JDABuilder.createDefault(config.getBotToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(waiter)
                .addEventListeners(new BaseCommands(waiter))
                .addEventListeners(new GHCommands())
                .addEventListeners(new JoinListener())
                .addEventListeners(
                        new SlashCommandListener(
                                new RuleCommand(),
                                new InviteCommand(),
                                new PluginCommand(),
                                new ReloadCommand()
                        ).addCommands(config.getSlashMessageCommands())
                ).build();

        botOwner = jda.retrieveUserById(Constants.OWNER_ID).complete();
    }

    public static void loadSettings() {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of("settings.yml"))
                .build();

        try {
            CommentedConfigurationNode root = loader.load();
            config = root.get(Settings.class);
        } catch (IOException e) {
            System.err.println("An error occurred while loading settings: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);

        }
    }

    public static void applySettings() {
        if (config.getBotActivity() != null) {
            jda.getPresence().setActivity(Activity.playing(config.getBotActivity()));
        }

        if (config.getGithubToken() == null) return;

        try {
            github = new GitHubBuilder().withOAuthToken(config.getGithubToken()).build();
            defaultRepo = github.getRepository(config.getDefaultRepoName());
        } catch (IOException e) {
            System.err.println("An error occurred while loading settings: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }
    }

    public static void reloadSettings() {
        loadSettings();
        applySettings();
    }

    public static Settings getConfig() {
        return config;
    }

    public static GitHub getGithub() {
        return github;
    }

    public static GHRepository getDefaultRepo() {
        return defaultRepo;
    }

    public static User getBotOwner() {
        return botOwner;
    }
}
