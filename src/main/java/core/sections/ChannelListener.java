package core.sections;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ChannelListener {
    void messageReceived(MessageReceivedEvent event);
    long getUserID();
    long getChannelID();
}
