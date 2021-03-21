package core.command;

import core.util.MultiPageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

/**
 * An abstract class for classes that interact with the chat.
 * Removes all the boilerplate code associated with sending messages over JDA
 */
public abstract class MessageSender {

    protected MessageReceivedEvent event;

    /**
     * Called by the specific handler.
     *
     * @param event The MessageReceivedEvent
     * @param args  The arguments
     */
    public void onMessageReceived(MessageReceivedEvent event, String args) {
        this.event = event;
        called(args);
    }


    /**
     * This method gets called by this object after the MessageReceivedEvent has been saved.
     *
     * @param args The arguments/text of the sent message
     */
    public abstract void called(String args);

    /**
     * Send a simple String message to the chat the original message came from.
     * Equivalent to {@code event.getTextChannel().sendMessage(message).queue();}
     * Will not send empty Strings.
     *
     * @param message The message
     */
    protected void reply(String message) {
        if (!message.isEmpty()) {
            event.getTextChannel().sendMessage(message).queue();
        }
    }

    /**
     * Send a simple embed message to the chat original message came from.
     * Equivalent to {@code event.getTextChannel().sendMessage(embed).queue();}
     * Will not send empty Embeds
     *
     * @param embed The embed
     */
    protected void reply(MessageEmbed embed) {
        if (!embed.isEmpty()) {
            event.getTextChannel().sendMessage(embed).queue();
        }
    }

    /**
     * Send multiple embeds in one message message as a {@code MultiPageEmbed} to the chat original message came from.
     * Equivalent to {@code new MultiPageEmbed(event, embeds);}
     * Will not send empty Embeds
     *
     * @param embeds The embeds
     */
    protected void reply(MessageEmbed... embeds) {
        if (embeds[0].isEmpty()) {
            return;
        }

        if (embeds.length == 1) {
            reply(embeds[0]);
        } else {
            new MultiPageEmbed(event, embeds);
        }

    }

    /**
     * Send multiple embeds in one message message as a {@code MultiPageEmbed} to the chat original message came from.
     * Equivalent to {@code new MultiPageEmbed(event, embeds);}
     * Will not send empty Embeds
     *
     * @param timeout The timeout in milliseconds
     * @param embeds  The embeds
     */
    protected void reply(long timeout, MessageEmbed... embeds) {
        if (!embeds[0].isEmpty()) {
            new MultiPageEmbed(event, timeout, embeds);
        }
    }


    /**
     * Send multiple embeds in one message message as a {@code MultiPageEmbed} to the chat original message came from.
     * Equivalent to {@code new MultiPageEmbed(event, embeds);}
     * Will not send empty Embeds
     *
     * @param embeds The embeds
     * @param emote1 The emote for scrolling to the left
     * @param emote2 The emote for scrolling to the right
     */
    protected void reply(String emote1, String emote2, MessageEmbed... embeds) {
        if (!embeds[0].isEmpty()) {
            new MultiPageEmbed(event, emote1, emote2, embeds);
        }
    }

    /**
     * Delete the original message after a specific delay in s.
     *
     * @param delay The delay in s
     */
    protected void deleteMsg(long delay) {
        event.getMessage().delete().queueAfter(delay, TimeUnit.SECONDS);
    }

    /**
     * Delete the original message after a specific delay in ms.
     */
    protected void deleteMsg() {
        event.getMessage().delete().queue();
    }
}
