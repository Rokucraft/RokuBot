package io.github.aikovdp.RokuBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {

    public static String prefix = "!";

    public static void main(String[] arguments) throws Exception {
        JDA api = JDABuilder.createDefault("Nzg2MTg4NDQzODcyNTI2Mzc3.X9CxCw.eDOW67G5H9mAtlPfUVOTmkMQ7OA")
                .addEventListeners(new Commands())
                .build();
        System.out.println(Plugins.getPluginData("AdvancedBan", "updateLink"));
    }
}
