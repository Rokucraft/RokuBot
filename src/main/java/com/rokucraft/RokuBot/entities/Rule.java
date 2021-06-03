package com.rokucraft.RokuBot.entities;

import com.rokucraft.RokuBot.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import static com.rokucraft.RokuBot.Constants.FUCHSIA;

@ConfigSerializable
public class Rule extends AbstractEntity {
    private String description;

    public MessageEmbed toEmbed(int index) {
         return new EmbedBuilder()
                 .setTitle(index + ". " + name)
                 .setDescription(description)
                 .setFooter(Settings.rulesFooter, "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2696.png")
                 .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f4dc.png")
                 .setColor(FUCHSIA)
                 .build();
    }
}
