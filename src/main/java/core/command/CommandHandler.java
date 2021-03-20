package core.command;

import cofig.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The CommandHandler handles everything about the commands
 */
public class CommandHandler {

    public static final int MAX_HELP_PAGE_LENGTH = 10;

    private static final Map<String, Command> commands = new HashMap<>();
    private static final HashSet<Command> commandsForHelp = new HashSet<>();
    private static final Map<String, Command> hiddenCommands = new HashMap<>();

    private static final CommandParser parser = new CommandParser();

    /**
     * Add a new command to the handler. This is normally done by the{@code Command}.
     *
     * @param key    The key (command name)
     * @param cmd    The command object
     * @param hidden Whether the command should be shown on the help page
     */
    public static void addCommand(String key, Command cmd, boolean hidden) {
        if (hidden) {
            hiddenCommands.put(key, cmd);
        } else {
            commands.put(key, cmd);
            commandsForHelp.add(cmd);
        }
    }

    public static void addCommand(String key, Command cmd) {
        commands.put(key, cmd);
        commandsForHelp.add(cmd);
    }

    /**
     * This method is called by the{@code CommandListener}
     *
     * @param event The {@code MessageReceivedEvent}
     */
    static void call(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().toLowerCase().startsWith(Config.PREFIX)) {
            String[] split = parser.splitOffCommandName(event.getMessage().getContentRaw());
            String command = split[0];
            if (commands.containsKey(command)) {
                commands.get(command).onMessageReceived(event, split[1]);
            } else if (hiddenCommands.containsKey(command)) {
                hiddenCommands.get(command).onMessageReceived(event, split[1]);
            }
        }
    }

    public static int commandAmount() {
        return commandsForHelp.size();
    }

    /**
     * Get the list of all commands, on one page.
     * May lead to problems if there are too many commands.
     * @return The page
     */
    public static EmbedBuilder getHelpList() {
        EmbedBuilder builder = Config.getDefaultEmbed();
        builder.setTitle("Touka's Commands");
        for (Command s : commandsForHelp) {
            builder.addField(Config.PREFIX + s.getName(), "`" + s.getDescription() + "`", false);
        }
        return builder;
    }

    /**
     * Get a list of all commands, spread over different pages
     * @return The pages
     */
    public static MessageEmbed[] getHelpLists() {

        int length = commandsForHelp.size();
        int pages = length / MAX_HELP_PAGE_LENGTH + 1;
        EmbedBuilder[] builders = new EmbedBuilder[pages];


        int i = 0, j = 0;
        EmbedBuilder builder = null;
        for (Command value : commandsForHelp) {
            if (i % MAX_HELP_PAGE_LENGTH == 0) {
                builder = Config.getDefaultEmbed();
                builder.setTitle("Touka help");
                builders[j] = builder;
                j++;
            }
            builder.addField(value.getName(), value.getDescription(), false);

            i++;
        }

        MessageEmbed[] messageEmbeds = new MessageEmbed[pages];
        for (i = 0; i < builders.length; i++) {
            messageEmbeds[i] = builders[i].build();
        }
        return messageEmbeds;
    }

    /**
     * Returns the help page for a single command
     * @param command The command name
     * @return The help page
     */
    public static MessageEmbed getCommandHelp(String command) {
        Command cmd = commands.get(command);
        if (cmd != null) {
            EmbedBuilder builder = Config.getDefaultEmbed()
                    .setTitle("Touka help: " + cmd.getName())
                    .addField("Name", cmd.getName(), true)
                    .addField("Description:", cmd.getDetailDescription(), true)
                    .addField("Example usage", "`" + cmd.getExampleUsage() + "`", true);

            if (!cmd.getArguments().equals("")) {
                builder.addField("Arguments", "`" + cmd.getArguments() + "`", true);
            }
            return builder.build();
        } else {
            return null;
        }
    }
}
