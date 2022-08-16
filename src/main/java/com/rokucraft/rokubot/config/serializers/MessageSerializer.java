package com.rokucraft.rokubot.config.serializers;

import io.leangen.geantyref.TypeToken;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessageSerializer implements TypeSerializer<Message> {

    public static final MessageSerializer INSTANCE = new MessageSerializer();

    @Override
    public Message deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return new MessageBuilder()
                .setContent(node.node("content").getString())
                .setEmbeds(node.node("embeds").getList(MessageEmbed.class, new ArrayList<>()))
                .setActionRows(
                        node.node("buttons").getList(new TypeToken<List<Button>>(){}, new ArrayList<>())
                                .stream()
                                .map(ActionRow::of)
                                .toList())
                .build();
    }

    @Override
    public void serialize(Type type, @Nullable Message obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("content").set(obj.getContentRaw());
        node.node("embeds").set(obj.getEmbeds());
        node.node("buttons").set(obj.getActionRows().stream().map(ActionRow::getButtons).toList());
    }
}
