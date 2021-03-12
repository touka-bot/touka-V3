package commands.info;

import core.command.Command;

public class RebootCommand extends Command {
    public RebootCommand() {
        super("reboot", true);
    }

    @Override
    public void called(String args) {
        long id = event.getMember().getUser().getIdLong();
        if (id == 265849018662387712L ||
                id == 414755070161453076L) {
            event.getTextChannel().sendMessage("Rebooting!").queue();
            System.exit(409);
        } else {
            event.getTextChannel().sendMessage("Sorry, you don't seem to have sufficient Permissions for this!").queue();
        }
    }
}
