package com.rokucraft.rokubot

import com.rokucraft.rokubot.command.CommandManager
import com.rokucraft.rokubot.command.commands.*
import com.rokucraft.rokubot.config.Config
import com.rokucraft.rokubot.entities.Repository
import com.rokucraft.rokubot.entities.Tag
import com.rokucraft.rokubot.listeners.JoinListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

class RokuBot @Inject constructor(
    private val jda: JDA,
    private val github: GitHub,
    private val config: Config
) {
    private val repositoryCache: List<GHRepository> = github.getOrganization(config.githubOrganization)
        .listRepositories().toList()
        .filterNot { it.isArchived }
        .sortedByDescending { it.updatedAt }

    init {
        if (config.botActivity != null) {
            jda.presence.activity = Activity.playing(config.botActivity)
        }
        jda.addEventListener(JoinListener(config.welcomeChannelMap, config.welcomeEmbeds))

        val commandManager = CommandManager()
        initCommands(commandManager)
        commandManager.register(jda)
    }

    private fun initCommands(commandManager: CommandManager) {
        commandManager.addCommands(
            RuleCommand(config.rules, config.rulesFooter),
            RollCommand(),
            UUIDCommand()
        )

        val publicInvites = config.publicInvites

        if (publicInvites.isNotEmpty()) {
            commandManager.addCommands(InviteCommand("invite", publicInvites, publicInvites[0], true))
        }
        commandManager.addCommands(config.rootTagCommands.map { RootTagCommand(it) }.toList())

        val privateInvites = config.privateInvites

        val repositories = config.repositories

        val tags: MutableList<Tag> = config.privateTags.toMutableList()
        tags.addAll(config.markdownSections.map { it.toTag(this.github) })

        try {
            jda.awaitReady()
            config.trustedServerIds.mapNotNull { jda.getGuildById(it) }
                .forEach { guild ->
                    commandManager.addGuildCommands(
                        guild,
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
                        val repos = repositories + repositoryCache.map { Repository.of(it) }

                        commandManager.addGuildCommands(
                            guild,
                            RepoCommand(repos, repositories[0])
                        )
                    }
                }
        } catch (e: InterruptedException) {
            LOGGER.error("Thread was interrupted while waiting for JDA to be ready", e)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(RokuBot::class.java)
    }
}
