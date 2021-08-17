package cofig;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.discordbots.api.client.DiscordBotListAPI;

import java.awt.*;
import java.io.*;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Config {

    public static final String PREFIX = ">";
    public static final Color DEFAULT_COLOR = new Color(40, 38, 38);
    public static final String VERSION = "3.1.3";

    /*  Discord Bot  */
    public static final String BOT_TOKEN = "NzgzNzIwNzI1ODQ4MTI5NTY2.X8e2zQ.nbq7lDRxEK9eNebvwY6yfV6qLGk";

    /*  Error Discord Bot */
    
    // Bot sends exceptions for debugging purposes into the specified discord channel
    public static final String ERROR_BOT_TOKEN = "ODQyMDk4NjMyMDQzMzk3MTgx.YJwXdw.zDah5AEkntFKcOYhJ9CED3aRadY";
    public static final long ERROR_GUILD_ID = 783764380398518292L;
    public static final long ERROR_CHANNEL_ID = 842100828952199228L;

    /*  Discord Bot List  */
    public static final String DBL_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijc4MzcyMDcyNTg0ODEyOTU2NiIsImJvdCI6dHJ1ZSwiaWF0IjoxNjEwOTU4NjM0fQ.tvBj4mWyIKOpYimt6hCvShwlUm7vX63Zz0evPszNFY8";
    public static final String DBL_ID = "783720725848129566";

    /*  Bot Owner */
    public static final List<Long> BOT_OWNER = List.of(265849018662387712L, 414755070161453076L);

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
                .botId(Config.DBL_ID)
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
        String base;
        if (e instanceof SocketTimeoutException) {
            base = e + "\n" + Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n"))
                    + "\n" + error;
        } else {
            base = "SocketTimeoutException: " + e.getMessage() + "\n" + error;
        }


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
            errorBot.getGuildById(ERROR_GUILD_ID).getTextChannelById(ERROR_CHANNEL_ID).sendMessage(s)
                    .queue(m -> System.out.println("sent error to channel"));
        }
    }
}