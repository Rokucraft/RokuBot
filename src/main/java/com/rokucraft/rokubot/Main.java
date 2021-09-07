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
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.List;

public class Main {

    public static JDA jda;
    public static GitHub github;
    public static List<GHIssue> openIssues;
    public static GHRepository defaultRepo;
    public static User botOwner;
    private static final long OWNER_ID = 118004527600238593L;

    public static void main(String[] arguments) throws Exception {

        Settings.load();
        Settings.loadTextCommands();
        Settings.loadDiscordInvites();
        Settings.loadRepositories();
        Settings.loadPlugins();
        Settings.loadMarkdownSections();
        Settings.loadRules();
        Settings.loadSlashMessageCommands();
        Settings.loadWelcomeEmbeds();

        EventWaiter waiter = new EventWaiter();

        jda = JDABuilder.createDefault(Settings.botToken)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.playing("Rokucraft"))
                .addEventListeners(waiter)
                .addEventListeners(new BaseCommands(waiter))
                .addEventListeners(new GHCommands())
                .addEventListeners(new PluginCommands())
                .addEventListeners(new GuildVoiceListener())
                .addEventListeners(new JoinListener())
                .addEventListeners(
                        new SlashCommandListener(new RuleCommand(), new InviteCommand())
                                .addCommands(Settings.slashMessageCommandList)
                ).build();

        botOwner = jda.retrieveUserById(OWNER_ID).complete();

        github = new GitHubBuilder().withOAuthToken(Settings.gitHubOAuth, Settings.gitHubLogin).build();
        defaultRepo = github.getRepository(Settings.defaultRepoName);

        try {
            openIssues = defaultRepo.getIssues(GHIssueState.OPEN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
