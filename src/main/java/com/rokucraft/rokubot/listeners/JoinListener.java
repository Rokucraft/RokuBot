package com.rokucraft.rokubot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class JoinListener extends ListenerAdapter {

    private final @NonNull Map<String, String> welcomeChannelMap;
    private final @NonNull List<MessageEmbed> welcomeEmbeds;
    private final Random rand = new Random();

    public JoinListener(
            @Nullable Map<String, String> welcomeChannelMap,
            @Nullable List<MessageEmbed> welcomeEmbeds
    ) {
        this.welcomeChannelMap = welcomeChannelMap != null ? Map.copyOf(welcomeChannelMap) : Map.of();
        this.welcomeEmbeds = welcomeEmbeds != null ? List.copyOf(welcomeEmbeds) : List.of();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String channelId = this.welcomeChannelMap.get(event.getGuild().getId());
        if (channelId == null) return;
        TextChannel welcomeChannel = event.getGuild().getTextChannelById(channelId);
        if (welcomeChannel == null) return;

        MessageEmbed welcomeEmbed = this.welcomeEmbeds.get(this.rand.nextInt(this.welcomeEmbeds.size()));

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
