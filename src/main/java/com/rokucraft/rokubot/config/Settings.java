package com.rokucraft.rokubot.config;

import com.rokucraft.rokubot.commands.SlashMessageCommand;
import com.rokucraft.rokubot.config.serializers.ButtonSerializer;
import com.rokucraft.rokubot.config.serializers.MessageEmbedSerializer;
import com.rokucraft.rokubot.config.serializers.MessageSerializer;
import com.rokucraft.rokubot.config.serializers.SlashMessageCommandSerializer;
import com.rokucraft.rokubot.entities.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.util.CheckedSupplier;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class Settings {
    public String botToken;
    public String botActivity;
    public String prefix;
    @Setting("staff-category-ids")
    public List<String> staffCategoryIDs;
    public String githubLogin;
    @Setting("github-oauth")
    public String githubOAuth;
    public String defaultRepoName;
    public String rulesFooter;
    public Map<String, String> voiceChannelRoleMap;
    public Map<String, String> welcomeChannelMap;

    public final transient List<TextCommand> textCommands = getChecked(() ->
            nodeFromPath("text-commands.yml")
                    .node("text-commands")
                    .getList(TextCommand.class));
    public final transient List<DiscordInvite> discordInvites = getChecked(() ->
            nodeFromPath("discord-invites.yml")
                    .node("discord-invites")
                    .getList(DiscordInvite.class));
    public final transient List<Plugin> plugins = getChecked(() ->
            nodeFromPath("plugins.yml")
                    .node("plugins")
                    .getList(Plugin.class));
    public final transient List<Repository> repositories = getChecked(() ->
            nodeFromPath("repositories.yml")
                    .node("repositories")
                    .getList(Repository.class));
    public final transient List<MarkdownSection> markdownSections = getChecked(() ->
            nodeFromPath("markdown-sections.yml")
                    .node("markdown-sections")
                    .getList(MarkdownSection.class));
    public final transient List<Rule> rules = getChecked(() ->
            nodeFromPath("rules.yml")
                    .node("rules")
                    .getList(Rule.class));
    public final transient List<SlashMessageCommand> slashMessageCommands = getChecked(() ->
            nodeFromPath("slash-message-commands.yml")
                    .node("slash-message-commands")
                    .getList(SlashMessageCommand.class));
    public final transient List<MessageEmbed> welcomeEmbeds = getChecked(() ->
            nodeFromPath("welcome-embeds.yml")
                    .node("welcome-embeds")
                    .getList(MessageEmbed.class));

    public Settings() {
        if (plugins != null) {
            for (Plugin plugin : plugins) {
                if (plugin.getDiscordInviteCode() != null && discordInvites != null) {
                    discordInvites.add(new DiscordInvite(plugin.getName(), plugin.getAliases(), true, plugin.getDiscordInviteCode()));
                }
                if (plugin.getRepositoryUrl() != null && repositories != null) {
                    repositories.add(new Repository(plugin.getName(), plugin.getAliases(), true, plugin.getRepositoryUrl()));
                }
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
                        builder -> builder.register(Message.class, MessageSerializer.INSTANCE)
                                .register(MessageEmbed.class, MessageEmbedSerializer.INSTANCE)
                                .register(SlashMessageCommand.class, SlashMessageCommandSerializer.INSTANCE)
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
}
