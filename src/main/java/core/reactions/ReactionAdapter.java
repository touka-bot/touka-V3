package core.reactions;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

/**
 * The adapter for the ReactionListener
 */
public abstract class ReactionAdapter implements ReactionListener {

    private long message;

    /**
     * This method has to be called with the message ID
     * @param message The message ID
     */
    protected void create(long message) {
        this.message = message;
        ReactionEventManager.addMessage(message, this);
    }

    /**
     * This method can be called to remove the Listener class from the Handlerr
     */
    protected void dispose() {
        ReactionEventManager.removeMessage(message);
    }

    /**
     * This method gets called from the Handler after a reaction is added to this message
     * @param event The event
     */
    @Override
    public void onReactionAdded(MessageReactionAddEvent event) {
    }

    /**
     * This method gets called from the Handler after a reaction is removed to this message
     * @param event The event
     */
    @Override
    public void onReactionRemoved(MessageReactionRemoveEvent event) {
    }

    public long getMessage() {
        return message;
    }
}
