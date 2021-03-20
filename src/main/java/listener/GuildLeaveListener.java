package listener;

import cofig.Config;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        Config.refreshServerCount();
    }
}
