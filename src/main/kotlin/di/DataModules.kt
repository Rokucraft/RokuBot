package com.rokucraft.rokubot.di

import com.rokucraft.rokubot.data.ConfigPluginRepository
import com.rokucraft.rokubot.data.PluginRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModules {
    @Binds
    fun bindPluginRepository(repo: ConfigPluginRepository): PluginRepository
}
