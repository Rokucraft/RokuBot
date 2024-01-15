package com.rokucraft.rokubot.di

import com.rokucraft.rokubot.config.Config
import com.rokucraft.rokubot.config.loadConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ConfigModule {

    @Provides
    @Singleton
    fun provideConfig(): Config = loadConfig()
}
