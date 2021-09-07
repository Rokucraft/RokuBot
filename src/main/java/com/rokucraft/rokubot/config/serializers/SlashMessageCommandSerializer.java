package com.rokucraft.rokubot.config.serializers;

import com.rokucraft.rokubot.commands.SlashMessageCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.Button;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class SlashMessageCommandSerializer implements TypeSerializer<SlashMessageCommand> {

    public static final SlashMessageCommandSerializer INSTANCE = new SlashMessageCommandSerializer();

    @Override
    public SlashMessageCommand deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return new SlashMessageCommand(
                node.node("name").getString(),
                node.node("description").getString(),
                node.node("message").get(Message.class),
                node.node("buttons").getList(Button.class)
        );
    }

    @Override
    public void serialize(Type type, @Nullable SlashMessageCommand obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("name").set(obj.getData().getName());
        node.node("description").set(obj.getData().getDescription());
        node.node("message").set(obj.getMessage());
    }
}
