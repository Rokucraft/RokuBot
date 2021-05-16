package com.rokucraft.RokuBot.entities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import static com.rokucraft.RokuBot.Constants.FUCHSIA;

@ConfigSerializable
public class Rule extends AbstractEntity {
    private String description;

    public MessageEmbed toEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(name)
                .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f4dc.png")
                .setColor(FUCHSIA)
                .setDescription(description);
        return builder.build();
    }
}
