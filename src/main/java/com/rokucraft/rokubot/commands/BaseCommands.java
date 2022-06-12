package com.rokucraft.rokubot.commands;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.entities.DiscordInvite;
import com.rokucraft.rokubot.entities.TextCommand;
import com.rokucraft.rokubot.util.StaffOnly;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static com.rokucraft.rokubot.Constants.GREEN;
import static com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed;
import static com.rokucraft.rokubot.util.EmbedUtil.createInfoEmbed;

public class BaseCommands extends ListenerAdapter {

    final EventWaiter waiter;

    public BaseCommands(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild() ||event.getAuthor().isBot()) return;
        String prefix = RokuBot.getConfig().getPrefix();

        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        if (content.startsWith(prefix + "help") && StaffOnly.check(message))  {
            EmbedBuilder response = createInfoEmbed()
                    .setTitle(event.getGuild().getSelfMember().getEffectiveName() + " Help")
                    .setFooter("Made by " + RokuBot.getBotOwner().getName(), RokuBot.getBotOwner().getAvatarUrl())
                    .addField("Plugin Commands",
                                ":snail: `" + prefix + "invite <name>` shows a discord invite for the named plugin",
                        false)
                    .addField("GitHub Commands",
                        ":exclamation: `" + prefix + "issues [label]` lists all issues (with the specified label)\n" +
                                ":books: `" + prefix + "reference` shows a link to the GE reference\n" +
                                ":question: `" + prefix + "questions` shows a link to the asking-questions document\n" +
                                ":fleur_de_lis: `" + prefix + "symbols` shows a link to the list with symbols",
                        false);

            StringBuilder textCommandsHelpBuilder = new StringBuilder();
            for (TextCommand textCommand : RokuBot.getConfig().getTextCommands()) {
                if (textCommand.isAllowed(message.getCategory())) {
                    String description = textCommand.getDescription();
                    if (description == null) {description = "";}
                    textCommandsHelpBuilder.append("• `").append(prefix).append(textCommand.getName()).append("` ").append(description).append("\n");
                }
            }
            String textCommandsHelp = textCommandsHelpBuilder.toString();

            response.addField("Utility Commands", textCommandsHelp, false);

            channel.sendMessageEmbeds(response.build()).queue();
            response.clear();
            return;
        }

        if (content.startsWith(prefix + "discord")) {
            String[] args = content.split("\\s+");
            if (args.length == 1) {
                channel.sendMessage(DiscordInvite.getDefault().getInviteUrl()).queue();
            } else {
                DiscordInvite discordInvite = DiscordInvite.find(args[1]);
                if (discordInvite != null && discordInvite.isAllowed(message.getCategory())) {
                    channel.sendMessage(discordInvite.getInviteUrl()).queue();
                } else {
                    MessageEmbed errorEmbed = createErrorEmbed()
                            .setTitle("Discord server `" + args[1] + "` not found!")
                            .setDescription("Usage: `" + prefix + "invite [name]`")
                            .build();
                    channel.sendMessageEmbeds(errorEmbed).queue();
                }
            }
            return;
        }

        if (content.toLowerCase().startsWith("all my homies ")) {
            channel.sendMessage("who").queue();
            waiter.waitForEvent(MessageReceivedEvent.class,
                    e -> e.getAuthor().equals(event.getAuthor())
                            && e.getChannel().equals(event.getChannel()),
                    e -> channel.sendMessage("asked").queue());
        }

        if (content.toLowerCase().startsWith(prefix)) {
            TextCommand textCommand = TextCommand.find(content.substring(prefix.length()));
            if (textCommand != null && textCommand.isAllowed(message.getCategory())) {
                textCommand.execute(channel);
            }
        }
    }
}
