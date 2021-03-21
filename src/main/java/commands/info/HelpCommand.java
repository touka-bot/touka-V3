package commands.info;

import core.command.Command;
import core.command.CommandHandler;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
        setDescription("Shows this message");
        setExampleUsage("help invite");
        setArguments("(command name)");
    }

    @Override
    public void called(String args) {
        if (args.length() == 0) {
            reply(CommandHandler.getHelpList());
        } else {
            MessageEmbed help = CommandHandler.getCommandHelp(args);
            if (help != null) {
                reply(help);
            }
        }
    }
}