package com.rokucraft.rokubot.entities;

import com.rokucraft.rokubot.config.Settings;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Repository extends AbstractEntity{
    private String repositoryUrl;

    public Repository() {}

    public Repository(String name, String[] aliases, boolean staffOnly, String repositoryUrl) {
        this.name = name;
        this.aliases = aliases;
        this.staffOnly = staffOnly;
        this.repositoryUrl = repositoryUrl;
        Settings.repositoryList.add(this);
    }

    @Nullable
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    @Nullable
    public static Repository find(String name) {
        return (Repository) find(name, Settings.repositoryList);
    }
}
