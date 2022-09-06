package com.rokucraft.rokubot.config;

import com.rokucraft.rokubot.entities.*;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ConfigSerializable @Source("settings.yml")
public record Config(
        @Source("secrets.yml") String botToken,
        String githubAppId,
        String githubOrganization,
        String botActivity,
        String defaultRepoName,
        String rulesFooter,
        Map<String, String> welcomeChannelMap,
        List<String> trustedServerIds,
        @Source("discord-invites.yml") List<DiscordInvite> publicInvites,
        @Source("discord-invites.yml") List<DiscordInvite> privateInvites,
        @Source("private-tags.yml") List<Tag> privateTags,
        @Source("plugins.yml") List<Plugin> plugins,
        @Source("repositories.yml") List<Repository> repositories,
        @Source("markdown-sections.yml") List<MarkdownSection> markdownSections,
        @Source("rules.yml") List<Rule> rules,
        @Source("root-tag-commands.yml") List<Tag> rootTagCommands,
        @Source("welcome-embeds.yml") List<MessageEmbed> welcomeEmbeds
) {
    public Config {
        privateInvites = new ArrayList<>(privateInvites);
        repositories = new ArrayList<>(repositories);
        repositories.addAll(
                plugins.stream()
                        .map(Plugin::repository)
                        .filter(Objects::nonNull)
                        .toList()
        );
        privateInvites.addAll(
                plugins.stream()
                        .map(Plugin::discordInvite)
                        .filter(Objects::nonNull)
                        .toList()
        );
    }
}
