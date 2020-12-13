package io.github.aikovdp.RokuBot;

import io.github.aikovdp.RokuBot.module.PluginCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.*;
import java.util.Properties;

public class Main {

    public static String prefix = "!";
    public static JDA api;
    public static GitHub github;

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

        github = GitHubBuilder.fromPropertyFile("github.properties").build();

    }
}
