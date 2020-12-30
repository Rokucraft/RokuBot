package io.github.aikovdp.RokuBot.commands;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.aikovdp.RokuBot.Main;
import io.github.aikovdp.RokuBot.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static io.github.aikovdp.RokuBot.util.EmbedUtil.createErrorEmbed;
import static io.github.aikovdp.RokuBot.util.EmbedUtil.createInfoEmbed;

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
        EmbedBuilder response = null;


        if (content.startsWith(Settings.prefix + "help")) {
            String botNickname = event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).getNickname();
            String botName = botNickname != null ? botNickname : event.getJDA().getSelfUser().getName();
            response = createInfoEmbed();
            response.setTitle(botName + " Help");
            response.setFooter("Made by " + Main.botOwner.getName(),
                    Main.botOwner.getAvatarUrl());

            if (Settings.staffCategoryIDs.contains(message.getCategory().getId())) {
                response.addField("Plugin Commands",
                        ":octopus: `" + Settings.prefix + "plugin <name>` shows all info for the named plugin\n" +
                                ":lizard: `" + Settings.prefix + "version <name>` shows version info for the named plugin\n" +
                                ":giraffe: `" + Settings.prefix + "download <name>` shows the download link for the named plugin\n" +
                                ":owl: `" + Settings.prefix + "docs <name>` shows documentation links for the named plugin\n" +
                                ":sloth: `" + Settings.prefix + "dependencies <name>` lists dependencies for the named plugin\n" +
                                ":door: `" + Settings.prefix + "discord <name>` shows a discord invite for the named plugin",
                        false);
                response.addField("GitHub Commands",
                        ":exclamation: `" + Settings.prefix + "issues [label]` lists all issues (with the specified label)\n" +
                                ":books: `" + Settings.prefix + "reference` shows a link to the GE reference\n" +
                                ":question: `" + Settings.prefix + "questions` shows a link to the asking-questions document\n" +
                                ":fleur_de_lis: `" + Settings.prefix + "symbols` shows a link to the list with symbols",
                        false);
            }
            response.addField("Utility Commands",
                    "• `" + Settings.prefix + "invite [water|earth|fire|air]` shows a discord invite link for the named nation\n" +
                            "• `" + Settings.prefix + "optifine` shows a download link for OptiFine\n" +
                            "• `" + Settings.prefix + "java` shows a download link for Java\n" +
                            "• `" + Settings.prefix + "cracks` shows information about cracked clients",
                    false);
        }

        if (content.startsWith(Settings.prefix + "java")) {
            response = createInfoEmbed();
            response.setTitle("Download Java", "https://www.java.com/en/download/");
            response.setDescription("If someone linked you to this, you probably have to install Java. You can download it here:\n" +
                    "https://www.java.com/en/download/");
        }

        if (content.startsWith(Settings.prefix + "optifine")) {
            response = createInfoEmbed();
            response.setTitle("Download OptiFine", "https://optifine.net/adloadx?f=OptiFine_1.13.2_HD_U_F5.jar");
            response.setDescription("Using OptiFine on our server is highly recommended. " +
                    "Not only will it greatly improve your performance, but you will also be able to see many more of our custom models and textures. \n" +
                    "You can download the 1.13.2 version here:\n" +
                    "https://optifine.net/adloadx?f=OptiFine_1.13.2_HD_U_F5.jar");
        }

        if (content.startsWith(Settings.prefix + "cracks")) {
            response = createInfoEmbed();
            response.setTitle("Cracks");
            response.setDescription("Cracked versions of Minecraft are not able to join our server and never will be.\n" +
                    "Minecraft cracks are illegal software piracy. " +
                    "Creating, downloading, using, possessing, or distributing a Minecraft crack is a **crime**.\n\n" +
                    "You can buy a legal copy of Minecraft here:" +
                    " https://www.minecraft.net/store/minecraft-java-edition");
            response.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/26a0.png");
        }

        if (content.startsWith(Settings.prefix + "invite")) {
            String[] args = content.split("\\s+");
            if (args.length > 1) {
                switch (args[1]) {
                    case "water":
                        channel.sendMessage("https://discord.gg/KCkVJFx").queue();
                        break;
                    case "earth":
                        channel.sendMessage("https://discord.gg/WH4wVwB").queue();
                        break;
                    case "fire":
                        channel.sendMessage("https://discord.gg/E2XZ9qW").queue();
                        break;
                    case "air":
                        channel.sendMessage("https://discord.gg/XEqjG2Q").queue();
                        break;
                    default:
                        response = createErrorEmbed();
                        response.setTitle("Incorrect Usage");
                        response.setDescription("Usage: `" + Settings.prefix + "invite [water|earth|fire|air]");
                        break;
                }
            } else {
                channel.sendMessage("https://discord.gg/7WNFu3v").queue();
            }
        }

        if (content.toLowerCase().startsWith("all my homies ")) {
            channel.sendMessage("who").queue();
            waiter.waitForEvent(GuildMessageReceivedEvent.class,
                    e -> e.getAuthor().equals(event.getAuthor())
                            && e.getChannel().equals(event.getChannel()),
                    e -> channel.sendMessage("asked").queue());
        }

        if (response != null) {
            channel.sendMessage(response.build()).queue();
            response.clear();
        }
    }
}
