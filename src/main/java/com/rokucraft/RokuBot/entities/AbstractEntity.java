package com.rokucraft.RokuBot.entities;

import com.rokucraft.RokuBot.Settings;
import net.dv8tion.jda.api.entities.Category;

import java.util.List;

public abstract class AbstractEntity {
    String name;
    String[] aliases;
    boolean staffOnly;

    public String getName() {
        return name;
    }

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

    public static <T extends AbstractEntity> AbstractEntity find(String name, List<T> abstractEntityList) {
        if (abstractEntityList != null) {
            for (AbstractEntity entity : abstractEntityList) {
                if (name.startsWith(entity.getName())) {
                    return entity;
                }
                for (String alias : entity.getAliases()) {
                    if (name.startsWith(alias)) {
                        return entity;
                    }
                }
            }
        }
        return null;
    }
}
