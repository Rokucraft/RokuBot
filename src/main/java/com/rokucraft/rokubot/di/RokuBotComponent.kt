package com.rokucraft.rokubot.di

import com.rokucraft.rokubot.RokuBot
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [JdaModule::class, ConfigModule::class, GitHubModule::class]
)
interface RokuBotComponent {
    fun bot(): RokuBot
}
