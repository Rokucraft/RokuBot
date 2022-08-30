package com.rokucraft.rokubot.entities;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

@ConfigSerializable
public record Repository(
        @Required String name,
        @Required String repositoryUrl
) {}
