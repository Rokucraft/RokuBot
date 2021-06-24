package com.rokucraft.RokuBot.serializers;

import com.rokucraft.RokuBot.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class MessageEmbedSerializer implements TypeSerializer<MessageEmbed> {

    public static final MessageEmbedSerializer INSTANCE = new MessageEmbedSerializer();

    @Override
    public MessageEmbed deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return new EmbedBuilder()
                .setTitle(node.node("title").getString(), node.node("url").getString())
                .setThumbnail(node.node("thumbnail-url").getString())
                .setDescription(node.node("description").getString())
                .setImage(node.node("image-url").getString())
                .setColor(node.node("color").getInt(Constants.BLUE))
                .setFooter(node.node("footer", "text").getString(), node.node("footer", "icon-url").getString())
                .build();
    }

    @Override
    public void serialize(Type type, @Nullable MessageEmbed obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("title").set(obj.getTitle());
        node.node("url").set(obj.getUrl());
        if (obj.getThumbnail() != null) {
            node.node("thumbnail-url").set(obj.getThumbnail().getUrl());
        }
        node.node("description").set(obj.getDescription());
        if (obj.getImage() != null) {
            node.node("image-url").set(obj.getImage().getUrl());
        }
        node.node("color").set(obj.getColorRaw());
        if (obj.getFooter() != null) {
            node.node("footer", "text").set(obj.getFooter().getText());
            node.node("footer", "icon-url").set(obj.getFooter().getIconUrl());
        }
    }
}
