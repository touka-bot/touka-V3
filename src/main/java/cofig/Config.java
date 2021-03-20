package cofig;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.*;
import java.io.*;
import java.util.List;

public class Config {

    public static final String PREFIX = ">";
    public static final Color DEFAULT_COLOR = new Color(40, 38, 38);
    public static final long THIS_ID = 783720725848129566L;
    public static final String VERSION = "3.0.0";
    public static final String DBL_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijc4MzcyMDcyNTg0ODEyOTU2NiIsImJvdCI6dHJ1ZSwiaWF0IjoxNjEwOTU4NjM0fQ.tvBj4mWyIKOpYimt6hCvShwlUm7vX63Zz0evPszNFY8";
    private static ShardManager sm;

    private static final String GUILD_COUNT_FILE_NAME = "guild_count.txt";

    private static int guildCount;

    public static void setSm(ShardManager sm) {
        Config.sm = sm;
    }

    public static EmbedBuilder getDefaultEmbed() {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.DEFAULT_COLOR).
                setThumbnail(sm.getShards().get(0).getSelfUser().getAvatarUrl());
        return builder;
    }

    public static void refreshGuildCount() {
         guildCount = sm.getShards().stream()
                .map(JDA::getGuilds)
                .mapToInt(List::size)
                .sum();

         writeGuildCountFile();
    }

    private static void writeGuildCountFile() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(GUILD_COUNT_FILE_NAME))) {
            bw.write(String.valueOf(guildCount));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getGuildCount() {
        return guildCount;
    }

    public static int readGuildCountFile() throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(GUILD_COUNT_FILE_NAME))) {
            return Integer.parseInt(br.readLine().trim());
        }
    }
}
