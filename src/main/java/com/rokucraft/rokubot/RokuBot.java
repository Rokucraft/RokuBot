package com.rokucraft.rokubot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.rokucraft.rokubot.commands.*;
import com.rokucraft.rokubot.config.Settings;
import com.rokucraft.rokubot.listeners.GuildVoiceListener;
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
    public static JDA jda;
    public static GitHub github;
    public static GHRepository defaultRepo;
    public static User botOwner;
    private static Settings config;

    public static void main(String[] arguments) throws Exception {
        loadSettings();

        EventWaiter waiter = new EventWaiter();

        jda = JDABuilder.createDefault(config.botToken)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(waiter)
                .addEventListeners(new BaseCommands(waiter))
                .addEventListeners(new GHCommands())
                .addEventListeners(new PluginCommands())
                .addEventListeners(new GuildVoiceListener())
                .addEventListeners(new JoinListener())
                .addEventListeners(
                        new SlashCommandListener(new RuleCommand(), new InviteCommand())
                                .addCommands(config.slashMessageCommandList)
                ).build();

        if (config.botActivity != null) {
            jda.getPresence().setActivity(Activity.playing(config.botActivity));
        }

        botOwner = jda.retrieveUserById(Constants.OWNER_ID).complete();

        github = new GitHubBuilder().withOAuthToken(config.githubOAuth, config.githubLogin).build();
        defaultRepo = github.getRepository(config.defaultRepoName);
    }

    public static Settings getConfig() {
        return config;
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
}
