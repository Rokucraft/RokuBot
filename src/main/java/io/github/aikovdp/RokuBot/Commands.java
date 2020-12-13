package io.github.aikovdp.RokuBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import static io.github.aikovdp.RokuBot.util.EmbedUtil.createGHInfoEmbed;

public class Commands extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        EmbedBuilder response = null;

        // GAME ENGINEER REFERENCE
        // Semantic Versioning
        if(content.startsWith("!semver")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Semantic Versioning");
        }

        // Keep a Changelog
        if(content.startsWith("!changelog")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Keep a Changelog");
        }

        // Material Names
        if(content.startsWith("!material")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Material Names");
        }

        // Binary Search
        if(content.startsWith("!binarysearch")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "Binary Search");
        }

        // The "It Works" Fallacy
        if(content.startsWith("!itworks")) {
            response = createGHInfoEmbed("game-engineer-reference.md", "The \"It Works\" Fallacy");
        }

        // ASKING QUESTIONS
        // The XY Problem
        if(content.startsWith(Main.prefix + "xy")) {
            response = createGHInfoEmbed("asking-questions.md", "The XY Problem");
        }

        // Don't ask to ask
        if(content.startsWith(Main.prefix + "ask")) {
            response = createGHInfoEmbed("asking-questions.md", "Don't ask to ask");
        }

        if (response != null) {
            channel.sendMessage(response.build()).queue();
            response.clear();
        }
    }
}
