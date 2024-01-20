package com.rokucraft.rokubot.di

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.command.commands.*
import com.rokucraft.rokubot.config.Config
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet

@Module
object GlobalCommandsModule {

    @Provides
    @IntoSet
    @GlobalCommand
    fun provideRuleCommand(config: Config): AbstractCommand =
        RuleCommand(config.rules, config.rulesFooter)

    @Provides
    @IntoSet
    @GlobalCommand
    fun provideRollCommand(): AbstractCommand = RollCommand()

    @Provides
    @IntoSet
    @GlobalCommand
    fun provideUUIDCommand(): AbstractCommand = UUIDCommand()

    @Provides
    @IntoSet
    @GlobalCommand
    fun provideInviteCommand(config: Config): AbstractCommand {
        val invites = config.publicInvites
        return InviteCommand("invite", invites, invites.getOrNull(0), true)
    }

    @Provides
    @ElementsIntoSet
    @GlobalCommand
    fun provideRootTagCommands(config: Config): Set<AbstractCommand> =
        config.rootTagCommands.map { RootTagCommand(it) }.toSet()
}

@Module
interface GlobalCommandsBindsModule {
    @Binds
    @IntoSet
    @GlobalCommand
    fun bindEightBallCommand(command: EightBallCommand): AbstractCommand
}
