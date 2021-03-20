package core;

import cofig.Config;
import commands.info.*;
import commands.search.SearchCommand;
import core.command.CommandListener;
import core.reactions.ReactionEventListener;
import core.sections.ChannelMessageListener;
import data.Storage;
import listener.GuildLeaveListener;
import listener.ServerJoinListener;
import listener.StartUpListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws LoginException, IOException {
        DefaultShardManagerBuilder builder =
                DefaultShardManagerBuilder.createDefault("NzgzNzIwNzI1ODQ4MTI5NTY2.X8e2zQ.nbq7lDRxEK9eNebvwY6yfV6qLGk");
        builder.setCompression(Compression.ZLIB);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setAutoReconnect(true);

        builder.setActivity(Activity.listening(Config.PREFIX + "help"));
        builder.setDisabledIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);
        builder.setMemberCachePolicy(MemberCachePolicy.NONE);
        builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ROLE_TAGS, CacheFlag.MEMBER_OVERRIDES, CacheFlag.EMOTE, CacheFlag.VOICE_STATE);
        builder.addEventListeners(
                new StartUpListener(),
                new ChannelMessageListener(),
                new CommandListener(),
                new ReactionEventListener(),
                new GuildLeaveListener(),
                new ServerJoinListener()
        );

        //get the shard amount from the server count
        //each shard gets 1000 servers
        int shardCount = Config.readGuildCountFile() / 1000 + 1;
        System.out.println("Starting Touka with " + shardCount + " shards.");
        builder.setShardsTotal(shardCount);

        ShardManager sm = builder.build();
        Config.setSm(sm);
        Config.refreshGuildCount();
        Storage.init();
        setupCommands();
    }

    private static void setupCommands() {
        new HelpCommand();
        new InviteCommand();
        new InfoCommand();
        new SearchCommand();
        new RebootCommand();
        new TopCommand();
    }
}
