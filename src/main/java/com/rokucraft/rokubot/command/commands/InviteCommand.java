package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.command.GuildIndependentCommand;
import com.rokucraft.rokubot.entities.DiscordInvite;
import com.rokucraft.rokubot.util.EmbedUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class InviteCommand implements SlashCommand, GuildIndependentCommand {
    private final CommandData data;

    public InviteCommand() {
        List<Choice> inviteChoices = new ArrayList<>();
        for (DiscordInvite invite : RokuBot.getConfig().getDiscordInvites()) {
            if (!invite.isStaffOnly()) {
                String name = invite.getName();
                inviteChoices.add(new Choice(name, name));
            }
        }

        this.data = Commands.slash("invite", "Shows a discord invite link for the named server")
                .addOptions(
                        new OptionData(OptionType.STRING, "name", "The name of the server")
                                .addChoices(inviteChoices)
                );
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        String name = event.getOption("name", OptionMapping::getAsString);
        DiscordInvite invite = name == null ? DiscordInvite.getDefault() : DiscordInvite.find(name);
        if (invite == null) {
            event.replyEmbeds(EmbedUtil.createErrorEmbed("Invite `" + name + "` not found")).setEphemeral(true).queue();
            return;
        }
        event.reply(invite.getInviteUrl()).queue();
    }

    @Override
    public @NonNull CommandData getData() {
        return this.data;
    }
}
