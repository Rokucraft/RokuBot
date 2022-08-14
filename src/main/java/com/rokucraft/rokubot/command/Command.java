package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface Command {
    CommandData getData(Guild guild);
}
