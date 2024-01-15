package com.rokucraft.rokubot.di

import com.rokucraft.rokubot.config.Config
import dagger.Module
import dagger.Provides
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder
import org.kohsuke.github.authorization.AppInstallationAuthorizationProvider
import org.kohsuke.github.authorization.AuthorizationProvider
import org.kohsuke.github.extras.authorization.JWTTokenProvider
import java.nio.file.Path
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.io.path.readText

@Module
object GitHubModule {
    @Provides
    @Singleton
    fun provideGitHub(provider: AuthorizationProvider): GitHub =
        GitHubBuilder().withAuthorizationProvider(provider).build()

    @Provides
    @GitHubPrivateKey
    fun providesGitHubKey(): String =
        System.getenv("GITHUB_PRIVATE_KEY") ?: Path.of("github-app.private-key.pem").readText()

    @Provides
    fun providesAuthorizationProvider(
        config: Config,
        @GitHubPrivateKey gitHubKey: String
    ): AuthorizationProvider =
        AppInstallationAuthorizationProvider(
            { it.getInstallationByOrganization(config.githubOrganization) },
            JWTTokenProvider(config.githubAppId, gitHubKey)
        )

}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
private annotation class GitHubPrivateKey
