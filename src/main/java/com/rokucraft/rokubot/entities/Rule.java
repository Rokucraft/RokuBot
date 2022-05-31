package com.rokucraft.rokubot.entities;

import com.rokucraft.rokubot.RokuBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import static com.rokucraft.rokubot.Constants.FUCHSIA;

@SuppressWarnings("unused")
@ConfigSerializable
public class Rule extends AbstractEntity {
    private String description;

    /**
     * Constructs a decorated {@link MessageEmbed} containing the rule
     * @param index the index of the rule
     * @return a decorated {@link MessageEmbed} containing the rule
     */
    @NonNull
    public MessageEmbed toEmbed(int index) {
         return new EmbedBuilder()
                 .setTitle(index + ". " + name)
                 .setDescription(description)
                 .setFooter(RokuBot.getConfig().getRulesFooter(), "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2696.png")
                 .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f4dc.png")
                 .setColor(FUCHSIA)
                 .build();
    }
}
