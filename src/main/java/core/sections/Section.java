package core.sections;

import core.command.MessageSender;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The section class can be extended to create sections where the user is asked to write multiple messages
 */
public abstract class Section extends MessageSender implements ChannelListener {
    private final long textChannelID;
    private final long userID;

    /**
     * Create a new section for a specific user
     *
     * @param textChannelID The channel ID
     * @param userID        The user ID
     */
    public Section(long textChannelID, long userID) {
        this.textChannelID = textChannelID;
        this.userID = userID;

        ChannelMessageEventManager.addListener(this, textChannelID);
    }

    /**
     * Create a new section for all users in a channel
     *
     * @param textChannelID The channel ID
     */
    public Section(long textChannelID) {
        this.textChannelID = textChannelID;
        this.userID = 0;
    }

    @Override
    public void messageReceived(MessageReceivedEvent event) {
        this.event = event;
        called(event.getMessage().getContentRaw());
    }

    /**
     * End the section.
     */
    protected void dispose() {
        ChannelMessageEventManager.removeListener(this);
    }

    @Override
    public long getUserID() {
        return userID;
    }

    @Override
    public long getChannelID() {
        return textChannelID;
    }

    protected void setSectionTimeout(long timeout) {
        //SECTION TIMEOUT
        //is safe because listeners can be removed multiple times
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                dispose();
                t.cancel();
            }
        }, timeout);
    }
}
