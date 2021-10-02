package com.rokucraft.rokubot.util;

import com.rokucraft.rokubot.RokuBot;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import org.checkerframework.checker.nullness.qual.Nullable;

public class StaffOnly {
    public static boolean check(@Nullable Category category) {
        if (category == null) {
            return false;
        } else {
            return RokuBot.getConfig().staffCategoryIDs.contains(category.getId());
        }
    }

    public static boolean check(Message message) {
        return check(message.getCategory());
    }
}
