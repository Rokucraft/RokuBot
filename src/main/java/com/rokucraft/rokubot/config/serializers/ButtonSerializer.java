package com.rokucraft.rokubot.config.serializers;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ButtonSerializer implements TypeSerializer<Button> {

    public static final ButtonSerializer INSTANCE = new ButtonSerializer();

    @Override
    public Button deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Emoji emoji = null;
        String emojiCode = node.node("emoji").getString();
        if (emojiCode != null && !emojiCode.isEmpty()) {
            emoji = Emoji.fromMarkdown(emojiCode);
        }
        String idOrUrl = node.node("id-or-url").getString();
        if (idOrUrl == null) {
            throw new SerializationException("A value is required for this field");
        }
        return Button.of(
                node.node("style").get(ButtonStyle.class, ButtonStyle.LINK),
                idOrUrl,
                node.node("label").getString(),
                emoji
        );
    }

    @Override
    public void serialize(Type type, @Nullable Button obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }
        node.node("style").set(obj.getStyle());
        if (obj.getStyle() == ButtonStyle.LINK) {
            node.node("id-or-url").set(obj.getUrl());
        } else {
            node.node("id-or-url").set(obj.getId());
        }
        node.node("label").set(obj.getLabel());
        if (obj.getEmoji() != null) {
            node.node("emoji").set(obj.getEmoji().getAsMention());
        }
    }
}
