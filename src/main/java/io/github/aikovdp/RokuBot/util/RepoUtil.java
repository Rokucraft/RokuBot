package io.github.aikovdp.RokuBot.util;

import io.github.aikovdp.RokuBot.Main;
import org.kohsuke.github.GHRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class RepoUtil {
    public String infoTitle;
    public String infoURL;
    public String infoContent;

    public RepoUtil(String repoName, String filePath, String title) throws IOException {

        GHRepository rokuRepo = Main.github.getRepository(repoName);
        InputStream referenceContents = rokuRepo.getFileContent(filePath).read();
        String fullText = new BufferedReader(
                new InputStreamReader(referenceContents, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        String halfText = fullText.substring(fullText.indexOf(title));
        String finalText;
        if (halfText.contains("##")) {
            finalText = halfText.substring(halfText.indexOf(title), halfText.indexOf("##"));
        } else {
            finalText = halfText;
        }
        String[] cutText = finalText.split("\\r?\\n", 2);

        infoContent = cutText[1].replace("*", "•");

        if (cutText[0].contains(": ")) {
            String[] titleArray = cutText[0].split(": ");
            infoTitle = titleArray[0];
            infoURL = titleArray[1];
        } else infoTitle = cutText[0];
    }
}
