package com.rokucraft.rokubot.config.serializers;

import io.leangen.geantyref.TypeToken;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessageCreateDataSerializer implements TypeSerializer<MessageCreateData> {

    public static final MessageCreateDataSerializer INSTANCE = new MessageCreateDataSerializer();

    @Override
    public MessageCreateData deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return new MessageCreateBuilder()
                .setContent(node.node("content").getString())
                .setEmbeds(node.node("embeds").getList(MessageEmbed.class, new ArrayList<>()))
                .setComponents(
                        node.node("buttons").getList(new TypeToken<List<Button>>(){}, new ArrayList<>())
                                .stream()
                                .map(ActionRow::of)
                                .toList())
                .build();
    }

    @Override
    public void serialize(Type type, @Nullable MessageCreateData obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("content").set(obj.getContent());
        node.node("embeds").set(obj.getEmbeds());
        node.node("buttons").set(obj.getComponents().stream().map(LayoutComponent::getButtons).toList());
    }
}
