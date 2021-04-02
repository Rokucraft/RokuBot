package com.rokucraft.RokuBot.entities;

import com.rokucraft.RokuBot.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class TextCommand extends AbstractEntity {
    private String description;
    private Message message;

    public String getDescription() {
        return description;
    }

    public Message getMessage() {
        return message;
    }

    @ConfigSerializable
    public static class Message {
        private String content;
        private Embed embed;

        public String getContent() {
            return content;
        }

        public Embed getEmbed() {
            return embed;
        }

        @ConfigSerializable
        public static class Embed {
            private String title;
            private String url;
            private String thumbnailUrl;
            private String description;
            private String imageUrl;

            public String getTitle() {
                return title;
            }

            public String getUrl() {
                return url;
            }

            public String getThumbnailUrl() {
                return thumbnailUrl;
            }

            public String getDescription() {
                return description;
            }

            public String getImageUrl() {
                return imageUrl;
            }
        }
    }

    public void execute(MessageChannel channel) {
        String content = getMessage().getContent();
        Message.Embed embed = getMessage().getEmbed();
        boolean hasContent = content != null;
        boolean hasEmbed = false;
        if (embed.getTitle() != null) {
            hasEmbed = true;
        } else if (embed.getDescription() != null) {
            hasEmbed = true;
        } else if (embed.getUrl() != null) {
            hasEmbed = true;
        } else if (embed.getThumbnailUrl() != null) {
            hasEmbed = true;
        }

        if (!hasContent && !hasEmbed) {return;}

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setContent(content);

        if (hasEmbed) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(0x0FFFFF)
                    .setTitle(embed.getTitle(), embed.getUrl())
                    .setDescription(embed.getDescription())
                    .setImage(embed.getImageUrl());

            if (embed.getThumbnailUrl() != null) {
                builder.setThumbnail(embed.getThumbnailUrl());
            } else {
                builder.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png");
            }
            messageBuilder.setEmbed(builder.build());
        }
        channel.sendMessage(messageBuilder.build()).queue();
    }

    public static TextCommand find(String name) {
        return (TextCommand) find(name, Settings.textCommandList);
    }
}
