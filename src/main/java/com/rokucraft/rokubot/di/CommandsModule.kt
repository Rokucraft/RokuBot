package com.rokucraft.rokubot.di

import com.rokucraft.rokubot.command.CommandManager
import com.rokucraft.rokubot.command.addCommands
import com.rokucraft.rokubot.command.addGuildCommands
import com.rokucraft.rokubot.command.commands.*
import com.rokucraft.rokubot.config.Config
import com.rokucraft.rokubot.entities.Repository
import com.rokucraft.rokubot.entities.Tag
import com.rokucraft.rokubot.github.RepositoryCache
import dagger.Module
import dagger.Provides
import net.dv8tion.jda.api.JDA
import org.kohsuke.github.GitHub

@Module
object CommandsModule {

    @Provides
    fun provideCommandManager(
        config: Config,
        jda: JDA,
        github: GitHub,
        repositoryCache: RepositoryCache
    ): CommandManager {
        val commandManager = CommandManager()
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
        tags.addAll(config.markdownSections.map { it.toTag(github) })


        jda.awaitReady()
        config.trustedServerIds.mapNotNull { jda.getGuildById(it) }
            .forEach { guild ->
                commandManager.addGuildCommands(
                    guild,
                    PluginCommand(config.plugins),
                    IssueCommand(github, repositoryCache, config.defaultRepoName),
                    TagCommand(tags)
                )
                if (privateInvites.isNotEmpty()) {
                    commandManager.addGuildCommands(
                        guild,
                        InviteCommand("discord", privateInvites, null, false)
                    )
                }
                if (repositories.isNotEmpty() || repositoryCache.repositories.isNotEmpty()) {
                    val repos = repositories + repositoryCache.repositories.map { Repository.of(it) }

                    commandManager.addGuildCommands(
                        guild,
                        RepoCommand(repos, repositories[0])
                    )
                }
            }
        return commandManager
    }
}
