package com.rokucraft.RokuBot.entities;

import com.rokucraft.RokuBot.Settings;
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

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public static Repository find(String name) {
        return (Repository) find(name, Settings.repositoryList);
    }
}
