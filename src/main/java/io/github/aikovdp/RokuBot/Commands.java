package io.github.aikovdp.RokuBot;

import io.github.aikovdp.RokuBot.util.IssueUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.kohsuke.github.GHLabel;


import java.io.IOException;

import static io.github.aikovdp.RokuBot.util.EmbedUtil.*;

public class Commands extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        EmbedBuilder response = null;

        // GAME ENGINEER REFERENCE
        // Semantic Versioning
        if(content.startsWith(Main.prefix + "semver")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Semantic Versioning");
        }

        // Keep a Changelog
        if(content.startsWith(Main.prefix + "changelog")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Keep a Changelog");
        }

        // Material Names
        if(content.startsWith(Main.prefix + "material")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Material Names");
        }

        // Binary Search
        if(content.startsWith(Main.prefix + "binarysearch")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Binary Search");
        }

        // The "It Works" Fallacy
        if(content.startsWith(Main.prefix + "itworks")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "The \"It Works\" Fallacy");
        }

        // ASKING QUESTIONS
        // The XY Problem
        if(content.startsWith(Main.prefix + "xy")) {
            response = createGHInfoEmbed("asking-questions.md", "The XY Problem");
        }

        // Don't ask to ask
        if(content.startsWith(Main.prefix + "ask")) {
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
        if(content.startsWith(Main.prefix + "issues")) {
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
                    GHLabel label = Main.rokuRepo.getLabel(args[1]);
                    String issueList = IssueUtil.getIssueList(label);
                    if (issueList.isEmpty()) {
                        response = createErrorEmbed().setTitle("No Open Issues with label `" + label.getName() + "`");
                        response.setThumbnail("https://cdn.discordapp.com/attachments/786216721065050112/787721551285059614/issue-closed72px.png");
                        response.setColor(0xf85149);
                    } else {
                        response = createIssuesEmbed(issueList);
                        response.setTitle("Open issues with label `" + label.getName() + "`");
                        System.out.println(label.getColor());
                        response.setColor(Integer.parseInt(label.getColor(), 16));
                    }
                } catch (IOException e) {
                    response = createErrorEmbed();
                    response.setTitle("❌ Label `" + args[1] + "` does not exist");
                }
            }

        }

        if (response != null) {
            channel.sendMessage(response.build()).queue();
            response.clear();
        }
    }
}
