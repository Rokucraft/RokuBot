package com.rokucraft.rokubot.commands;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.entities.DiscordInvite;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class InviteCommand extends Command {

    public InviteCommand() {
        List<net.dv8tion.jda.api.interactions.commands.Command.Choice> inviteChoices = new ArrayList<>();
        for (DiscordInvite invite : RokuBot.getConfig().discordInvites) {
            if (!invite.isStaffOnly()) {
                String name = invite.getName();
                inviteChoices.add(new net.dv8tion.jda.api.interactions.commands.Command.Choice(name, name));
            }
        }

        this.data = new CommandData("invite", "Shows a discord invite link for the named server")
                .addOptions(
                        new OptionData(OptionType.STRING, "name", "The name of the server")
                                .addChoices(inviteChoices)
                );
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void execute(SlashCommandEvent event) {
        OptionMapping name = event.getOption("name");
        if (name == null) {
            event.reply(DiscordInvite.getDefault().getInviteUrl()).queue();
        } else {
            event.reply(DiscordInvite.find(name.getAsString()).getInviteUrl()).queue();
        }
    }
}
