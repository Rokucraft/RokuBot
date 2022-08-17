package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class CommandManager extends ListenerAdapter {
    private final JDA jda;
    private final Set<GlobalCommand> globalCommands = new HashSet<>();
    private final Map<Guild, Set<Command>> guildCommands = new HashMap<>();

    public CommandManager(JDA jda) {
        this.jda = jda;
        this.jda.addEventListener(this);
    }

    public void addGuildCommands(Guild guild, Command... commands) {
        addGuildCommands(guild, Arrays.asList(commands));
    }

    public void addGuildCommands(Guild guild, List<? extends Command> commands) {
        Set<Command> commandsInGuild = this.guildCommands.get(guild);
        if (commandsInGuild != null) {
            commandsInGuild.addAll(commands);
        } else {
            this.guildCommands.put(guild, new HashSet<>(commands));
        }
    }

    public void addCommands(GlobalCommand... commands) {
        addCommands(Arrays.asList(commands));
    }

    public void addCommands(Collection<? extends GlobalCommand> commands) {
        this.globalCommands.addAll(commands);
    }

    public void clearCommands() {
        this.globalCommands.clear();
    }

    public void clearGuildCommands() {
        this.guildCommands.clear();
    }

    public void clearAll() {
        clearCommands();
        clearGuildCommands();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        findFirstMatchingCommand(SlashCommand.class, event)
                .ifPresent(cmd -> cmd.execute(event));
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        findFirstMatchingCommand(AutoCompletable.class, event)
                .ifPresent(cmd -> cmd.autoComplete(event));
    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {
        findFirstMatchingCommand(UserContextCommand.class, event)
                .ifPresent(cmd -> cmd.execute(event));
    }

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {
        findFirstMatchingCommand(MessageContextCommand.class, event)
                .ifPresent(cmd -> cmd.execute(event));
    }

    private <T> Optional<T> findFirstMatchingCommand(Class<T> clazz, CommandInteractionPayload event) {
        return findFirstMatchingCommand(clazz, event.getName(), event.getGuild());
    }

    private <T> Optional<T> findFirstMatchingCommand(Class<T> clazz, String name, Guild guild) {
        Set<Command> commandsInGuild = this.guildCommands.get(guild);
        return Stream.concat(
                        (commandsInGuild != null ? commandsInGuild.stream() : Stream.empty()),
                        this.globalCommands.stream()
                )
                .filter(clazz::isInstance)
                .filter(cmd -> cmd.getData(guild).getName().equals(name))
                .map(clazz::cast)
                .findFirst();
    }


    public void registerCommands() {
        this.jda.updateCommands().addCommands(
                this.globalCommands.stream()
                        .map(GlobalCommand::getData)
                        .toList()
        ).queue();
        this.guildCommands.forEach((guild, commands) ->
                guild.updateCommands().addCommands(
                        commands.stream()
                                .map(cmd -> cmd.getData(guild))
                                .toList()
                ).queue()
        );
    }

}
