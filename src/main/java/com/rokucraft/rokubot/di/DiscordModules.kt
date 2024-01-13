package com.rokucraft.rokubot.di

import dagger.Module
import dagger.Provides
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import javax.inject.Singleton

@Module
object JdaModule {
    @Provides
    @Singleton
    fun provideJda(@BotToken botToken: String): JDA = JDABuilder.createLight(botToken)
        .enableIntents(GatewayIntent.GUILD_MEMBERS)
        .setRequestTimeoutRetry(false)
        .build()

    @Provides
    @BotToken
    fun provideBotToken(): String = System.getenv("DISCORD_TOKEN")
}
