package io.github.aikovdp.RokuBot;

import com.google.gson.Gson;
import io.github.aikovdp.RokuBot.module.PluginCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.FileReader;
import java.io.Reader;

public class Main {

    public static String prefix = "!";

    public static void main(String[] arguments) throws Exception {
        JDA api = JDABuilder.createDefault("Nzg2MTg4NDQzODcyNTI2Mzc3.X9CxCw.eDOW67G5H9mAtlPfUVOTmkMQ7OA")
                .addEventListeners(new Commands())
                .addEventListeners(new PluginCommands())
                .build();

        Gson gson = new Gson();
        Reader reader = new FileReader("plugins.json");
        Plugin[] pluginList = gson.fromJson(reader, Plugin[].class);

        for (Plugin plugin : pluginList) {
            if (plugin.name.equalsIgnoreCase("AdvancedBan")) {
                System.out.println("Name: " + plugin.name);
                System.out.println("Download: " + plugin.downloadURL);
                System.out.println("Docs: " + plugin.docsURL);
            }
        }

    }


}
