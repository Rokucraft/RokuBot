package com.rokucraft.RokuBot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class Command {

    protected CommandData data;

    public abstract void execute(SlashCommandEvent event);

    public CommandData getData() {
        return data;
    }
}
