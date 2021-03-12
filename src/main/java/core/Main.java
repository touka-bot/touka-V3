package core;

import cofig.Config;
import commands.info.HelpCommand;
import commands.info.InfoCommand;
import commands.info.InviteCommand;
import commands.info.RebootCommand;
import commands.search.SearchCommand;
import commands.search.SearchSection;
import core.command.CommandListener;
import core.reactions.ReactionEventListener;
import core.sections.ChannelMessageListener;
import listener.StartUpListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) throws LoginException {
        DefaultShardManagerBuilder builder =
                DefaultShardManagerBuilder.createDefault("ODE5MjM3MTU0OTc3ODczOTMw.YEjsDg.n20cgALF7DBbrp1-0RnA3Kg_8ok");
        builder.setCompression(Compression.ZLIB);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setAutoReconnect(true);

        builder.setActivity(Activity.listening(Config.PREFIX + "help"));
        builder.setDisabledIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);
        builder.setMemberCachePolicy(MemberCachePolicy.NONE);
        builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ROLE_TAGS, CacheFlag.MEMBER_OVERRIDES, CacheFlag.EMOTE, CacheFlag.VOICE_STATE);
        builder.setShardsTotal(1);
        builder.addEventListeners(
                new StartUpListener(),
                new ChannelMessageListener(),
                new CommandListener(),
                new ReactionEventListener()
        );

        ShardManager sm = builder.build();
        Config.setSm(sm);
        setupCommands();
    }

    private static void setupCommands() {
        new HelpCommand();
        new InviteCommand();
        new InfoCommand();
        new SearchCommand();
        new RebootCommand();
    }
}
