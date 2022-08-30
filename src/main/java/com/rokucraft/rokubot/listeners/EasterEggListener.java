package com.rokucraft.rokubot.listeners;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EasterEggListener extends ListenerAdapter {

    private final EventWaiter waiter;

    public EasterEggListener(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild() ||event.getAuthor().isBot()) return;

        MessageChannel originalChannel = event.getChannel();

        if (event.getMessage().getContentRaw().toLowerCase().startsWith("all my homies ")) {
            originalChannel.sendMessage("who").queue();
            waiter.waitForEvent(
                    MessageReceivedEvent.class,
                    e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(originalChannel),
                    e -> e.getChannel().sendMessage("asked").queue()
            );
        }
    }
}
