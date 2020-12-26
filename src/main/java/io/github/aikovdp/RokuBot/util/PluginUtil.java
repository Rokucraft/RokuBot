package io.github.aikovdp.RokuBot.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aikovdp.RokuBot.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PluginUtil {
    public static Plugin getPlugin(String pluginName) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Plugin> pluginMap = new HashMap<>();
        Map<String, String> aliasMap = new HashMap<>();

        try {
            pluginMap = mapper.readValue(new File("plugins.json"), new TypeReference<HashMap<String, Plugin>>() {});

            for (Map.Entry<String, Plugin> entry : pluginMap.entrySet()) {
                String key = entry.getKey();
                Plugin plugin = entry.getValue();
                plugin.setName(key);
                aliasMap.put(key.toLowerCase(), key);
                if (plugin.getAliases() != null) {
                    for (String alias : plugin.getAliases()) {
                        aliasMap.put(alias, key);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Plugins could not be loaded!");
            e.printStackTrace();
        }


        return pluginMap.get(aliasMap.get(pluginName.toLowerCase()));
    }
}
