package io.github.aikovdp.RokuBot;

import io.github.aikovdp.RokuBot.module.PluginCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import org.kohsuke.github.*;

import java.io.*;
import java.util.List;

public class Main {

    public static JDA api;
    public static GitHub github;
    public static List<GHIssue> openIssues;
    public static GHRepository defaultRepo;
    public static User botOwner;
    private static final long OWNER_ID = 118004527600238593L;

    public static void main(String[] arguments) throws Exception {

        Settings.load();

        api = JDABuilder.createDefault(Settings.botToken)
                .setActivity(Activity.playing("Rokucraft"))
                .addEventListeners(new BaseCommands())
                .addEventListeners(new GHCommands())
                .addEventListeners(new PluginCommands())
                .build();

        botOwner = api.retrieveUserById(OWNER_ID).complete();


        github = new GitHubBuilder().withOAuthToken(Settings.gitHubOAuth, Settings.gitHubLogin).build();
        defaultRepo = github.getRepository(Settings.defaultRepoName);

        try {
            openIssues = defaultRepo.getIssues(GHIssueState.OPEN);
        } catch (IOException ignored) {
            // This error wil be thrown if there are no open issues, so the List can remain empty
        }
    }
}
