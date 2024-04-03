package com.rokucraft.rokubot.entities

import com.rokucraft.rokubot.util.ColorConstants.BLUE
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import org.kohsuke.github.GitHub
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Required
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

@ConfigSerializable
data class MarkdownSection(
    @Required val name: String,
    @Required val title: String,
    @Required val repoName: String,
    @Required val filePath: String,
    val thumbnailUrl: String?,
    val url: String?,
    val description: String?
) {
    @Throws(IOException::class)
    fun toTag(gitHub: GitHub): Tag {
        return Tag(
            this.name,
            this.description,
            MessageCreateBuilder().addEmbeds(toEmbed(gitHub)).build()
        )
    }

    @Throws(IOException::class)
    fun toEmbed(gitHub: GitHub): MessageEmbed {
        return EmbedBuilder()
            .setColor(BLUE)
            .setTitle(title.replace("#+ ".toRegex(), ""), this.url)
            .setThumbnail(this.thumbnailUrl ?: INFO_ICON)
            .setDescription(getContents(gitHub))
            .build()
    }

    @Throws(IOException::class)
    fun getContents(gitHub: GitHub): String {
        val repository = gitHub.getRepository(this.repoName)
        BufferedReader(
            InputStreamReader(repository.getFileContent(this.filePath).read(), StandardCharsets.UTF_8)
        ).use { reader ->
            return reader.lines()
                .dropWhile { line: String -> !line.contains(this.title) } // Remove everything before title
                .skip(1) // Remove title
                .takeWhile { line: String -> !line.matches("#+ .*".toRegex()) } // Take until next title
                .collect(Collectors.joining("\n"))
        }
    }

    companion object {
        const val INFO_ICON: String = "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png"
    }
}
