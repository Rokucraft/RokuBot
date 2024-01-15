package com.rokucraft.rokubot.config

import com.rokucraft.rokubot.config.serializers.ButtonSerializer
import com.rokucraft.rokubot.config.serializers.MessageCreateDataSerializer
import com.rokucraft.rokubot.config.serializers.MessageEmbedSerializer
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.ConfigurationOptions
import org.spongepowered.configurate.kotlin.dataClassFieldDiscoverer
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.objectmapping.ObjectMapper
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path
import kotlin.io.path.*

private val serializers = TypeSerializerCollection.defaults().childBuilder()
    .register(MessageCreateData::class.java, MessageCreateDataSerializer)
    .register(MessageEmbed::class.java, MessageEmbedSerializer)
    .register(Button::class.java, ButtonSerializer)
    .build()

private val defaultOptions = ConfigurationOptions.defaults()
    .serializers(serializers)
    .serializers { s ->
        s.registerAnnotatedObjects(
            ObjectMapper.factoryBuilder().addDiscoverer(dataClassFieldDiscoverer()).build(),
        )
    }
    .implicitInitialization(true)

@OptIn(ExperimentalPathApi::class)
fun loadConfig(): Config = try {
    Path.of("").walk()
        .filter { it.isReadable() && it.isRegularFile() && it.isYaml() }
        .map { loadNodeAtPath(it) }
        .reduceOrNull { pv, cv -> pv.mergeFrom(cv) }
        ?.get<Config>() ?: throw ConfigurationException("Unable to load configuration")
} catch (e: ConfigurateException) {
    throw ConfigurationException(e)
}

private fun Path.isYaml(): Boolean = extension == "yml" || extension == "yaml"

private fun loadNodeAtPath(path: Path): ConfigurationNode =
    YamlConfigurationLoader.builder()
        .path(path)
        .defaultOptions(defaultOptions)
        .build()
        .load()
