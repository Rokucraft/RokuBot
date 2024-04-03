package com.rokucraft.rokubot.entities

import org.kohsuke.github.GHRepository
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
data class Repository(
    @Required val name: String,
    @Required val repositoryUrl: String
) {
    companion object {
        fun of(repo: GHRepository): Repository {
            return Repository(repo.name, repo.htmlUrl.toString())
        }
    }
}
