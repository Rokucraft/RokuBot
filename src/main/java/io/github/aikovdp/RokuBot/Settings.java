package io.github.aikovdp.RokuBot;

import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Settings {

    private static BasicConfigurationNode root;

    public static String botToken;
    public static String botActivity;
    public static String prefix;
    public static List<String> staffCategoryIDs = new ArrayList<>();
    public static String gitHubLogin;
    public static String gitHubOAuth;
    public static String defaultRepoName;

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
            staffCategoryIDs = root.node("staffCategoryIDs").getList(new TypeToken<String>() {});
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
}
