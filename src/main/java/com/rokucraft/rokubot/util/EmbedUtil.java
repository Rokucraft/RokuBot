package com.rokucraft.rokubot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import static com.rokucraft.rokubot.Constants.RED;

public class EmbedUtil {

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
