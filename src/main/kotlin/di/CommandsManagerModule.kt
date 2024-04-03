package com.rokucraft.rokubot.di

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.command.CommandManager
import com.rokucraft.rokubot.config.Config
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
object CommandsManagerModule {

    @Provides
    fun provideCommandManager(
        config: Config,
        @GlobalCommand globalCommands: Set<@JvmSuppressWildcards AbstractCommand>,
        @GuildCommand guildCommands: Set<@JvmSuppressWildcards AbstractCommand>
    ): CommandManager {
        val registrableGuildCommands = guildCommands.filter { it.shouldBeRegistered }.toSet()
        return CommandManager(
            globalCommands = globalCommands.filter { it.shouldBeRegistered }.toSet(),
            guildCommands = config.trustedServerIds.associate {
                (it to registrableGuildCommands)
            }
        )
    }
}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class GlobalCommand

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class GuildCommand
