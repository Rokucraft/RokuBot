package com.rokucraft.rokubot

import com.rokucraft.rokubot.command.CommandManager
import com.rokucraft.rokubot.command.commands.*
import com.rokucraft.rokubot.config.Config
import com.rokucraft.rokubot.entities.Repository
import com.rokucraft.rokubot.entities.Tag
import com.rokucraft.rokubot.listeners.JoinListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject

class RokuBot @Inject constructor(
    private val jda: JDA,
    private val github: GitHub,
    private val config: Config
) {
    private var repositoryCache: List<GHRepository> = mutableListOf()
    private var commandManager: CommandManager = CommandManager(jda)

    init {
        if (config.botActivity != null) {
            jda.presence.activity = Activity.playing(config.botActivity)
        }
        jda.addEventListener(JoinListener(config.welcomeChannelMap, config.welcomeEmbeds))

        initRepoCache()
        initCommands()
    }

    private fun initCommands() {
        commandManager.addCommands(
            RuleCommand(config.rules, config.rulesFooter),
            RollCommand(),
            UUIDCommand()
        )

        val publicInvites = config.publicInvites

        if (publicInvites.isNotEmpty()) {
            commandManager.addCommands(InviteCommand("invite", publicInvites, publicInvites[0], true))
        }
        commandManager.addCommands(config.rootTagCommands.stream().map { tag: Tag? ->
            RootTagCommand(
                tag!!
            )
        }.toList())

        val privateInvites = config.privateInvites

        val repositories = config.repositories

        val tags: MutableList<Tag> = ArrayList(
            config.privateTags
        )
        tags.addAll(
            config.markdownSections
                .map { section ->
                    try {
                        return@map section.toTag(this.github)
                    } catch (e: IOException) {
                        LOGGER.error("Unable to get contents for $section", e)
                        return@map null
                    }
                }
                .filterNotNull()
        )

        try {
            jda.awaitReady()
            config.trustedServerIds.stream()
                .map<Guild?> { id: String? ->
                    jda.getGuildById(
                        id!!
                    )
                }
                .filter { obj: Guild? -> Objects.nonNull(obj) }
                .forEach { guild: Guild? ->
                    commandManager.addGuildCommands(
                        guild!!,
                        PluginCommand(config.plugins),
                        IssueCommand(this.github, this.repositoryCache, config.defaultRepoName),
                        TagCommand(tags)
                    )
                    if (privateInvites.isNotEmpty()) {
                        commandManager.addGuildCommands(
                            guild,
                            InviteCommand("discord", privateInvites, null, false)
                        )
                    }
                    if (repositories.isNotEmpty() || repositoryCache.isNotEmpty()) {
                        val repos = Stream.concat(
                            repositoryCache.stream().map { Repository.of(it) },
                            repositories.stream()
                        )
                            .toList()
                        commandManager.addGuildCommands(
                            guild,
                            RepoCommand(repos, repositories[0])
                        )
                    }
                }
        } catch (e: InterruptedException) {
            LOGGER.error("Thread was interrupted while waiting for JDA to be ready", e)
        }
        commandManager.registerCommands()
    }

    private fun initRepoCache() {
        this.repositoryCache = github.getOrganization(config.githubOrganization)
            .listRepositories().toList()
            .filterNot { it.isArchived }
            .sortedByDescending { it.updatedAt }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(RokuBot::class.java)
    }
}
