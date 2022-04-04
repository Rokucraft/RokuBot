package com.rokucraft.rokubot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class Command {

    protected SlashCommandData data;

    public abstract void execute(SlashCommandInteractionEvent event);

    public SlashCommandData getData() {
        return data;
    }
}
