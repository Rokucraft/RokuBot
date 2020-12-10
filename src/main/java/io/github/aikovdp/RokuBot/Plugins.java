package io.github.aikovdp.RokuBot;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Plugins {
    public static String getPluginData(String pluginName, String key) {
        try {
            JSONArray pluginList = new JSONArray(new String(Files.readAllBytes(Paths.get("plugins.json"))));
            for (Object plugin : pluginList) {
                JSONObject jsonPlugin = (JSONObject) plugin;
                if(jsonPlugin.get("name").toString().equalsIgnoreCase(pluginName)){
                    return jsonPlugin.get(key).toString();
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
