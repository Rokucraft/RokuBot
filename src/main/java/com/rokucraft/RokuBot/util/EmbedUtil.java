package com.rokucraft.RokuBot.util;

import com.rokucraft.RokuBot.Main;
import net.dv8tion.jda.api.EmbedBuilder;

import static com.rokucraft.RokuBot.Constants.*;

public class EmbedUtil {
    public static EmbedBuilder createInfoEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(BLUE);
        builder.setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png");

        return builder;
    }

    public static EmbedBuilder createIssuesEmbed(String issueList) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Open Issues", Main.defaultRepo.getHtmlUrl() + "/issues");
        builder.setDescription(issueList);
        builder.setThumbnail("https://cdn.discordapp.com/attachments/786216721065050112/787721554992824360/issue-opened72px.png");
        builder.setColor(ISSUE_OPEN_COLOR);
        builder.setFooter("");
        return builder;
    }

    public static EmbedBuilder createErrorEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(RED);

        return builder;
    }
}
