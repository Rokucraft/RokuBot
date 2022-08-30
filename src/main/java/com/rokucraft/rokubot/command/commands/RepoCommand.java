package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.AutoCompletable;
import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.entities.Repository;
import com.rokucraft.rokubot.util.EmbedUtil;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepoCommand implements SlashCommand, AutoCompletable, GuildIndependentCommand {
    private final @NonNull Set<Repository> repositories;
    private final @Nullable Repository defaultRepository;
    private final @NonNull SlashCommandData data;

    public RepoCommand(
            @NonNull Collection<? extends Repository> repositories,
            @Nullable Repository defaultRepository
    ) {
        this.repositories = new HashSet<>(repositories);
        this.defaultRepository = defaultRepository;

        boolean nameRequired = defaultRepository == null; // A name is required when no default is provided
        boolean useAutocomplete = this.repositories.size() > OptionData.MAX_CHOICES;
        var nameOption = new OptionData(
                OptionType.STRING,
                "name",
                "The name of the repository",
                nameRequired,
                useAutocomplete
        );
        if (!useAutocomplete) {
            nameOption.addChoices(
                    this.repositories.stream()
                            .map(repo -> new Command.Choice(repo.name(), repo.name()))
                            .toList()
            );
        }

        this.data = Commands.slash("repo", "Shows a link for the named repository")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                .addOptions(nameOption);
    }
    @Override
    public void autoComplete(@NonNull CommandAutoCompleteInteractionEvent event) {
        event.replyChoiceStrings(
            AutoCompletable.filterCollectionByQueryString(this.repositories, Repository::name, event)
        ).queue();
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        String name = event.getOption("name", OptionMapping::getAsString);
        Optional<Repository> repoOptional = (name == null)
                ? Optional.ofNullable(defaultRepository)
                : this.repositories.stream()
                    .filter(repo -> repo.name().equals(name))
                    .findFirst();

        repoOptional.ifPresentOrElse(
                invite -> event.reply(invite.repositoryUrl()).queue(),
                () -> {
                    String error = (name != null) ? "Repository `" + name + "` not found" : "You must provide a name";
                    event.replyEmbeds(EmbedUtil.createErrorEmbed(error)).setEphemeral(true).queue();
                }
        );
    }

    @Override
    public @NonNull CommandData getData() {
        return this.data;
    }
}
