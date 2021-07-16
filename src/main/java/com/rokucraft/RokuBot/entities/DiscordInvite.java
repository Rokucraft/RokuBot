package com.rokucraft.RokuBot.entities;

import com.rokucraft.RokuBot.Settings;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class DiscordInvite extends AbstractEntity{
    private String inviteCode;

    public DiscordInvite() {}

    public DiscordInvite(String name, String[] aliases, boolean staffOnly, String inviteCode) {
        this.name = name;
        this.aliases = aliases;
        this.staffOnly = staffOnly;
        this.inviteCode = inviteCode;
        Settings.discordInviteList.add(this);
    }

    @Nullable
    public String getInviteUrl() {
        if (inviteCode != null && !inviteCode.isEmpty()) {
            return "https://discord.gg/" + inviteCode;
        } else return null;
    }

    @Nullable
    public static DiscordInvite find(String name) {
        return (DiscordInvite) find(name, Settings.discordInviteList);
    }
}
