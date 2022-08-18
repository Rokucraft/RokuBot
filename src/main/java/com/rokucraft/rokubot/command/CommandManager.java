package com.rokucraft.rokubot.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class CommandManager extends ListenerAdapter {
    private final @NonNull JDA jda;
    private final @NonNull Set<GuildIndependentCommand> globalCommands = new HashSet<>();
    private final @NonNull Map<Guild, Set<Command>> guildCommands = new HashMap<>();

    public CommandManager(@NonNull JDA jda) {
        this.jda = jda;
        this.jda.addEventListener(this);
    }

    public void addGuildCommands(@NonNull Guild guild, @NonNull Command... commands) {
        addGuildCommands(guild, Arrays.asList(commands));
    }

    public void addGuildCommands(@NonNull Guild guild, @NonNull List<? extends Command> commands) {
        Set<Command> commandsInGuild = this.guildCommands.get(guild);
        if (commandsInGuild != null) {
            commandsInGuild.addAll(commands);
        } else {
            this.guildCommands.put(guild, new HashSet<>(commands));
        }
    }

    public void addCommands(@NonNull GuildIndependentCommand... commands) {
        addCommands(Arrays.asList(commands));
    }

    public void addCommands(@NonNull Collection<? extends GuildIndependentCommand> commands) {
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
    public void onSlashCommandInteraction(@NonNull SlashCommandInteractionEvent event) {
        findFirstMatchingCommand(SlashCommand.class, event)
                .ifPresent(cmd -> cmd.execute(event));
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NonNull CommandAutoCompleteInteractionEvent event) {
        findFirstMatchingCommand(AutoCompletable.class, event)
                .ifPresent(cmd -> cmd.autoComplete(event));
    }

    @Override
    public void onUserContextInteraction(@NonNull UserContextInteractionEvent event) {
        findFirstMatchingCommand(UserContextCommand.class, event)
                .ifPresent(cmd -> cmd.execute(event));
    }

    @Override
    public void onMessageContextInteraction(@NonNull MessageContextInteractionEvent event) {
        findFirstMatchingCommand(MessageContextCommand.class, event)
                .ifPresent(cmd -> cmd.execute(event));
    }

    private <T> Optional<T> findFirstMatchingCommand(@NonNull Class<T> clazz, @NonNull CommandInteractionPayload event) {
        return findFirstMatchingCommand(clazz, event.getName(), event.getGuild());
    }

    private <T> Optional<T> findFirstMatchingCommand(
            @NonNull Class<T> clazz,
            @NonNull String name,
            @Nullable Guild guild
    ) {
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
                        .map(GuildIndependentCommand::getData)
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
