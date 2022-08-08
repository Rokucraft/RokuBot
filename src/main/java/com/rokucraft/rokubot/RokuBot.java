package com.rokucraft.rokubot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.rokucraft.rokubot.commands.*;
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
import org.kohsuke.github.authorization.OrgAppInstallationAuthorizationProvider;
import org.kohsuke.github.extras.authorization.JWTTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RokuBot {
    private static final Logger logger = LoggerFactory.getLogger(RokuBot.class);
    private static JDA jda;
    private static GitHub github;
    private static GHRepository defaultRepo;
    private static User botOwner;
    private static Settings config;
    private static List<GHRepository> repositoryCache = new ArrayList<>();

    public static void main(String[] arguments) throws Exception {
        loadSettings();

        EventWaiter waiter = new EventWaiter();

        jda = JDABuilder.createDefault(config.getBotToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(waiter)
                .addEventListeners(new BaseCommands(waiter))
                .addEventListeners(new GHCommands())
                .addEventListeners(new JoinListener())
                .addEventListeners(
                        new SlashCommandListener(
                                new RuleCommand(),
                                new InviteCommand(),
                                new PluginCommand(),
                                new ReloadCommand(),
                                new IssueCommand()
                        ).addCommands(config.getSlashMessageCommands())
                ).build();

        botOwner = jda.retrieveUserById(Constants.OWNER_ID).complete();
        applySettings();
    }

    public static void loadSettings() {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of("settings.yml"))
                .build();

        try {
            CommentedConfigurationNode root = loader.load();
            config = root.get(Settings.class);
        } catch (IOException e) {
            logger.error("An error occurred while loading settings:", e);
            System.exit(1);
        }
    }

    public static void applySettings() {
        if (config.getBotActivity() != null) {
            jda.getPresence().setActivity(Activity.playing(config.getBotActivity()));
        }

        if (config.getGithubAppId() == null || config.getGithubOrganization() == null) return;

        try {
            var jwtAuth = new JWTTokenProvider(config.getGithubAppId(), Path.of("github-app.private-key.pem"));
            var orgAppAuth = new OrgAppInstallationAuthorizationProvider(config.getGithubOrganization(), jwtAuth);
            github = new GitHubBuilder().withAuthorizationProvider(orgAppAuth).build();
            defaultRepo = github.getRepository(config.getDefaultRepoName());

            repositoryCache = github.getOrganization(config.getGithubOrganization())
                    .listRepositories().toList().stream()
                    .sorted(Comparator.comparing((GHRepository r) -> {
                        try {
                            return r.getUpdatedAt();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).reversed())
                    .toList();
        } catch (IOException | GeneralSecurityException | RuntimeException e) {
            logger.error("An error occurred while loading GitHub settings", e);
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

    public static List<GHRepository> getRepositoryCache() {
        return repositoryCache;
    }
}
