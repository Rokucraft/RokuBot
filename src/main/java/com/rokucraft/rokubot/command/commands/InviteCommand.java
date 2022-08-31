package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.AutoCompletable;
import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.entities.DiscordInvite;
import com.rokucraft.rokubot.util.EmbedUtil;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions.*;

public class InviteCommand implements SlashCommand, GuildIndependentCommand, AutoCompletable {
    private final @NonNull CommandData data;
    private final @NonNull Set<DiscordInvite> invites;
    private final @Nullable DiscordInvite defaultInvite;

    public InviteCommand(
            @NonNull String name,
            @NonNull Collection<? extends DiscordInvite> invites,
            @Nullable DiscordInvite defaultInvite,
            boolean defaultEnabled
    ) {
        this.invites = new HashSet<>(invites);
        this.defaultInvite = defaultInvite;

        boolean nameRequired = defaultInvite == null; // A name is required when no default is provided
        boolean useAutocomplete = this.invites.size() > OptionData.MAX_CHOICES;
        var nameOption = new OptionData(
                OptionType.STRING,
                "name",
                "The name of the server",
                nameRequired,
                useAutocomplete
        );
        if (!useAutocomplete) {
            nameOption.addChoices(
                    this.invites.stream()
                            .map(invite -> new Choice(invite.name(), invite.name()))
                            .toList()
            );
        }

        this.data = Commands.slash(name, "Shows a Discord invite link for the named server")
                .setDefaultPermissions(defaultEnabled ? ENABLED : DISABLED)
                .addOptions(nameOption);
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        String name = event.getOption("name", OptionMapping::getAsString);
        Optional<DiscordInvite> inviteOptional = (name == null)
                ? Optional.ofNullable(this.defaultInvite)
                : this.invites.stream()
                        .filter(invite -> invite.name().equals(name))
                        .findFirst();

        inviteOptional.ifPresentOrElse(
                invite -> event.reply(invite.inviteUrl()).queue(),
                () -> {
                    String error = (name != null) ? "Invite `" + name + "` not found" : "You must provide a name";
                    event.replyEmbeds(EmbedUtil.createErrorEmbed(error)).setEphemeral(true).queue();
                }
        );
    }

    @Override
    public void autoComplete(@NonNull CommandAutoCompleteInteractionEvent event) {
        event.replyChoiceStrings(
                AutoCompletable.filterCollectionByQueryString(this.invites, DiscordInvite::name, event)
        ).queue();
    }

    @Override
    public @NonNull CommandData getData() {
        return this.data;
    }
}
