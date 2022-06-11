package com.rokucraft.rokubot.commands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class Command {

    protected SlashCommandData data;

    private boolean guildOnly = false;

    public boolean isGuildOnly() {
        return guildOnly;
    }

    public void setGuildOnly(boolean publicCommand) {
        this.guildOnly = publicCommand;
    }

    public abstract void execute(SlashCommandInteractionEvent event);

    public SlashCommandData getData() {
        return data;
    }

    public void autoComplete(CommandAutoCompleteInteractionEvent event) {}
}
