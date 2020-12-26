package io.github.aikovdp.RokuBot;

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.requests.RestAction;

public class Plugin {
    private String name;
    private String[] aliases;
    private String resourceURL;
    private String description;
    private String currentVersion;
    private String latestVersion;
    private String downloadURL;
    private String docsURL;
    private String discordInviteCode;
    private String[] dependencies;


    public String getInvite() {
        if (discordInviteCode != null) {
            RestAction<Invite> inviteRestAction = Invite.resolve(Main.api, discordInviteCode, false);
            Invite invite = inviteRestAction.complete();
            return invite.getUrl();
        } else return null;
    }

    public String getDiscordInviteCode() {
        return discordInviteCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getDependencies() {
        if (dependencies != null) {
            return String.join(", ", dependencies);
        } else return null;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public String getDocsURL() {
        return docsURL;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}
