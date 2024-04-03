package com.rokucraft.rokubot.data

import com.rokucraft.rokubot.config.Config
import com.rokucraft.rokubot.entities.Plugin
import javax.inject.Inject

interface PluginRepository {
    fun getAll(): List<Plugin>
    fun getByName(name: String): Plugin?
    fun getNamesContaining(query: String): List<String>
}

class ConfigPluginRepository @Inject constructor(
    private val config: Config
) : PluginRepository {
    override fun getAll(): List<Plugin> = config.plugins

    override fun getByName(name: String) = config.plugins.find { it.name.equals(name, ignoreCase = true) }

    override fun getNamesContaining(query: String): List<String> =
        config.plugins
            .map { it.name }
            .filter { it.contains(query, ignoreCase = true) }
}
