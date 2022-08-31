package com.rokucraft.rokubot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.rokucraft.rokubot.ColorConstants.RED;

public class EmbedUtil {

    public static @NonNull MessageEmbed createErrorEmbed(@NonNull String description) {
        return new EmbedBuilder()
                .setColor(RED)
                .setDescription("❌ " + description)
                .build();
    }
}
