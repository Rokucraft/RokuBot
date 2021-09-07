package com.rokucraft.rokubot.util;

import com.rokucraft.rokubot.RokuBot;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHLabel;

import java.io.IOException;
import java.util.List;

public class IssueUtil {

    private static List<GHIssue> openIssues;

    public static String getIssueList() {
        String issueList = "";
        try {
            openIssues = RokuBot.defaultRepo.getIssues(GHIssueState.OPEN);
        } catch (IOException ignored) {
            // This error wil be thrown if there are no open issues, so the List can remain empty
        }
        for (GHIssue issue : openIssues) {
            issueList = issueList.concat("[`#" + issue.getNumber() + "`](" + issue.getHtmlUrl() + ") " + issue.getTitle() + "\n");
        }
        return issueList;
    }

    public static String getIssueList(GHLabel label) {
        String issueList = "";
        try {
            openIssues = RokuBot.defaultRepo.getIssues(GHIssueState.OPEN);
        } catch (IOException ignored) {
            // This error wil be thrown if there are no open issues, so the List can remain empty
        }
        for (GHIssue issue : openIssues) {
            if (issue.getLabels().contains(label)) {
                issueList = issueList.concat("[`#" + issue.getNumber() + "`](" + issue.getHtmlUrl() + ") " + issue.getTitle() + "\n");
            }
        }
        return issueList;
    }
}
