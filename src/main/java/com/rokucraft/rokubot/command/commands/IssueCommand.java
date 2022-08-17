package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.AutoCompletable;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.List;

import static com.rokucraft.rokubot.Constants.ISSUE_CLOSED_COLOR;
import static com.rokucraft.rokubot.Constants.ISSUE_OPEN_COLOR;

public class IssueCommand implements SlashCommand, AutoCompletable {
    private final @NonNull CommandData data;
    private final @NonNull GitHub github;
    private final @Nullable List<GHRepository> repositoryCache;
    private final @Nullable String defaultRepoName;
    public static final String ICON_ISSUE_OPEN =
            "https://cdn.discordapp.com/attachments/786216721065050112/787721554992824360/issue-opened72px.png";
    public static final String ICON_ISSUE_CLOSED =
            "https://cdn.discordapp.com/attachments/786216721065050112/787721551285059614/issue-closed72px.png";

    public IssueCommand(
            @NonNull GitHub github,
            @Nullable List<GHRepository> repositoryCache,
            @Nullable String defaultRepoName
    ) {
        this.github = github;
        this.repositoryCache = repositoryCache;
        this.defaultRepoName = defaultRepoName;
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
            String repo = event.getOption("repo", this.defaultRepoName, OptionMapping::getAsString);
            GHIssue issue = this.github.getRepository(repo).getIssue(number);
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
        if (repositoryCache == null) {
            event.replyChoices().queue();
            return;
        }
        event.replyChoices(
                this.repositoryCache
                        .stream()
                        .filter(GHRepository::hasIssues)
                        .filter(r -> r.getName().toLowerCase().contains(query))
                        .limit(25)
                        .map(r -> new Choice(r.getName(), r.getFullName()))
                        .toList()
        ).queue();
    }

    @NonNull
    private MessageEmbed createIssueEmbed(@NonNull GHIssue issue) throws IOException {
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

    @Override @NonNull
    public CommandData getData(Guild guild) {
        return this.data;
    }
}
