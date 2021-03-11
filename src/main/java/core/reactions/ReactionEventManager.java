package core.reactions;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.util.HashMap;

public class ReactionEventManager {
    private static final HashMap<Long, ReactionListener> currentReactions = new HashMap<>();

    public static void addMessage(long message, ReactionListener listener){
        currentReactions.put(message, listener);
    }

    public static void removeMessage(Long message){
        currentReactions.remove(message);
    }

    public static void onReactionAdd(MessageReactionAddEvent event){
        long message = event.getMessageIdLong();
        ReactionListener listener = currentReactions.get(message);
        if (listener != null) {
            listener.onReactionAdded(event);
        }
    }

    public static void onReactionRemove(MessageReactionRemoveEvent event){
        long message = event.getMessageIdLong();
        ReactionListener listener = currentReactions.get(message);
        if (listener != null) {
            listener.onReactionRemoved(event);
        }
    }
}