package com.rokucraft.rokubot.command.legacy;

import com.rokucraft.rokubot.RokuBot;
import com.rokucraft.rokubot.entities.Repository;
import com.rokucraft.rokubot.util.StaffOnly;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static com.rokucraft.rokubot.util.EmbedUtil.createErrorEmbed;

public class GHCommands extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (!event.isFromGuild() || event.getAuthor().isBot()) return;
        String prefix = RokuBot.getConfig().getPrefix();

        Message message = event.getMessage();
        if (!StaffOnly.check(message)) return;
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        if (content.startsWith(prefix + "repo") || content.startsWith(prefix + "source")) {
            String[] args = content.split("\\s+");
            if (args.length == 1) {
                channel.sendMessage(Repository.getDefault().getRepositoryUrl()).queue();
            } else {
                Repository repository = Repository.find(args[1]);
                if (repository != null) {
                    channel.sendMessage(repository.getRepositoryUrl()).queue();
                } else {
                    MessageEmbed errorEmbed = createErrorEmbed()
                            .setTitle("Repository `" + args[1] + "` not found!")
                            .setDescription("Usage: `" + prefix + "repository [name]`")
                            .build();
                    channel.sendMessageEmbeds(errorEmbed).queue();
                }
            }
        }
    }
}
