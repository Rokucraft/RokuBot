package com.rokucraft.RokuBot.entities;

import com.rokucraft.RokuBot.Settings;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Plugin extends AbstractEntity {
    private String resourceUrl;
    private String description;
    private String currentVersion;
    private String latestVersion;
    private String downloadUrl;
    private String docsUrl;
    private String discordInviteCode;
    private String[] dependencies;

    public static Plugin find(String name) {
        return (Plugin) find(name, Settings.pluginList);
    }

    public String getDiscordInviteCode() {
        return discordInviteCode;
    }

    public String getDescription() {
        return description;
    }

    public String getDependencies() {
        if (dependencies.length != 0) {
            return String.join(", ", dependencies);
        } else return null;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getDocsUrl() {
        return docsUrl;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}
