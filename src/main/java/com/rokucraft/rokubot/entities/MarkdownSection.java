package com.rokucraft.rokubot.entities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static com.rokucraft.rokubot.ColorConstants.BLUE;

@ConfigSerializable
public record MarkdownSection (
    @Required String name,
    @Required String title,
    @Required String repoName,
    @Required String filePath,
    @Nullable String thumbnailUrl,
    @Nullable String url,
    @Nullable String description
){
    public static final String INFO_ICON =
            "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png";

    public @NonNull Tag toTag(@NonNull GitHub gitHub) throws IOException {
        return new Tag(
                name,
                description,
                new MessageCreateBuilder().addEmbeds(toEmbed(gitHub)).build()
        );
    }

    public @NonNull MessageEmbed toEmbed(@NonNull GitHub gitHub) throws IOException {
        return new EmbedBuilder()
                    .setColor(BLUE)
                    .setTitle(title.replaceAll("#+ ", ""), url)
                    .setThumbnail(thumbnailUrl != null ? thumbnailUrl : INFO_ICON)
                    .setDescription(getContents(gitHub))
                    .build();
    }

    public @NonNull String getContents(@NonNull GitHub gitHub) throws IOException {
        GHRepository repository = gitHub.getRepository(repoName);
        try (var reader = new BufferedReader(
                new InputStreamReader(repository.getFileContent(filePath).read(), StandardCharsets.UTF_8)
        )) {
             return reader.lines()
                     .dropWhile(line -> !line.contains(title)) // Remove everything before title
                     .skip(1) // Remove title
                     .takeWhile(line -> !line.matches("#+ .*")) // Take until next title
                     .collect(Collectors.joining("\n"));
        }
    }
}
