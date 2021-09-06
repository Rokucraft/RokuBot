package com.rokucraft.RokuBot.entities;

import com.rokucraft.RokuBot.config.Settings;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Plugin extends AbstractEntity {
    private String resourceUrl;
    private String description;
    private String downloadUrl;
    private String docsUrl;
    private String repositoryUrl;
    private String discordInviteCode;
    private String[] dependencies;

    @Nullable
    public static Plugin find(String name) {
        return (Plugin) find(name, Settings.pluginList);
    }

    @Nullable
    public String getDiscordInviteCode() {
        return discordInviteCode;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getDependencies() {
        if (dependencies.length != 0) {
            return String.join(", ", dependencies);
        } else return null;
    }

    @Nullable
    public String getResourceUrl() {
        return resourceUrl;
    }

    @Nullable
    public String getDownloadUrl() {
        return downloadUrl;
    }

    @Nullable
    public String getDocsUrl() {
        return docsUrl;
    }

    @Nullable
    public String getRepositoryUrl() {
        return repositoryUrl;
    }
}
