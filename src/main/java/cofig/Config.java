package cofig;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.discordbots.api.client.DiscordBotListAPI;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Config {

    public static final String PREFIX = ">";
    public static final Color DEFAULT_COLOR = new Color(40, 38, 38);
    public static final String VERSION = "3.1.1";
    public static final String DBL_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijc4MzcyMDcyNTg0ODEyOTU2NiIsImJvdCI6dHJ1ZSwiaWF0IjoxNjEwOTU4NjM0fQ.tvBj4mWyIKOpYimt6hCvShwlUm7vX63Zz0evPszNFY8";
    public static long thisId = 0;
    private static ShardManager sm;

    private static final String GUILD_COUNT_FILE_NAME = "guild_count.txt";

    private static int guildCount;
    private static JDA errorBot;

    public static void setSm(ShardManager sm) {
        Config.sm = sm;
    }

    public static EmbedBuilder getDefaultEmbed() {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.DEFAULT_COLOR).
                setThumbnail(sm.getShards().get(0).getSelfUser().getAvatarUrl());
        return builder;
    }

    public static void init() {
        refreshGuildCount();
        thisId = sm.getShards().get(0).getSelfUser().getIdLong();
    }

    public static void refreshGuildCount() {
        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token(Config.DBL_TOKEN)
                .botId("783720725848129566")
                .build();

        guildCount = sm.getShards().stream()
                .map(JDA::getGuilds)
                .mapToInt(List::size)
                .sum();

        api.setStats(guildCount);

        writeGuildCountFile();
    }

    private static void writeGuildCountFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(GUILD_COUNT_FILE_NAME))) {
            bw.write(String.valueOf(guildCount));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getGuildCount() {
        return guildCount;
    }

    public static int readGuildCountFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(GUILD_COUNT_FILE_NAME))) {
            return Integer.parseInt(br.readLine().trim());
        }
    }

    public static void setErrorBot(JDA error) {
        errorBot = error;
    }

    public static void logError(Exception e, String error) {
        String base = e.toString() + "\n" + Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"))
                + "\n" + error;

        List<String> messages = new ArrayList<>();
        messages.add(base);

        String msg;
        int last;
        while ((msg = messages.get(last = (messages.size() - 1))).length() >= 2000) {
            String firstPart = msg.substring(0, 2000);
            String other = msg.substring(2000);

            messages.set(last, firstPart);
            messages.add(other);
        }

        for (String s : messages) {
            errorBot.getGuildById(783764380398518292L).getTextChannelById(842100828952199228L).sendMessage(s)
                    .queue(m -> System.out.println("sent error to channel"));
        }
    }
}
