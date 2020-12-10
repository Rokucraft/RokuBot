package io.github.aikovdp.RokuBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();

        // The XY Problem
        if(content.startsWith(Main.prefix + "xy")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("The XY Problem", "https://xyproblem.info/");
            info.setDescription("> The XY problem is asking about your attempted *solution* rather than your actual *problem*. This leads to enormous amounts of wasted time and energy, both on the part of people asking for help, and on the part of those providing help.\n" +
                    "> \n" +
                    "> 1. Always include information about a broader picture along with any attempted solution.\n" +
                    "> 2. If someone asks for more information, do provide details.\n" +
                    "> 3. If there are other solutions you've already ruled out, share why you've ruled them out. This gives more information about your requirements.");
            info.setColor(0x0FFFFF);
            // info.addField("", "1. Always include information about a broader picture along with any attempted solution.", true);
            // info.addField("", "2. If someone asks for more information, do provide details", true);
            // info.addField("", "3. If there are other solutions you've already ruled out, share why you've ruled them out. This gives more information about your requirements.", true);
            // info.setFooter("Click the title for more information");
            info.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png");

            MessageChannel channel = event.getChannel();
            channel.sendMessage(info.build()).queue();
            info.clear();
        }

        // Semantic Versioning
        if(content.startsWith("!semver")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Semantic Versioning", "https://semver.org/");
            info.setDescription("**Semantic Versioning** is one of the best known version schemes, and is used by most plugins. It is recommended to use this scheme whenever necessary.\n" +
                    "\n" +
                    "> Given a version number MAJOR.MINOR.PATCH, increment the:\n" +
                    "> 1. MAJOR version when you make incompatible API changes,\n" +
                    "> 2. MINOR version when you add functionality in a backwards compatible manner, and\n" +
                    "> 3. PATCH version when you make backwards compatible bug fixes.\n" +
                    "> \n" +
                    "> Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.");
            info.setColor(0x00FFFF);
            // info.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png");
            info.setDescription("The current **WorldGuard** version is **7.0.4**");
            info.setTitle("WorldGuard Version");
            // info.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/1f98e.png");
            // info.setFooter("Click the title for more information");

            MessageChannel channel = event.getChannel();
            channel.sendMessage(info.build()).queue();
            info.clear();
        }

        if(content.startsWith("!plugins")) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            if (args[1].equalsIgnoreCase("update")) {
                Plugins plugins = new Plugins();
                String updateLink = plugins.getPluginData(args[2], "updateLink");
                event.getChannel().sendMessage(updateLink).queue();
            }
        }
    }
}
