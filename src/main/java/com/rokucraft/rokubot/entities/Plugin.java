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
    public @Nullable String discordInviteUrl() {
        if (this.discordInviteCode == null)
            return null;
        return "https://discord.gg/" + this.discordInviteCode;
    }

    public @Nullable DiscordInvite discordInvite() {
        if (this.discordInviteCode == null)
            return null;
        return new DiscordInvite(this.name, this.discordInviteCode);
    }

    public @Nullable Repository repository() {
        if (this.repositoryUrl == null)
            return null;
        return new Repository(this.name, this.repositoryUrl);
    }
}
