package core.sections;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class ChannelMessageEventManager {
    private static final HashMap<Long, List<ChannelListener>> listeners = new HashMap<>();
    private static final List<ChannelListener> removeBuffer = new ArrayList<>();
    private static final Set<Long> removedChannels = new HashSet<>();

    public static void addListener(ChannelListener listener, long channel) {
        if (!listeners.containsKey(channel)) {
            listeners.put(channel, new ArrayList<>());
        }
        listeners.get(channel).add(listener);
    }

    public static void removeListener(ChannelListener listener) {
        if (listeners.containsKey(listener.getChannelID())) {
            removeBuffer.add(listener);
        }
    }

    public static void onMessageReceived(MessageReceivedEvent event) {
        long id = event.getTextChannel().getIdLong();
        //if the message is relevant
        if (listeners.containsKey(id)) {
            //notify all listeners
            List<ChannelListener> list = listeners.get(id);
            for (ChannelListener channelListener : list) {
                if (channelListener.getUserID() == 0) {
                    channelListener.messageReceived(event);
                } else if (channelListener.getUserID() == event.getAuthor().getIdLong()) {
                    channelListener.messageReceived(event);
                }
            }

            //remove the listeners that got removed during the calling
            for (ChannelListener channelListener : removeBuffer) {
                listeners.get(channelListener.getChannelID()).remove(channelListener);
                removedChannels.add(channelListener.getChannelID());
            }

            //remove the channels if all listeners for that channel have been removed
            for (Long removedChannel : removedChannels) {
                list = listeners.get(removedChannel);
                if (list.isEmpty()) {
                    listeners.remove(removedChannel);
                }
            }

            //clear the buffers
            removedChannels.clear();
            removeBuffer.clear();
        }
    }
}
