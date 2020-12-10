package io.github.aikovdp.RokuBot;

import io.github.aikovdp.RokuBot.util.PluginUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.FileNotFoundException;

import static io.github.aikovdp.RokuBot.util.EmbedUtil.createInfoEmbed;
import static io.github.aikovdp.RokuBot.util.EmbedUtil.createPluginEmbed;
import static io.github.aikovdp.RokuBot.util.PluginUtil.getPlugin;

public class Commands extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        // The XY Problem
        if(content.startsWith(Main.prefix + "xy")) {
            EmbedBuilder info = createInfoEmbed();
            info.setTitle("The XY Problem", "https://xyproblem.info/");
            info.setDescription("> The XY problem is asking about your attempted *solution* rather than your actual *problem*. This leads to enormous amounts of wasted time and energy, both on the part of people asking for help, and on the part of those providing help.\n" +
                    "> \n" +
                    "> 1. Always include information about a broader picture along with any attempted solution.\n" +
                    "> 2. If someone asks for more information, do provide details.\n" +
                    "> 3. If there are other solutions you've already ruled out, share why you've ruled them out. This gives more information about your requirements.");

            channel.sendMessage(info.build()).queue();
            info.clear();
        }

        // Semantic Versioning
        if(content.startsWith("!semver")) {
            EmbedBuilder info = createInfoEmbed();
            info.setTitle("Semantic Versioning", "https://semver.org/");
            info.setDescription("**Semantic Versioning** is one of the best known version schemes, and is used by most plugins. It is recommended to use this scheme whenever necessary.\n" +
                    "\n" +
                    "> Given a version number MAJOR.MINOR.PATCH, increment the:\n" +
                    "> 1. MAJOR version when you make incompatible API changes,\n" +
                    "> 2. MINOR version when you add functionality in a backwards compatible manner, and\n" +
                    "> 3. PATCH version when you make backwards compatible bug fixes.\n" +
                    "> \n" +
                    "> Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.");

            channel.sendMessage(info.build()).queue();
            info.clear();
        }
    }
}
