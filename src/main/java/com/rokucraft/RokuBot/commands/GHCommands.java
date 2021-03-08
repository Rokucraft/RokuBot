package com.rokucraft.RokuBot.commands;

import com.rokucraft.RokuBot.Main;
import com.rokucraft.RokuBot.Settings;
import com.rokucraft.RokuBot.util.IssueUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.kohsuke.github.GHLabel;

import java.io.IOException;

import static com.rokucraft.RokuBot.util.EmbedUtil.*;

public class GHCommands extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        if (!Settings.staffCategoryIDs.contains(message.getCategory().getId())) return;
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        EmbedBuilder response = null;

        // GAME ENGINEER REFERENCE
        // Semantic Versioning
        if (content.startsWith(Settings.prefix + "semver")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Semantic Versioning");
        }

        // Keep a Changelog
        if (content.startsWith(Settings.prefix + "changelog")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Keep a Changelog");
        }

        // Material Names
        if (content.startsWith(Settings.prefix + "material")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Material Names");
        }

        // Binary Search
        if (content.startsWith(Settings.prefix + "binarysearch")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Binary Search");
        }

        // The "It Works" Fallacy
        if (content.startsWith(Settings.prefix + "itworks")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "The \"It Works\" Fallacy");
        }

        // ASKING QUESTIONS
        // The XY Problem
        if (content.startsWith(Settings.prefix + "xy")) {
            response = createGHInfoEmbed("asking-questions.md", "The XY Problem");
        }

        // Don't ask to ask
        if (content.startsWith(Settings.prefix + "ask")) {
            response = createGHInfoEmbed("asking-questions.md", "Don't ask to ask");
            response.setDescription("> Every now and then, in online chat rooms I hang around in, someone pops in and says something in the lines of,\n" +
                    "> ```\n" +
                    "> Foobar123:Any Java experts around?\n" +
                    "> ```\n" +
                    "> This is bad form, for several reasons. What the person is actually asking here is,\n" +
                    "> ```\n" +
                    "> Foobar123:Any Java experts around who are willing to commit into looking into my problem, whatever that may turn out to be, even if it's not actually related to Java or if someone who doesn't know anything about Java could actually answer my question?\n" +
                    "> ```\n" +
                    "> There are plenty of reasons why people who DO have the knowledge would not admit to it. By asking, you're asking for more than what you think you're asking.\n" +
                    "> \n" +
                    "> You're asking people to take responsibility. You're questioning people's confidence in their abilities. You're also unnecessarily walling other people out. I often answer questions related to languages or libraries I have never used, because the answers are (in a programmer kind of way) common sense.\n" +
                    "> \n" +
                    "> Alternatively, it can be seen as..\n" +
                    "> ```\n" +
                    "> Foobar123:I have a question about Java but I'm too lazy to actually formalize it in words unless there's someone on the channel who might be able to answer it\n" +
                    "> ```\n" +
                    "> ..which is just lazy. If you're not willing to do the work to solve your problem, why should we?\n" +
                    "> \n" +
                    "> The solution is not to ask to ask, but just to ask. Someone who is idling on the channel and only every now and then glances what's going on is unlikely to answer to your \"asking to ask\" question, but your actual problem description may pique their interest and get them to answer.\n" +
                    "> \n" +
                    "> So, to summarize, don't ask *\"Any Java experts around?\"*, but rather ask *\"How do I do [problem] with Java and [other relevant info]?\"*");
                    response.setFooter("");
        }

        // Open Issues
        if (content.startsWith(Settings.prefix + "issues")) {
            String[] args = content.split("\\s+");

            if (Main.openIssues.isEmpty()) {
                response = createErrorEmbed().setTitle("❌ No Open Issues");
                response.setThumbnail("https://cdn.discordapp.com/attachments/786216721065050112/787721551285059614/issue-closed72px.png");
                response.setColor(0xf85149);
            }

            else if (args.length == 1) {
                String issueList = IssueUtil.getIssueList();
                response = createIssuesEmbed(issueList);
            }

            else {
                try {
                    GHLabel label = Main.defaultRepo.getLabel(args[1]);
                    String issueList = IssueUtil.getIssueList(label);
                    if (issueList.isEmpty()) {
                        response = createErrorEmbed().setTitle("No Open Issues with label `" + label.getName() + "`");
                        response.setThumbnail("https://cdn.discordapp.com/attachments/786216721065050112/787721551285059614/issue-closed72px.png");
                        response.setColor(0xf85149);
                    } else {
                        response = createIssuesEmbed(issueList);
                        response.setTitle("Open issues with label `" + label.getName() + "`");
                        response.setColor(Integer.parseInt(label.getColor(), 16));
                    }
                } catch (IOException e) {
                    response = createErrorEmbed();
                    response.setTitle("❌ Label `" + args[1] + "` does not exist");
                }
            }

        }

        if (content.startsWith(Settings.prefix + "reference")) {
            response = new EmbedBuilder().setTitle("Game Engineer Reference");
            try {
                response.setTitle("Game Engineer Reference", Main.defaultRepo.getFileContent("game-engineer-reference.md").getHtmlUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.setDescription("The Game Engineer Reference document contains all sort of useful information for GEs. " +
                    "All Game Engineers should read through this at least once.");
            response.setColor(0x0FFFFF);
            response.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f4da.png");
            response.setFooter("Click the title to open the document", message.getAuthor().getAvatarUrl());
        }

        if (content.startsWith(Settings.prefix + "questions")) {
            response = createGHInfoEmbed("asking-questions.md", "On the Subject of");
            try {
                response.setTitle("On the Subject of Asking Questions", Main.defaultRepo.getFileContent("asking-questions.md").getHtmlUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2753.png");
            response.setFooter("Click the title to open the document", message.getAuthor().getAvatarUrl());
        }

        if (content.startsWith(Settings.prefix + "symbols")) {
            response = createGHInfoEmbed("symbols.md", "List of Symbols");
            try {
                response.setTitle("List of Symbols", Main.defaultRepo.getFileContent("symbols.md").getHtmlUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/269c.png");
            response.setFooter("Click the title to open the list", message.getAuthor().getAvatarUrl());
        }

        if (response != null) {
            channel.sendMessage(response.build()).queue();
            response.clear();
        }
    }
}
