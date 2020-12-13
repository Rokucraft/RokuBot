package io.github.aikovdp.RokuBot.util;


import com.google.gson.Gson;
import io.github.aikovdp.RokuBot.Main;
import io.github.aikovdp.RokuBot.Plugin;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.requests.RestAction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class PluginUtil {
    public static Plugin getPlugin(String pluginName) throws FileNotFoundException {
        Gson gson = new Gson();
        Reader reader = new FileReader("plugins.json");
        Plugin[] pluginList = gson.fromJson(reader, Plugin[].class);
        for (Plugin plugin : pluginList) {
            if (plugin.name.equalsIgnoreCase(pluginName)) {
                return plugin;
            }
        }
        return null;
    }

    public static String getInvite(String inviteCode) {
        RestAction<Invite> inviteRestAction = Invite.resolve(Main.api, inviteCode, false);
        Invite invite = inviteRestAction.complete();
        return invite.getUrl();
    }
}
