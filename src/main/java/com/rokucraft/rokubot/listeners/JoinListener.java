package com.rokucraft.rokubot.listeners;

import com.rokucraft.rokubot.RokuBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class JoinListener extends ListenerAdapter {

    final Random rand = new Random();
    
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Map<String, String> welcomeChannelMap = RokuBot.getConfig().getWelcomeChannelMap();
        List<MessageEmbed> welcomeEmbeds = RokuBot.getConfig().getWelcomeEmbeds();

        String id = welcomeChannelMap.get(event.getGuild().getId());
        if (id == null) return;
        TextChannel welcomeChannel = event.getGuild().getTextChannelById(id);
        if (welcomeChannel == null) return;

        MessageEmbed welcomeEmbed = welcomeEmbeds.get(this.rand.nextInt(welcomeEmbeds.size()));

        if (welcomeEmbed.getDescription() != null && welcomeEmbed.getDescription().contains("%member%")) {
            welcomeEmbed = new EmbedBuilder(welcomeEmbed)
                    .setDescription(welcomeEmbed.getDescription().replace("%member%", event.getMember().getAsMention()))
                    .build();
        }
        welcomeChannel.sendMessage("<:ds_join:884026626046828585>  Welcome, " + event.getMember().getAsMention() + "!")
                .setEmbeds(welcomeEmbed)
                .queue();
    }
}
