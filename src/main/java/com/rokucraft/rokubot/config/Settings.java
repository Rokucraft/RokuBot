package com.rokucraft.rokubot.config;

import com.rokucraft.rokubot.config.serializers.ButtonSerializer;
import com.rokucraft.rokubot.config.serializers.MessageCreateDataSerializer;
import com.rokucraft.rokubot.config.serializers.MessageEmbedSerializer;
import com.rokucraft.rokubot.entities.*;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.util.CheckedSupplier;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
@ConfigSerializable
public class Settings {
    private String githubAppId;
    private String githubOrganization;
    private String botActivity;
    private String prefix;
    @Setting("staff-category-ids")
    private List<String> staffCategoryIDs;
    private String defaultRepoName;
    private String rulesFooter;
    private Map<String, String> voiceChannelRoleMap;
    private Map<String, String> welcomeChannelMap;
    private List<String> trustedServerIds;

    private final transient List<Tag> privateTags = getCheckedList(() ->
            nodeFromPath("private-tags.yml")
                    .node("private-tags")
                    .getList(Tag.class));
    private final transient List<DiscordInvite> publicInvites = getCheckedList(() ->
            nodeFromPath("discord-invites.yml")
                    .node("public-invites")
                    .getList(DiscordInvite.class));
    private final transient List<DiscordInvite> privateInvites = getCheckedList(() ->
            nodeFromPath("discord-invites.yml")
                    .node("private-invites")
                    .getList(DiscordInvite.class));
    private final transient List<Plugin> plugins = getCheckedList(() ->
            nodeFromPath("plugins.yml")
                    .node("plugins")
                    .getList(Plugin.class));
    private final transient List<Repository> repositories = getCheckedList(() ->
            nodeFromPath("repositories.yml")
                    .node("repositories")
                    .getList(Repository.class));
    private final transient List<MarkdownSection> markdownSections = getCheckedList(() ->
            nodeFromPath("markdown-sections.yml")
                    .node("markdown-sections")
                    .getList(MarkdownSection.class));
    private final transient List<Rule> rules = getCheckedList(() ->
            nodeFromPath("rules.yml")
                    .node("rules")
                    .getList(Rule.class));
    private final transient List<Tag> rootTags = getCheckedList(() ->
            nodeFromPath("root-tag-commands.yml")
                    .node("root-tag-commands")
                    .getList(Tag.class));
    private final transient List<MessageEmbed> welcomeEmbeds = getCheckedList(() ->
            nodeFromPath("welcome-embeds.yml")
                    .node("welcome-embeds")
                    .getList(MessageEmbed.class));

    private final transient Secrets secrets = getChecked(() -> nodeFromPath("secrets.yml").get(Secrets.class));

    @ConfigSerializable
    private record Secrets(String botToken) {}

    public Settings() {
        for (Plugin plugin : getPlugins()) {
            if (plugin.getDiscordInviteCode() != null) {
                this.privateInvites.add(new DiscordInvite(plugin.getName(), plugin.getDiscordInviteCode()));
            }
            if (plugin.getRepositoryUrl() != null) {
                this.repositories.add(new Repository(plugin.getName(), plugin.getRepositoryUrl()));
            }
        }
    }

    /**
     * Attempts to load a {@link ConfigurationNode} from the defined source.
     *
     * <p>The resultant node represents the root of the configuration being
     * loaded.
     *
     * @param path The path string of the configuration file
     * @return the newly constructed node
     * @throws ConfigurateException if any sort of error occurs with reading or parsing the configuration
     */
    @NonNull
    private static ConfigurationNode nodeFromPath(String path) throws ConfigurateException {
        return YamlConfigurationLoader.builder()
                .path(Path.of(path))
                .defaultOptions(options -> options.serializers(
                        builder -> builder.register(MessageCreateData.class, MessageCreateDataSerializer.INSTANCE)
                                .register(MessageEmbed.class, MessageEmbedSerializer.INSTANCE)
                                .register(Button.class, ButtonSerializer.INSTANCE)
                )).build().load();
    }

    /**
     * Gets a result from a {@link CheckedSupplier} that can throw a {@link ConfigurateException}.
     * @param supplier The supplier whose result to get
     * @param <T> the type of results supplied by this supplier
     * @return a result from the supplier, or null in case of a {@link ConfigurateException}
     */
    private static <T> @Nullable T getChecked(@NonNull CheckedSupplier<T, ConfigurateException> supplier) {
        try {
            return supplier.get();
        } catch (ConfigurateException e) {
            System.err.println("An error occurred while loading the configuration" + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            return null;
        }
    }


    /**
     * Gets a result from a {@link CheckedSupplier} that supplies a list and can throw a {@link ConfigurateException}.
     * @param supplier The supplier whose result to get
     * @param <T> the type of elements in the list supplied by this supplier
     * @return a resulting list from the supplier
     */
    private static <T> @NonNull List<T> getCheckedList(@NonNull CheckedSupplier<List<T>, ConfigurateException> supplier) {
        return Objects.requireNonNullElse(getChecked(supplier), new ArrayList<>());
    }

    public String getBotToken() {
        return secrets.botToken();
    }

    public String getBotActivity() {
        return botActivity;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getStaffCategoryIDs() {
        return staffCategoryIDs;
    }

    public String getGithubAppId() {
        return githubAppId;
    }

    public String getGithubOrganization() {
        return githubOrganization;
    }

    public String getDefaultRepoName() {
        return defaultRepoName;
    }

    public String getRulesFooter() {
        return rulesFooter;
    }

    public Map<String, String> getVoiceChannelRoleMap() {
        return voiceChannelRoleMap;
    }

    public Map<String, String> getWelcomeChannelMap() {
        return welcomeChannelMap;
    }

    public List<String> getTrustedServerIds() {
        return trustedServerIds;
    }

    public List<Tag> getPrivateTags() {
        return privateTags;
    }

    public List<DiscordInvite> getPublicInvites() {
        return publicInvites;
    }

    public List<DiscordInvite> getPrivateInvites() {
        return privateInvites;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public List<MarkdownSection> getMarkdownSections() {
        return markdownSections;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<Tag> getRootTags() {
        return rootTags;
    }

    public List<MessageEmbed> getWelcomeEmbeds() {
        return welcomeEmbeds;
    }
}
