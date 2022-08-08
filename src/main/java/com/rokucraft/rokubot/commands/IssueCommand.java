package com.rokucraft.rokubot.commands;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

import java.io.IOException;

import static com.rokucraft.rokubot.Constants.ISSUE_CLOSED_COLOR;
import static com.rokucraft.rokubot.Constants.ISSUE_OPEN_COLOR;

public class IssueCommand extends Command {
    public static final String ICON_ISSUE_OPEN =
            "https://cdn.discordapp.com/attachments/786216721065050112/787721554992824360/issue-opened72px.png";
    public static final String ICON_ISSUE_CLOSED =
            "https://cdn.discordapp.com/attachments/786216721065050112/787721551285059614/issue-closed72px.png";

    public IssueCommand() {
        setGuildOnly(true);
        this.data = Commands.slash("issue", "Preview an issue")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                .addOptions(
                        new OptionData(OptionType.INTEGER, "number", "The issue number")
                                .setRequiredRange(1, Integer.MAX_VALUE)
                )
                .addOption(OptionType.STRING, "repo", "The repository the issue belongs to", false,true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        try {
            Integer number = event.getOption("number", OptionMapping::getAsInt);
            if (number == null) {
                throw new IllegalArgumentException("You must enter a number");
            }
            String repo = event.getOption("repo", RokuBot.getConfig().getDefaultRepoName(), OptionMapping::getAsString);
            GHIssue issue = RokuBot.getGithub().getRepository(repo).getIssue(number);
            event.replyEmbeds(createIssueEmbed(issue)).queue();
        } catch (IOException e) {
            event.replyEmbeds(EmbedUtil.createErrorEmbed("Unable to find issue")).setEphemeral(true).queue();
        } catch (IllegalArgumentException e) {
            event.replyEmbeds(EmbedUtil.createErrorEmbed(e.getMessage())).setEphemeral(true).queue();
        }
    }

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {
        String query = event.getFocusedOption().getValue().toLowerCase();
        event.replyChoices(
                RokuBot.getRepositoryCache()
                        .stream()
                        .filter(GHRepository::hasIssues)
                        .filter(r -> r.getName().toLowerCase().contains(query))
                        .limit(25)
                        .map(r -> new Choice(r.getName(), r.getFullName()))
                        .toList()
        ).queue();
    }

    private MessageEmbed createIssueEmbed(GHIssue issue) throws IOException {
        boolean open = issue.getState() == GHIssueState.OPEN;
        GHUser author = issue.getUser();
        String authorName = (author.getName() != null) ? author.getName() : author.getLogin();
        String issueBody = issue.getBody()
                .replaceAll("(?s)<!--.*?-->", "") // Removes HTML comments
                .replaceAll("###(.*)", "**$1**") //Makes Markdown titles bold
                .trim();
        if (issueBody.length() > MessageEmbed.DESCRIPTION_MAX_LENGTH) {
            issueBody = issueBody.substring(0, MessageEmbed.DESCRIPTION_MAX_LENGTH - 1).concat("…");
        }
        return new EmbedBuilder()
                .setColor(open ? ISSUE_OPEN_COLOR : ISSUE_CLOSED_COLOR)
                .setThumbnail(open ? ICON_ISSUE_OPEN : ICON_ISSUE_CLOSED)
                .setAuthor(authorName, author.getHtmlUrl().toString(), author.getAvatarUrl())
                .setTitle(issue.getTitle(), issue.getHtmlUrl().toString())
                .setDescription(issueBody)
                .setTimestamp(issue.getCreatedAt().toInstant())
                .build();
    }
}
