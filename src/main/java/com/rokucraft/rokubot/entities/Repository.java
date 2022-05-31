package com.rokucraft.rokubot.entities;

import com.rokucraft.rokubot.RokuBot;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public class Repository extends AbstractEntity{
    @Required
    private String repositoryUrl;

    public Repository() {}

    public Repository(String name, String[] aliases, boolean staffOnly, String repositoryUrl) {
        this.name = name;
        this.aliases = aliases;
        this.staffOnly = staffOnly;
        this.repositoryUrl = repositoryUrl;
    }

    @NonNull
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    @Nullable
    public static Repository find(String name) {
        return (Repository) find(name, RokuBot.getConfig().getRepositories());
    }

    @NonNull
    public static Repository getDefault() {
        Repository repository = find("default");
        if (repository == null) {
            repository = RokuBot.getConfig().getRepositories().get(0);
        }
        return repository;
    }
}
