package com.rokucraft.rokubot.command.commands

import com.rokucraft.rokubot.command.AbstractCommand
import com.rokucraft.rokubot.entities.Rule
import com.rokucraft.rokubot.util.ColorConstants
import com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class RuleCommand(
    private val rules: List<Rule>,
    private val rulesFooter: String?
) : AbstractCommand() {
    override val data = Commands.slash("rule", "Shows the requested rule")
        .addOptions(
            OptionData(OptionType.INTEGER, "number", "The rule to show", true)
                .addChoices(this.rules.map {
                    val number = rules.indexOf(it) + 1
                    Command.Choice("$number. ${it.name}", number.toLong())
                })
        )

    override fun execute(event: SlashCommandInteractionEvent) {
        try {
            val number = event.getOption("number") { it.asInt }
                ?: throw IllegalArgumentException("You must provide a rule number.")
            event.replyEmbeds(createRuleEmbed(number)).queue()
        } catch (e: IllegalArgumentException) {
            event.replyEmbeds(createErrorEmbed(e.message ?: "Something went wrong"))
                .setEphemeral(true)
                .queue()
        }
    }

    /**
     * Constructs a decorated [MessageEmbed] containing the rule
     *
     * @param number the rule number
     * @return a decorated [MessageEmbed] containing the rule
     * @throws IllegalArgumentException when a rule with the provided number does not exist
     */
    private fun createRuleEmbed(number: Int): MessageEmbed {
        require((number >= 1 && number <= rules.size)) { "Invalid rule number: $number" }
        val rule = rules[number - 1]
        return EmbedBuilder()
            .setTitle("$number. ${rule.name}")
            .setDescription(rule.description)
            .setFooter(rulesFooter, SCALES_ICON)
            .setThumbnail(SCROLL_ICON)
            .setColor(ColorConstants.FUCHSIA)
            .build()
    }
}

private const val SCALES_ICON = "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2696.png"
private const val SCROLL_ICON = "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f4dc.png"
