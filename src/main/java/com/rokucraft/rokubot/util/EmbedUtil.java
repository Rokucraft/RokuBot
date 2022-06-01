package com.rokucraft.rokubot.util;

import com.rokucraft.rokubot.RokuBot;
import net.dv8tion.jda.api.EmbedBuilder;

import static com.rokucraft.rokubot.Constants.*;

public class EmbedUtil {
    public static EmbedBuilder createInfoEmbed() {
        return new EmbedBuilder()
                .setColor(BLUE)
                .setThumbnail("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/2139.png");
    }

    public static EmbedBuilder createIssuesEmbed(String issueList) {
        return new EmbedBuilder()
                .setTitle("Open Issues", RokuBot.getDefaultRepo().getHtmlUrl() + "/issues")
                .setDescription(issueList)
                .setThumbnail("https://cdn.discordapp.com/attachments/786216721065050112/787721554992824360/issue-opened72px.png")
                .setColor(ISSUE_OPEN_COLOR)
                .setFooter("");
    }

    public static EmbedBuilder createErrorEmbed() {
        return new EmbedBuilder()
                .setColor(RED);
    }
}
