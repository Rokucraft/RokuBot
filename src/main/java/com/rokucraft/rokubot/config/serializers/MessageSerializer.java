package com.rokucraft.rokubot.config.serializers;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class MessageSerializer implements TypeSerializer<Message> {

    public static final MessageSerializer INSTANCE = new MessageSerializer();

    @Override
    public Message deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return new MessageBuilder()
                .setContent(node.node("content").getString())
                .setEmbed(node.node("embed").get(MessageEmbed.class))
                .build();
    }

    @Override
    public void serialize(Type type, @Nullable Message obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("content").set(obj.getContentRaw());
        node.node("embed").set(obj.getEmbeds().get(0));
    }
}
