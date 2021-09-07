package com.rokucraft.rokubot.entities;

import com.rokucraft.rokubot.config.Settings;
import net.dv8tion.jda.api.entities.Category;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public abstract class AbstractEntity {
    String name;
    String[] aliases;
    boolean staffOnly;

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String[] getAliases() {
        return aliases;
    }

    public boolean isStaffOnly() {
        return staffOnly;
    }

    public boolean isAllowed(Category category) {
        if (this.staffOnly) {
            return Settings.staffCategoryIDs.contains(category.getId());
        }
        return true;
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
                    if (name.startsWith(alias.toLowerCase())) {
                        return entity;
                    }
                }
            }
        }
        return null;
    }
}
