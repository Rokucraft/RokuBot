package com.rokucraft.rokubot;

import com.rokucraft.rokubot.command.CommandManager;
import com.rokucraft.rokubot.command.commands.*;
import com.rokucraft.rokubot.config.Config;
import com.rokucraft.rokubot.config.RecordConfigurationLoader;
import com.rokucraft.rokubot.entities.DiscordInvite;
import com.rokucraft.rokubot.entities.Repository;
import com.rokucraft.rokubot.entities.Tag;
import com.rokucraft.rokubot.listeners.JoinListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.authorization.OrgAppInstallationAuthorizationProvider;
import org.kohsuke.github.extras.authorization.JWTTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class RokuBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(RokuBot.class);
    private final JDA jda;
    private GitHub github;
    private final RecordConfigurationLoader configLoader;
    private Config config;
    private List<GHRepository> repositoryCache = new ArrayList<>();
    private CommandManager commandManager;
    private JoinListener joinListener;

    public static void main(String[] arguments) {
        new RokuBot();
    }

    private RokuBot() {
        this.configLoader = new RecordConfigurationLoader();
        try {
            loadSettings();
        } catch (ConfigurateException e) {
            LOGGER.error("An error occurred while loading settings:", e);
            System.exit(1);
        }

        this.jda = JDABuilder.createDefault(this.config.botToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        applySettings();
    }

    public void loadSettings() throws ConfigurateException {
        this.config = this.configLoader.load(Config.class);
    }

    public void applySettings() {
        if (this.config.botActivity() != null) {
            this.jda.getPresence().setActivity(Activity.playing(this.config.botActivity()));
        }
        if (this.joinListener != null) {
            this.jda.removeEventListener(this.joinListener);
        }
        this.joinListener = new JoinListener(this.config.welcomeChannelMap(), this.config.welcomeEmbeds());
        this.jda.addEventListener(this.joinListener);

        initGitHub();
        initCommands();
    }

    private void initCommands() {
        if (this.commandManager == null) {
            this.commandManager = new CommandManager(this.jda);
        } else {
            this.commandManager.clearAll();
        }
        CommandManager commandManager = new CommandManager(this.jda);
        commandManager.addCommands(
                new RuleCommand(this.config.rules(), this.config.rulesFooter()),
                new RollCommand()
        );

        List<DiscordInvite> publicInvites = this.config.publicInvites();

        if (!publicInvites.isEmpty()) {
            commandManager.addCommands(new InviteCommand("invite", publicInvites, publicInvites.get(0), true));
        }
        commandManager.addCommands(this.config.rootTagCommands().stream().map(RootTagCommand::new).toList());

        List<DiscordInvite> privateInvites = this.config.privateInvites();

        List<Repository> repositories = this.config.repositories();

        List<Tag> tags = new ArrayList<>(this.config.privateTags());
        tags.addAll(this.config.markdownSections().stream()
                .map(section -> {
                    try {
                        return section.toTag(this.github);
                    } catch (IOException e) {
                        LOGGER.error("Unable to get contents for " + section, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList());

        try {
            this.jda.awaitReady();
            this.config.trustedServerIds().stream()
                    .map(this.jda::getGuildById)
                    .filter(Objects::nonNull)
                    .forEach(guild -> {
                                commandManager.addGuildCommands(guild,
                                        new PluginCommand(this.config.plugins()),
                                        new ReloadCommand(this),
                                        new IssueCommand(this.github, this.repositoryCache, this.config.defaultRepoName()),
                                        new TagCommand(tags)
                                );
                                if (!privateInvites.isEmpty()) {
                                    commandManager.addGuildCommands(
                                            guild,
                                            new InviteCommand("discord", privateInvites, null, false)
                                    );
                                }

                                if (!repositories.isEmpty()) {
                                    commandManager.addGuildCommands(
                                            guild,
                                            new RepoCommand(repositories, repositories.get(0))
                                    );
                                }
                            }
                    );
        } catch (InterruptedException e) {
            LOGGER.error("Thread was interrupted while waiting for JDA to be ready", e);
        }
        commandManager.registerCommands();
    }

    private void initGitHub() {
        if (this.config.githubAppId() == null || this.config.githubOrganization() == null) return;

        try {
            var jwtAuth = new JWTTokenProvider(this.config.githubAppId(), Path.of("github-app.private-key.pem"));
            var orgAppAuth = new OrgAppInstallationAuthorizationProvider(this.config.githubOrganization(), jwtAuth);
            this.github = new GitHubBuilder().withAuthorizationProvider(orgAppAuth).build();

            this.repositoryCache = this.github.getOrganization(this.config.githubOrganization())
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
            LOGGER.error("An error occurred while loading GitHub settings", e);
        }
    }

    public void reloadSettings() throws ConfigurateException {
        loadSettings();
        applySettings();
        LOGGER.info("The bot has been reloaded.");
    }
}
