package com.rokucraft.rokubot.commands;

import com.rokucraft.rokubot.Main;
import com.rokucraft.rokubot.entities.Rule;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class RuleCommand extends Command {

    public RuleCommand() {
        List<net.dv8tion.jda.api.interactions.commands.Command.Choice> ruleChoices = new ArrayList<>();
        List<Rule> rulesList = Main.getConfig().rulesList;
        for (Rule rule : rulesList) {
            int index = ruleChoices.size() + 1;
            ruleChoices.add(new net.dv8tion.jda.api.interactions.commands.Command.Choice(index + ". " + rule.getName(), index));
        }

        this.data = new CommandData("rule", "Shows the requested rule")
                .addOptions(
                        new OptionData(OptionType.INTEGER, "number", "The rule to show", true)
                                .addChoices(ruleChoices)
                );
    }

    @Override
    public void execute(SlashCommandEvent event) {
        int index = Math.toIntExact(event.getOption("number").getAsLong());
        Rule rule = Main.getConfig().rulesList.get(index - 1);
        event.replyEmbeds(rule.toEmbed(index)).queue();
    }
}
