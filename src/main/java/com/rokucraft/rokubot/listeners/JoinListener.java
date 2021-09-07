package com.rokucraft.rokubot.listeners;

import com.rokucraft.rokubot.config.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class JoinListener extends ListenerAdapter {

    Random rand = new Random();
    
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel welcomeChannel = event.getGuild().getTextChannelById(Settings.welcomeChannelMap.get(event.getGuild().getId()));
        if (welcomeChannel == null) return;

        MessageEmbed welcomeEmbed = Settings.welcomeEmbeds.get(rand.nextInt(Settings.welcomeEmbeds.size()));

        if (welcomeEmbed.getDescription() != null && welcomeEmbed.getDescription().contains("%member%")) {
            welcomeEmbed = new EmbedBuilder(welcomeEmbed)
                    .setDescription(welcomeEmbed.getDescription().replace("%member%", event.getMember().getAsMention()))
                    .build();
        }
        welcomeChannel.sendMessage("<:ds_join:884026626046828585>  Welcome, " + event.getMember().getAsMention() + "!")
                .embed(welcomeEmbed)
                .queue();
    }
}
