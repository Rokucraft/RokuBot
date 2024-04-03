package com.rokucraft.rokubot.config.serializers

import com.rokucraft.rokubot.util.ColorConstants
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

object MessageEmbedSerializer : TypeSerializer<MessageEmbed?> {
    override fun deserialize(type: Type, node: ConfigurationNode): MessageEmbed {
        return EmbedBuilder()
            .setTitle(node.node("title").get(), node.node("url").get())
            .setThumbnail(node.node("thumbnail-url").get())
            .setDescription(node.node("description").get())
            .setImage(node.node("image-url").get())
            .setColor(node.node("color").getInt(ColorConstants.BLUE))
            .setFooter(node.node("footer", "text").get(), node.node("footer", "icon-url").get())
            .build()
    }

    @Throws(SerializationException::class)
    override fun serialize(type: Type, obj: MessageEmbed?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }
        node.node("title").set(obj.title)
        node.node("url").set(obj.url)
        if (obj.thumbnail != null) {
            node.node("thumbnail-url").set(obj.thumbnail!!.url)
        }
        node.node("description").set(obj.description)
        if (obj.image != null) {
            node.node("image-url").set(obj.image!!.url)
        }
        node.node("color").set(obj.colorRaw)
        if (obj.footer != null) {
            node.node("footer", "text").set(obj.footer!!.text)
            node.node("footer", "icon-url").set(obj.footer!!.iconUrl)
        }
    }
}
