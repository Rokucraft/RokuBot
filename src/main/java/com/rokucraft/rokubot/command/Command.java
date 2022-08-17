package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Command {
    @NonNull CommandData getData(Guild guild);
}
