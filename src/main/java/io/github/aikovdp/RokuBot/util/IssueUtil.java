package io.github.aikovdp.RokuBot.util;

import io.github.aikovdp.RokuBot.Main;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHLabel;

public class IssueUtil {

    public static String getIssueList() {
        String issueList = "";
        for (GHIssue issue : Main.openIssues) {
            issueList = issueList.concat("[`#" + issue.getNumber() + "`](" + issue.getHtmlUrl() + ") " + issue.getTitle() + "\n");
        }
        return issueList;
    }

    public static String getIssueList(GHLabel label) {
        String issueList = "";
        for (GHIssue issue : Main.openIssues) {
            if (issue.getLabels().contains(label)) {
                issueList = issueList.concat("[`#" + issue.getNumber() + "`](" + issue.getHtmlUrl() + ") " + issue.getTitle() + "\n");
            }
        }
        return issueList;
    }
}
