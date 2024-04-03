package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.AbstractCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import javax.inject.Inject

class EightBallCommand @Inject constructor() : AbstractCommand() {
    override val data: CommandData = Commands.slash("8ball", "Ask the Magic 8 Ball a question")

    private val answers = listOf(
        "It is certain",
        "It is decidedly so",
        "Without a doubt",
        "Yes definitely",
        "You may rely on it",
        "As I see it, yes",
        "Most likely",
        "Outlook good",
        "Yes",
        "Signs point to yes",
        "Reply hazy, try again",
        "Ask again later",
        "Better not tell you now",
        "Cannot predict now",
        "Concentrate and ask again",
        "Don't count on it",
        "My reply is no",
        "My sources say no",
        "Outlook not so good",
        "Very doubtful"
    )

    override fun execute(event: SlashCommandInteractionEvent) {
        event.reply(answers.random()).queue()
    }
}
