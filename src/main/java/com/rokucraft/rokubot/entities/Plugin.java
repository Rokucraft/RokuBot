package com.rokucraft.rokubot.entities;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

import java.util.List;

@ConfigSerializable
public record Plugin(
        @Required String name,
        @Nullable String resourceUrl,
        @Nullable String description,
        @Nullable String downloadUrl,
        @Nullable String docsUrl,
        @Nullable String repositoryUrl,
        @Nullable String discordInviteCode,
        @NonNull List<String> dependencies
) {

    public @Nullable String getDiscordInviteUrl() {
        if (discordInviteCode == null)
            return null;
        return "https://discord.gg/" + discordInviteCode;
    }

    public @Nullable String getDependenciesAsString() {
        if (!dependencies.isEmpty()) {
            return String.join(", ", dependencies);
        } else return null;
    }
}
