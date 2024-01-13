package com.rokucraft.rokubot.di

import com.rokucraft.rokubot.config.Config
import com.rokucraft.rokubot.config.RecordConfigurationLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ConfigModule {

    @Provides
    @Singleton
    fun provideConfig(): Config = RecordConfigurationLoader().load(Config::class.java)
}
