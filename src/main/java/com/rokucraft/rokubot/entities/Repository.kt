package com.rokucraft.rokubot.entities

import org.kohsuke.github.GHRepository
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
@JvmRecord
data class Repository(
    @Required val name: String,
    @Required val repositoryUrl: String
) {
    companion object {
        @JvmStatic
        fun of(repo: GHRepository): Repository {
            return Repository(repo.name, repo.htmlUrl.toString())
        }
    }
}
