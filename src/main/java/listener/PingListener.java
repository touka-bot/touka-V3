package listener;

import cofig.Config;
import core.command.CommandHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PingListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getMentionedMembers().stream().anyMatch(m -> m.getIdLong() == Config.thisId)) {
            event.getTextChannel().sendMessage(CommandHandler.getHelp()).queue();
        }
    }
}
