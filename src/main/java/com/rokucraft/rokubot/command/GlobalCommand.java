package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface GlobalCommand extends Command {
    @Override
    default CommandData getData(Guild guild) {
        return getData();
    }

    CommandData getData();
}
