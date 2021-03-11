package core.reactions;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactionEventListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if(event.getUser() == null){
            System.err.println("[ReactionEventListener] ADD Reaction User is null. Message: " + event.getMessageId() + " emote:" + event.getReactionEmote());
        } else {
            if (!event.getUser().isBot()) {
                ReactionEventManager.onReactionAdd(event);
            }
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        if(event.getUser() == null){
            System.err.println("[ReactionEventListener] REMOVE Reaction User is null. Message: " + event.getMessageId() + " emote:" + event.getReactionEmote());
        } else {
            if (!event.getUser().isBot()) {
                ReactionEventManager.onReactionRemove(event);
            }
        }
    }
}
