package com.rokucraft.rokubot.commands;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.entities.MarkdownSection;
import com.rokucraft.rokubot.entities.Repository;
import com.rokucraft.rokubot.util.IssueUtil;
import com.rokucraft.rokubot.util.StaffOnly;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHUser;

import java.io.IOException;

import static com.rokucraft.rokubot.Constants.ISSUE_CLOSED_COLOR;
import static com.rokucraft.rokubot.Constants.ISSUE_OPEN_COLOR;
import static com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed;
import static com.rokucraft.rokubot.util.EmbedUtil.createIssuesEmbed;

public class GHCommands extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        String prefix = RokuBot.getConfig().prefix;

        Message message = event.getMessage();
        if (!StaffOnly.check(message)) return;
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        // Open Issues
        if (content.startsWith(prefix + "issues")) {
            EmbedBuilder response;
            String[] args = content.split("\\s+");

            if (IssueUtil.getIssueList().isBlank()) {
                response = createErrorEmbed()
                        .setTitle("❌ No Open Issues")
                        .setThumbnail("https://cdn.discordapp.com/attachments/786216721065050112/787721551285059614/issue-closed72px.png")
                        .setColor(ISSUE_CLOSED_COLOR);
            }

            else if (args.length == 1) {
                String issueList = IssueUtil.getIssueList();
                response = createIssuesEmbed(issueList);
            }

            else {
                try {
                    GHLabel label = RokuBot.defaultRepo.getLabel(args[1]);
                    String issueList = IssueUtil.getIssueList(label);
                    if (issueList.isEmpty()) {
                        response = createErrorEmbed()
                                .setTitle("No Open Issues with label `" + label.getName() + "`")
                                .setThumbnail("https://cdn.discordapp.com/attachments/786216721065050112/787721551285059614/issue-closed72px.png")
                                .setColor(ISSUE_CLOSED_COLOR);
                    } else {
                        response = createIssuesEmbed(issueList)
                                .setTitle("Open issues with label `" + label.getName() + "`")
                                .setColor(Integer.parseInt(label.getColor(), 16));
                    }
                } catch (IOException e) {
                    response = createErrorEmbed().setTitle("❌ Label `" + args[1] + "` does not exist");
                }
            }
            channel.sendMessageEmbeds(response.build()).queue();
            response.clear();
            return;
        }

        if (content.startsWith("#")) {
            String[] args = content.split("\\s+");
            try {
                int id = Integer.parseInt(args[0].substring(1));
                GHIssue issue = RokuBot.defaultRepo.getIssue(id);

                int color = (issue.getState() == GHIssueState.OPEN) ? ISSUE_OPEN_COLOR : ISSUE_CLOSED_COLOR;

                String thumbnailUrl = (issue.getState() == GHIssueState.OPEN) ? "https://cdn.discordapp.com/attachments/786216721065050112/787721554992824360/issue-opened72px.png" : "https://cdn.discordapp.com/attachments/786216721065050112/787721551285059614/issue-closed72px.png";
                GHUser author = issue.getUser();
                String authorName = (author.getName() == null) ? author.getLogin() : author.getName();
                String issueBody = issue.getBody()
                        .replaceAll("(?s)<!--.*?-->", "") // Removes HTML comments
                        .replaceAll("###(.*)", "**$1**") //Makes Markdown titles bold
                        .trim();
                if (issueBody.length() > 2048) {
                    issueBody = issueBody.substring(0, 2047).concat("…"); // Prevents reaching the char limit
                }

                EmbedBuilder builder = new EmbedBuilder();
                builder.setColor(color)
                        .setThumbnail(thumbnailUrl)
                        .setAuthor(authorName, String.valueOf(author.getHtmlUrl()), author.getAvatarUrl())
                        .setTitle(issue.getTitle(), String.valueOf(issue.getHtmlUrl()))
                        .setDescription(issueBody)
                        .setTimestamp(issue.getCreatedAt().toInstant());
                channel.sendMessageEmbeds(builder.build()).queue();
            } catch (IOException e) {
                channel.sendMessageEmbeds(createErrorEmbed().setTitle("Issue not found").build()).queue();
            } catch (NumberFormatException ignored) {
                // Not a parsable int, so the user didn't ask for an issue
            }
            return;
        }

        if (content.startsWith(prefix + "repo") || content.startsWith(prefix + "source")) {
            String[] args = content.split("\\s+");
            if (args.length == 1) {
                channel.sendMessage(Repository.getDefault().getRepositoryUrl()).queue();
            } else {
                Repository repository = Repository.find(args[1]);
                if (repository != null) {
                    channel.sendMessage(repository.getRepositoryUrl()).queue();
                } else {
                    MessageEmbed errorEmbed = createErrorEmbed()
                            .setTitle("Repository `" + args[1] + "` not found!")
                            .setDescription("Usage: `" + prefix + "repository [name]`")
                            .build();
                    channel.sendMessageEmbeds(errorEmbed).queue();
                }
            }
            return;
        }

        if (content.toLowerCase().startsWith(prefix)) {
            MarkdownSection markdownSection = MarkdownSection.find(content.substring(prefix.length()));
            if (markdownSection != null) {
                channel.sendMessageEmbeds(markdownSection.toEmbed(event.getAuthor().getAvatarUrl())).queue();
            }
        }
    }
}
