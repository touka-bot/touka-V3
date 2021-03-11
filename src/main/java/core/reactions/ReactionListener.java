package core.reactions;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public interface ReactionListener {
    void onReactionAdded(MessageReactionAddEvent event);
    void onReactionRemoved(MessageReactionRemoveEvent event);
}
