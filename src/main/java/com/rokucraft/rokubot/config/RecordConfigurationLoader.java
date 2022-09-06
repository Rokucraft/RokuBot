package com.rokucraft.rokubot.config;

import com.rokucraft.rokubot.config.serializers.ButtonSerializer;
import com.rokucraft.rokubot.config.serializers.MessageCreateDataSerializer;
import com.rokucraft.rokubot.config.serializers.MessageEmbedSerializer;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.util.NamingSchemes;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RecordConfigurationLoader {
    private static final TypeSerializerCollection serializers =
            TypeSerializerCollection.defaults().childBuilder()
                    .register(MessageCreateData.class, MessageCreateDataSerializer.INSTANCE)
                    .register(MessageEmbed.class, MessageEmbedSerializer.INSTANCE)
                    .register(Button.class, ButtonSerializer.INSTANCE)
                    .build();
    private static final ConfigurationOptions defaultOptions =
            ConfigurationOptions.defaults()
                    .serializers(serializers)
                    .implicitInitialization(true);

    private final Map<Path, YamlConfigurationLoader> loaderMap = new HashMap<>();

    public <T extends Record> @NonNull T load(@NonNull Class<T> clazz) throws ConfigurateException {
        Source baseSource = clazz.getAnnotation(Source.class);
        if (baseSource == null) {
            throw new ConfigurateException(clazz + " does not have a @Source annotation");
        }

        T baseConfig = rootNodeAtPath(Path.of(baseSource.value())).get(clazz);
        RecordComponent[] components = clazz.getRecordComponents();
        Object[] args = new Object[components.length];
        int counter = 0;
        try {
            for (RecordComponent comp : components) {
                Source source = comp.getAnnotation(Source.class);
                args[counter++] = (source == null)
                        ? comp.getAccessor().invoke(baseConfig)
                        : deserializeNodeFromPath(comp, Path.of(source.value()));
            }
            return getCanonicalConstructor(clazz).newInstance(args);
        } catch (
                IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException
                | InstantiationException e
        ) {
            throw new SerializationException(clazz, e);
        }
    }

    private @Nullable Object deserializeNodeFromPath(
            @NonNull RecordComponent component,
            @NonNull Path path
    ) throws ConfigurateException {
        String nodeName = NamingSchemes.LOWER_CASE_DASHED.coerce(component.getName());
        return rootNodeAtPath(path)
                .node(nodeName)
                .get(component.getGenericType());
    }

    private @NonNull CommentedConfigurationNode rootNodeAtPath(@NonNull Path path) throws ConfigurateException {
        if (!this.loaderMap.containsKey(path)) {
            this.loaderMap.put(
                    path,
                    YamlConfigurationLoader.builder()
                            .path(path)
                            .defaultOptions(defaultOptions)
                            .build()
            );
        }
        return this.loaderMap.get(path).load();
    }

    private static <T extends Record> @NonNull Constructor<T> getCanonicalConstructor(@NonNull Class<T> cls)
            throws NoSuchMethodException {
        Class<?>[] paramTypes =
                Arrays.stream(cls.getRecordComponents())
                        .map(RecordComponent::getType)
                        .toArray(Class<?>[]::new);
        return cls.getDeclaredConstructor(paramTypes);
    }
}
