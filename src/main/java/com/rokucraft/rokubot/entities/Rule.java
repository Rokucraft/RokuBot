package com.rokucraft.rokubot.entities;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record Rule(
        @Required String name,
        @Nullable String description
) {}
