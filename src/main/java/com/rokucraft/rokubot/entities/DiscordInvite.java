package com.rokucraft.rokubot.entities;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record DiscordInvite(
        @Required String name,
        @Required String inviteCode
) {
    public @NonNull String inviteUrl() {
        return "https://discord.gg/" + inviteCode;
    }
}
