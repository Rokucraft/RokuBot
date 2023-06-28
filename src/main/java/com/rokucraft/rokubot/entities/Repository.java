package com.rokucraft.rokubot.entities;

import org.kohsuke.github.GHRepository;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record Repository(
        @Required String name,
        @Required String repositoryUrl
) {
    public static Repository of(GHRepository repo) {
        return new Repository(repo.getName(), repo.getHtmlUrl().toString());
    }
}
