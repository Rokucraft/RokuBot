package io.github.aikovdp.RokuBot;

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.requests.RestAction;

public class Plugin {
    public String name;
    public String[] aliases;
    public String description;
    public String[] depencencies;
    public String resourceURL;
    public String downloadURL;
    public String docsURL;
    public String discordInviteCode;
    public String currentVersion;
    public String latestVersion;

    public static String getInvite(String inviteCode) {
        RestAction<Invite> inviteRestAction = Invite.resolve(Main.api, inviteCode, false);
        Invite invite = inviteRestAction.complete();
        return invite.getUrl();
    }
}
