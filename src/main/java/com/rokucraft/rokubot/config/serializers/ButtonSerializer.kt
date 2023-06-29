package com.rokucraft.rokubot.config.serializers

import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

object ButtonSerializer : TypeSerializer<Button> {
    @Throws(SerializationException::class)
    override fun deserialize(type: Type, node: ConfigurationNode): Button {
        var emoji: Emoji? = null
        val emojiCode: String? = node.node("emoji").get()
        if (!emojiCode.isNullOrEmpty()) {
            emoji = Emoji.fromFormatted(emojiCode)
        }
        val idOrUrl: String = node.node("id-or-url").get()
            ?: throw SerializationException("A value is required for this field")
        return Button.of(
            node.node("style").get(ButtonStyle::class, ButtonStyle.LINK),
            idOrUrl,
            node.node("label").get(),
            emoji
        )
    }

    @Throws(SerializationException::class)
    override fun serialize(type: Type, obj: Button?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }
        node.node("style").set(obj.style)
        node.node("id-or-url").set(if (obj.style == ButtonStyle.LINK) obj.url else obj.id)
        node.node("label").set(obj.label)
        node.node("emoji").set(obj.emoji?.formatted)
    }
}
