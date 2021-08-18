package commands.info;

import cofig.Config;
import core.command.Command;

public class RebootCommand extends Command {
    public RebootCommand() {
        super("reboot");
        setHidden();
    }

    @Override
    public void called(String args) {
        long id = event.getMember().getUser().getIdLong();
        if (Config.BOT_OWNER.contains(id)) {
            event.getTextChannel().sendMessage("Rebooting!").queue();
            System.exit(409);
        } else {
            event.getTextChannel().sendMessage("Sorry, you don't seem to have sufficient Permissions for this!").queue();
        }
    }
}
