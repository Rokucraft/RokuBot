package com.rokucraft.rokubot.config.serializers

import io.leangen.geantyref.TypeToken
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

object MessageCreateDataSerializer : TypeSerializer<MessageCreateData?> {
    @Throws(SerializationException::class)
    override fun deserialize(type: Type, node: ConfigurationNode): MessageCreateData {
        return MessageCreateBuilder()
            .setContent(node.node("content").string)
            .setEmbeds(node.node("embeds").getList(MessageEmbed::class.java, ArrayList()))
            .setComponents(
                node.node("buttons")
                    .getList(object : TypeToken<List<Button>>() {}, listOf())
                    .map(ActionRow::of)
            )
            .build()
    }

    @Throws(SerializationException::class)
    override fun serialize(type: Type, obj: MessageCreateData?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }
        node.node("content").set(obj.content)
        node.node("embeds").set(obj.embeds)
        node.node("buttons").set(obj.components.map { it.buttons })
    }
}
