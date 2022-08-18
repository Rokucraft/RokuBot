package com.rokucraft.rokubot.command.commands;

import com.rokucraft.rokubot.command.GlobalCommand;
import com.rokucraft.rokubot.command.SlashCommand;
import com.rokucraft.rokubot.entities.Rule;
import com.rokucraft.rokubot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

import static com.rokucraft.rokubot.Constants.FUCHSIA;

public class RuleCommand implements GlobalCommand, SlashCommand {
    private static final String SCALES_ICON =
            "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2696.png";
    private static final String SCROLL_ICON =
            "https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f4dc.png";
    private final List<Rule> rules;
    private final String rulesFooter;
    private final CommandData data;

    public RuleCommand(List<Rule> rules, String rulesFooter) {
        this.rules = rules;
        this.rulesFooter = rulesFooter;
        this.data = Commands.slash("rule", "Shows the requested rule")
                .addOptions(new OptionData(OptionType.INTEGER, "number", "The rule to show", true).addChoices(
                        this.rules.stream()
                                .map(rule -> {
                                    int number = rules.indexOf(rule) + 1;
                                    return new Choice(number + ". " + rule.name(), number);
                                }).toList()
                ));
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        try {
            Integer number = event.getOption("number", OptionMapping::getAsInt);
            if (number == null) {
                throw new IllegalArgumentException("You must provide a rule number.");
            }
            event.replyEmbeds(createRuleEmbed(number)).queue();
        } catch (IllegalArgumentException e) {
            event.replyEmbeds(EmbedUtil.createErrorEmbed(e.getMessage())).setEphemeral(true).queue();
        }
    }

    /**
     * Constructs a decorated {@link MessageEmbed} containing the rule
     *
     * @param number the rule number
     * @return a decorated {@link MessageEmbed} containing the rule
     * @throws IllegalArgumentException when a rule with the provided number does not exist
     */
    private MessageEmbed createRuleEmbed(int number) throws IllegalArgumentException {
        if (number < 1 || number > rules.size()) {
            throw new IllegalArgumentException("Invalid rule number: " + number);
        }
        Rule rule = rules.get(number - 1);
        return new EmbedBuilder()
                .setTitle(number + ". " + rule.name())
                .setDescription(rule.description())
                .setFooter(rulesFooter, SCALES_ICON)
                .setThumbnail(SCROLL_ICON)
                .setColor(FUCHSIA)
                .build();
    }

    @Override
    public @NonNull CommandData getData() {
        return data;
    }
}
