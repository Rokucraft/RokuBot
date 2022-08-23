package com.rokucraft.rokubot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import static com.rokucraft.rokubot.Constants.BLUE;
import static com.rokucraft.rokubot.Constants.RED;

public class EmbedUtil {
    public static EmbedBuilder createInfoEmbed() {
        return new EmbedBuilder()
                .setColor(BLUE)
                .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png");
    }

    public static EmbedBuilder createErrorEmbed() {
        return new EmbedBuilder()
                .setColor(RED);
    }

    public static MessageEmbed createErrorEmbed(String description) {
        return createErrorEmbed()
                .setDescription("❌ " + description)
                .build();
    }
}
