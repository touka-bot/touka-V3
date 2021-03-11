package cofig;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.*;

public class Config {

    public static final String PREFIX = "<";
    public static final Color DEFAULT_COLOR = new Color(255, 255, 255);
    public static final long THIS_ID = 819237154977873930L;
    public static final String VERSION = "3.0.0";
    private static ShardManager sm;

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
}
