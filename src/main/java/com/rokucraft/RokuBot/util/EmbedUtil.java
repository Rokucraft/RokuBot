package com.rokucraft.RokuBot.util;

import com.rokucraft.RokuBot.Main;
import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedUtil {
    public static EmbedBuilder createInfoEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(0x0FFFFF);
        builder.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png");

        return builder;
    }

    public static EmbedBuilder createIssuesEmbed(String issueList) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Open Issues", Main.defaultRepo.getHtmlUrl() + "/issues");
        builder.setDescription(issueList);
        builder.setThumbnail("https://cdn.discordapp.com/attachments/786216721065050112/787721554992824360/issue-opened72px.png");
        builder.setColor(0x56d364);
        builder.setFooter("");
        return builder;
    }

    public static EmbedBuilder createErrorEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(0xFF0000);

        return builder;
    }
}
