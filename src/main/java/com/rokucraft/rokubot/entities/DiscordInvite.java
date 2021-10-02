package com.rokucraft.rokubot.entities;

import com.rokucraft.rokubot.RokuBot;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public class DiscordInvite extends AbstractEntity{
    @Required
    private String inviteCode;

    public DiscordInvite() {}

    public DiscordInvite(String name, String[] aliases, boolean staffOnly, String inviteCode) {
        this.name = name;
        this.aliases = aliases;
        this.staffOnly = staffOnly;
        this.inviteCode = inviteCode;
    }

    @NonNull
    public String getInviteUrl() {
        return "https://discord.gg/" + inviteCode;
    }

    @Nullable
    public static DiscordInvite find(String name) {
        return (DiscordInvite) find(name, RokuBot.getConfig().discordInviteList);
    }
}
