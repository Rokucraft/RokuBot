package com.rokucraft.rokubot.entities;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record Rule(String name, String description) {}
