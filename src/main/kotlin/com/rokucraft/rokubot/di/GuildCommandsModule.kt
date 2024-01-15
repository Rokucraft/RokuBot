package com.rokucraft.rokubot.di

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.command.commands.*
import com.rokucraft.rokubot.config.Config
import com.rokucraft.rokubot.entities.Repository
import com.rokucraft.rokubot.github.RepositoryCache
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import org.kohsuke.github.GitHub

@Module
object GuildCommandsModule {
    @Provides
    @IntoSet
    @GuildCommand
    fun providePluginCommand(config: Config): AbstractCommand =
        PluginCommand(config.plugins)

    @Provides
    @IntoSet
    @GuildCommand
    fun provideIssueCommand(
        config: Config,
        gitHub: GitHub,
        repositoryCache: RepositoryCache
    ): AbstractCommand =
        IssueCommand(gitHub, repositoryCache, config.defaultRepoName)

    @Provides
    @IntoSet
    @GuildCommand
    fun provideTagCommand(
        config: Config,
        gitHub: GitHub
    ): AbstractCommand = TagCommand(
        tags = config.privateTags + config.markdownSections.map { it.toTag(gitHub) }
    )

    @Provides
    @IntoSet
    @GuildCommand
    fun providePrivateInviteCommand(
        config: Config
    ): AbstractCommand = InviteCommand(
        name = "discord",
        invites = config.privateInvites,
        defaultInvite = null,
        defaultEnabled = false
    )

    @Provides
    @IntoSet
    @GuildCommand
    fun provideRepositoryCommand(
        config: Config,
        repositoryCache: RepositoryCache
    ): AbstractCommand {
        val repos = config.repositories + repositoryCache.repositories.map { Repository.of(it) }
        return RepoCommand(
            repos,
            repos.getOrNull(0)
        )
    }
}
