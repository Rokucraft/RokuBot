package com.rokucraft.rokubot.entities;

import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record Tag(
        @Required String name,
        @Nullable String description,
        @Required MessageCreateData message
) {}
