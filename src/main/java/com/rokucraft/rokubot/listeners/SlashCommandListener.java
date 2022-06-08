package com.rokucraft.rokubot.listeners;

import com.rokucraft.rokubot.commands.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SlashCommandListener extends ListenerAdapter {
    private final Map<String, Command> commandMap = new HashMap<>();

    public SlashCommandListener(Command... commands) {
        addCommands(commands);
    }

    /**
     * Adds the specified commands to the listener.
     *
     * @param commands The commands to add
     *
     * @return The SlashCommandListener instance, for chaining
     */
    public SlashCommandListener addCommands(Command... commands) {
        return addCommands(Arrays.asList(commands));
    }

    /**
     * Adds the specified commands to the listener.
     *
     * @param commands The commands to add
     *
     * @return The SlashCommandListener instance, for chaining
     */
    public SlashCommandListener addCommands(Collection<? extends Command> commands) {
        for (Command command : commands) {
            commandMap.put(command.getData().getName(), command);
        }
        return this;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Command command = commandMap.get(event.getName());
        if (command != null) {
            command.execute(event);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        Command command = commandMap.get(event.getName());
        if (command != null) {
            command.autoComplete(event);
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        updateCommands(event.getJDA());
    }

    /**
     * Sets the list of global commands to be the {@link Command}s added to the listener.
     *
     * @param jda The JDA instance
     */
    public void updateCommands(@NonNull JDA jda) {
        jda.updateCommands().addCommands(commandMap.values().stream().map(Command::getData).toList()).queue();
    }
}
