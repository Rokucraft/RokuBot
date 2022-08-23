package com.rokucraft.rokubot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.rokucraft.rokubot.command.CommandManager;
import com.rokucraft.rokubot.command.commands.*;
import com.rokucraft.rokubot.command.legacy.BaseCommands;
import com.rokucraft.rokubot.command.legacy.GHCommands;
import com.rokucraft.rokubot.config.Settings;
import com.rokucraft.rokubot.entities.Tag;
import com.rokucraft.rokubot.listeners.JoinListener;
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

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class RokuBot {
    private static final Logger logger = LoggerFactory.getLogger(RokuBot.class);
    private static JDA jda;
    private static GitHub github;
    private static User botOwner;
    private static Settings config;
    private static List<GHRepository> repositoryCache = new ArrayList<>();
    private static CommandManager commandManager;

    public static void main(String[] arguments) throws LoginException {
        loadSettings();

        EventWaiter waiter = new EventWaiter();

        jda = JDABuilder.createDefault(config.getBotToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(waiter)
                .addEventListeners(new BaseCommands(waiter))
                .addEventListeners(new GHCommands())
                .addEventListeners(new JoinListener())
                .build();

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

        initGitHub();
        initCommands();
    }

    private static void initCommands() {
        if (commandManager == null) {
            commandManager = new CommandManager(jda);
        } else {
            commandManager.clearAll();
        }
        CommandManager commandManager = new CommandManager(jda);
        commandManager.addCommands(new RuleCommand(config.getRules(), config.getRulesFooter()), new InviteCommand());
        commandManager.addCommands(config.getRootTags().stream().map(RootTagCommand::new).toList());

        List<Tag> tags = new ArrayList<>(config.getPrivateTags());
        tags.addAll(config.getMarkdownSections().stream()
                .map(section -> {
                    try {
                        return section.toTag(github);
                    } catch (IOException e) {
                        logger.error("Unable to get contents for " + section, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList());

        try {
            jda.awaitReady();
            config.getTrustedServerIds().stream()
                    .map(jda::getGuildById)
                    .filter(Objects::nonNull)
                    .forEach(guild ->
                            commandManager.addGuildCommands(guild,
                                    new PluginCommand(config.getPlugins()),
                                    new ReloadCommand(),
                                    new IssueCommand(github, repositoryCache, config.getDefaultRepoName()),
                                    new TagCommand(tags)
                            )
                    );
        } catch (InterruptedException e) {
            logger.error("Thread was interrupted while waiting for JDA to be ready", e);
        }
        commandManager.registerCommands();
    }

    private static void initGitHub() {
        if (config.getGithubAppId() == null || config.getGithubOrganization() == null) return;

        try {
            var jwtAuth = new JWTTokenProvider(config.getGithubAppId(), Path.of("github-app.private-key.pem"));
            var orgAppAuth = new OrgAppInstallationAuthorizationProvider(config.getGithubOrganization(), jwtAuth);
            github = new GitHubBuilder().withAuthorizationProvider(orgAppAuth).build();

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

    public static User getBotOwner() {
        return botOwner;
    }
}
