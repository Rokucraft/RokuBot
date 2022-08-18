package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface GuildIndependentCommand extends Command {
    @Override
    default @NonNull CommandData getData(@NonNull Guild guild) {
        return getData();
    }

    @NonNull CommandData getData();
}
