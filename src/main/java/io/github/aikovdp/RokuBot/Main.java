package io.github.aikovdp.RokuBot;

import io.github.aikovdp.RokuBot.module.PluginCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Category;
import org.kohsuke.github.*;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class Main {

    public static String prefix = "!";
    public static JDA api;
    public static GitHub github;
    public static List<GHIssue> openIssues;
    public static GHRepository rokuRepo;
    public static Category[] staffCategories;

    public static void main(String[] arguments) throws Exception {
        Properties settingsProperties = new Properties();
        Reader reader = new FileReader("settings.properties");
        settingsProperties.load(reader);
        String token = settingsProperties.getProperty("botToken");

        api = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("with Amelia's feelings"))
                .addEventListeners(new Commands())
                .addEventListeners(new PluginCommands())
                .build();

        staffCategories = new Category[]{api.getCategoryById("360704564137295872"),
                api.getCategoryById("360704564137295872"),
                api.getCategoryById("545608806789414923")};

        github = GitHubBuilder.fromPropertyFile("github.properties").build();
        rokuRepo = Main.github.getRepository("Rokucraft/Rokucraft");

        try {
            openIssues = rokuRepo.getIssues(GHIssueState.OPEN);
        } catch (IOException ignored) {
            // This error wil be thrown if there are no open issues, so the List can remain empty
        }
    }
}
