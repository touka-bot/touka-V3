package commands.info;

import core.command.Command;
import core.command.CommandHandler;
import net.dv8tion.jda.api.entities.MessageEmbed;

//todo the help command is generated automatically. If you want to change the look of it go to the CommandHandler
public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Shows this message", "help invite", "(command name)");
    }

    @Override
    public void called(String args) {
        if(args.length() == 0) {
            if (CommandHandler.commandAmount() > CommandHandler.MAX_HELP_PAGE_LENGTH) {
                reply(CommandHandler.getHelpLists());
            } else {
                reply(CommandHandler.getHelpList().build());
            }
        } else {
            MessageEmbed help = CommandHandler.getCommandHelp(args.split(" ")[0]);
            if (help != null) {
                reply(help);
            }
        }
    }
}