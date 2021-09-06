package com.rokucraft.RokuBot.commands;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.rokucraft.RokuBot.Main;
import com.rokucraft.RokuBot.config.Settings;
import com.rokucraft.RokuBot.entities.DiscordInvite;
import com.rokucraft.RokuBot.entities.TextCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static com.rokucraft.RokuBot.Constants.GREEN;
import static com.rokucraft.RokuBot.util.EmbedUtil.createErrorEmbed;
import static com.rokucraft.RokuBot.util.EmbedUtil.createInfoEmbed;

public class BaseCommands extends ListenerAdapter {

    EventWaiter waiter;

    public BaseCommands(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        if (content.startsWith(Settings.prefix + "help") && Settings.staffCategoryIDs.contains(message.getCategory().getId()))  {
            EmbedBuilder response = createInfoEmbed()
                    .setTitle(event.getGuild().getSelfMember().getEffectiveName() + " Help")
                    .setFooter("Made by " + Main.botOwner.getName(), Main.botOwner.getAvatarUrl())
                    .addField("Plugin Commands",
                        ":octopus: `" + Settings.prefix + "plugin <name>` shows all info for the named plugin\n" +
                                ":lizard: `" + Settings.prefix + "version <name>` shows version info for the named plugin\n" +
                                ":giraffe: `" + Settings.prefix + "download <name>` shows the download link for the named plugin\n" +
                                ":owl: `" + Settings.prefix + "docs <name>` shows documentation links for the named plugin\n" +
                                ":sloth: `" + Settings.prefix + "dependencies <name>` lists dependencies for the named plugin\n" +
                                ":snail: `" + Settings.prefix + "invite <name>` shows a discord invite for the named plugin",
                        false)
                    .addField("GitHub Commands",
                        ":exclamation: `" + Settings.prefix + "issues [label]` lists all issues (with the specified label)\n" +
                                ":books: `" + Settings.prefix + "reference` shows a link to the GE reference\n" +
                                ":question: `" + Settings.prefix + "questions` shows a link to the asking-questions document\n" +
                                ":fleur_de_lis: `" + Settings.prefix + "symbols` shows a link to the list with symbols",
                        false);

            StringBuilder textCommandsHelpBuilder = new StringBuilder();
            for (TextCommand textCommand : Settings.textCommandList) {
                if (textCommand.isAllowed(message.getCategory())) {
                    String description = textCommand.getDescription();
                    if (description == null) {description = "";}
                    textCommandsHelpBuilder.append("• `").append(Settings.prefix).append(textCommand.getName()).append("` ").append(description).append("\n");
                }
            }
            String textCommandsHelp = textCommandsHelpBuilder.toString();

            response.addField("Utility Commands", textCommandsHelp, false);

            channel.sendMessage(response.build()).queue();
            response.clear();
            return;
        }

        if (content.startsWith(Settings.prefix + "discord")) {
            String[] args = content.split("\\s+");
            if (args.length == 1) {
                channel.sendMessage(DiscordInvite.find("default").getInviteUrl()).queue();
            } else {
                DiscordInvite discordInvite = DiscordInvite.find(args[1]);
                if (discordInvite != null && discordInvite.isAllowed(message.getCategory())) {
                    channel.sendMessage(discordInvite.getInviteUrl()).queue();
                } else {
                    MessageEmbed errorEmbed = createErrorEmbed()
                            .setTitle("Discord server `" + args[1] + "` not found!")
                            .setDescription("Usage: `" + Settings.prefix + "invite [name]`")
                            .build();
                    channel.sendMessage(errorEmbed).queue();
                }
            }
            return;
        }

        if (content.startsWith(Settings.prefix + "reload") && Settings.staffCategoryIDs.contains(message.getCategory().getId())) {
            Settings.load();
            Settings.loadTextCommands();
            Settings.loadDiscordInvites();
            Settings.loadRepositories();
            Settings.loadPlugins();
            Settings.loadMarkdownSections();
            Settings.loadRules();
            Settings.loadWelcomeEmbeds();
            EmbedBuilder response = new EmbedBuilder().setColor(GREEN).setTitle("Successfully reloaded!");
            channel.sendMessage(response.build()).queue();
        }

        if (content.toLowerCase().startsWith("all my homies ")) {
            channel.sendMessage("who").queue();
            waiter.waitForEvent(GuildMessageReceivedEvent.class,
                    e -> e.getAuthor().equals(event.getAuthor())
                            && e.getChannel().equals(event.getChannel()),
                    e -> channel.sendMessage("asked").queue());
        }

        if (content.toLowerCase().startsWith(Settings.prefix)) {
            TextCommand textCommand = TextCommand.find(content.substring(Settings.prefix.length()));
            if (textCommand != null && textCommand.isAllowed(message.getCategory())) {
                textCommand.execute(channel);
            }
        }
    }
}
