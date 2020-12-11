package io.github.aikovdp.RokuBot;

import com.google.gson.Gson;
import io.github.aikovdp.RokuBot.module.PluginCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class Main {

    public static String prefix = "!";

    public static void main(String[] arguments) throws Exception {
        Properties settingsProperties = new Properties();
        Reader reader = new FileReader("settings.properties");
        settingsProperties.load(reader);
        String token = settingsProperties.getProperty("botToken");

        JDA api = JDABuilder.createDefault(token)
                .addEventListeners(new Commands())
                .addEventListeners(new PluginCommands())
                .build();
    }
}
