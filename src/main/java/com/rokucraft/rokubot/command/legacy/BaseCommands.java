package com.rokucraft.rokubot.command.legacy;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.util.StaffOnly;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static com.rokucraft.rokubot.ColorConstants.BLUE;

public class BaseCommands extends ListenerAdapter {

    final EventWaiter waiter;

    public BaseCommands(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild() ||event.getAuthor().isBot()) return;
        String prefix = RokuBot.getConfig().getPrefix();

        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        if (content.startsWith(prefix + "help") && StaffOnly.check(message))  {
            EmbedBuilder response = new EmbedBuilder()
                    .setColor(BLUE)
                    .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png")
                    .setTitle(event.getGuild().getSelfMember().getEffectiveName() + " Help")
                    .addField("Commands", ":books: `" + prefix + "repo` shows a link to a repository", false);

            channel.sendMessageEmbeds(response.build()).queue();
            response.clear();
            return;
        }

        if (content.toLowerCase().startsWith("all my homies ")) {
            channel.sendMessage("who").queue();
            waiter.waitForEvent(MessageReceivedEvent.class,
                    e -> e.getAuthor().equals(event.getAuthor())
                            && e.getChannel().equals(event.getChannel()),
                    e -> channel.sendMessage("asked").queue());
        }
    }
}
