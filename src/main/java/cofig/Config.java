package cofig;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.*;
import java.util.List;

public class Config {

    public static final String PREFIX = ">";
    public static final Color DEFAULT_COLOR = new Color(40, 38, 38);
    public static final long THIS_ID = 819237154977873930L;
    public static final String VERSION = "3.0.0";
    public static final String DBL_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijc4MzcyMDcyNTg0ODEyOTU2NiIsImJvdCI6dHJ1ZSwiaWF0IjoxNjEwOTU4NjM0fQ.tvBj4mWyIKOpYimt6hCvShwlUm7vX63Zz0evPszNFY8";
    private static ShardManager sm;

    private static int serverCount;

    public static void setSm(ShardManager sm) {
        Config.sm = sm;
    }

    public static EmbedBuilder getDefaultEmbed() {
        User thisBot = sm.getUserById(THIS_ID);
        if (thisBot == null) {
            thisBot = sm.retrieveUserById(THIS_ID).complete();
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.DEFAULT_COLOR).
                setThumbnail(thisBot.getAvatarUrl());
        return builder;
    }

    public static void refreshServerCount() {
         serverCount = sm.getShards().stream()
                .map(JDA::getGuilds)
                .mapToInt(List::size)
                .sum();
    }

    public static int getServerCount() {
        return serverCount;
    }
}
