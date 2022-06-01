package com.rokucraft.rokubot.listeners;

import com.rokucraft.rokubot.RokuBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildVoiceListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        Guild guild = event.getGuild();
        String roleId = RokuBot.getConfig().getVoiceChannelRoleMap().get(guild.getId());
        if (roleId == null) return;
        Role role = guild.getRoleById(roleId);
        if (role == null) return;
        guild.addRoleToMember(event.getMember(), role).queue();
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        Guild guild = event.getGuild();
        String roleId = RokuBot.getConfig().getVoiceChannelRoleMap().get(guild.getId());
        if (roleId == null) return;
        Role role = guild.getRoleById(roleId);
        if (role == null) return;
        guild.removeRoleFromMember(event.getMember(), role).queue();
    }
}
