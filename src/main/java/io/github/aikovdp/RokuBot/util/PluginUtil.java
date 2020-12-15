package io.github.aikovdp.RokuBot.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aikovdp.RokuBot.Plugin;

import java.io.*;

public class PluginUtil {
    public static Plugin getPlugin(String pluginName) throws FileNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Plugin[] pluginList = new Plugin[0];
        try {
            pluginList = mapper.readValue(new File("plugins.json"), Plugin[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Plugin plugin : pluginList) {
            if (plugin.name.equalsIgnoreCase(pluginName)) {
                return plugin;
            }
        }
        return null;
    }


}
