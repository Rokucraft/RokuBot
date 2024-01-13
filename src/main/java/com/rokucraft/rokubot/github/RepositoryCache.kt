package com.rokucraft.rokubot.github

import com.rokucraft.rokubot.config.Config
import org.kohsuke.github.GitHub
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryCache @Inject constructor(
    gitHub: GitHub,
    config: Config
) {
    val repositories = gitHub.getOrganization(config.githubOrganization)
        .listRepositories().toList()
        .filterNot { it.isArchived }
        .sortedByDescending { it.updatedAt }
}
