package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.entities.Tag;
import com.rokucraft.rokubot.util.EmbedUtil;
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

import java.util.List;
import java.util.Optional;

public class TagCommand implements SlashCommand, GuildIndependentCommand {
    private final @NonNull List<Tag> tags;
    private final @NonNull SlashCommandData data;
    public TagCommand(@NonNull List<Tag> tags) {
        this.tags = List.copyOf(tags);
        this.data = Commands.slash("tag", "Show tagged information")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                .addOptions(new OptionData(OptionType.STRING, "name", "The tag name", true).addChoices(
                        this.tags.stream()
                                .map(tag -> new Command.Choice(tag.name(), tag.name()))
                                .toList()
                ));
    }

    @Override
    public @NonNull CommandData getData() {
        return this.data;
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        String name = event.getOption("name", OptionMapping::getAsString);
        Optional<Tag> tag = this.tags.stream()
                .filter(t -> t.name().equals(name))
                .findFirst();
        tag.ifPresentOrElse(
                t -> event.reply(t.message()).queue(),
                () -> event.replyEmbeds(EmbedUtil.createErrorEmbed("Tag `" + name + "` does not exist"))
                        .setEphemeral(true).queue()
        );
    }
}
