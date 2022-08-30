package com.rokucraft.rokubot.entities;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.meta.Required;

import java.util.List;

public abstract class AbstractEntity {
    @Required
    String name;
    String[] aliases;
    boolean staffOnly;

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String[] getAliases() {
        return aliases;
    }

    @Nullable
    public static <T extends AbstractEntity> AbstractEntity find(String name, List<T> abstractEntityList) {
        if (abstractEntityList != null) {
            name = name.toLowerCase();
            for (AbstractEntity entity : abstractEntityList) {
                if (name.startsWith(entity.getName().toLowerCase())) {
                    return entity;
                }
                for (String alias : entity.getAliases()) {
                    if (alias != null && name.startsWith(alias.toLowerCase())) {
                        return entity;
                    }
                }
            }
        }
        return null;
    }
}
