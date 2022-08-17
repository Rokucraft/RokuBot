package com.rokucraft.rokubot.command.legacy;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.entities.MarkdownSection;
import com.rokucraft.rokubot.entities.Repository;
import com.rokucraft.rokubot.util.IssueUtil;
import com.rokucraft.rokubot.util.StaffOnly;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.kohsuke.github.GHLabel;

import java.io.IOException;

import static com.rokucraft.rokubot.Constants.ISSUE_CLOSED_COLOR;
import static com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed;
import static com.rokucraft.rokubot.util.EmbedUtil.createIssuesEmbed;

public class GHCommands extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (!event.isFromGuild() || event.getAuthor().isBot()) return;
        String prefix = RokuBot.getConfig().getPrefix();

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
                    GHLabel label = RokuBot.getDefaultRepo().getLabel(args[1]);
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
