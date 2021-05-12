package listener;

import cofig.Config;
import core.command.CommandHandler;
import data.Storage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PingListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        try {
            Storage.loadAds();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (event.getMessage().getMentionedMembers().stream().anyMatch(m -> m.getIdLong() == Config.thisId)) {
            event.getTextChannel().sendMessage(CommandHandler.getHelp()).queue();
        }
    }
}
