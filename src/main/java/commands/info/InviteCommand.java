package commands.info;

import cofig.Config;
import core.command.Command;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class InviteCommand extends Command {

    public InviteCommand() {
        super("invite");
        setDescription("Get the invite link for this bot");
    }

    @Override
    public void called(String args) {

        MessageEmbed builder = Config.getDefaultEmbed()
                .setTitle("Invite me!")
                .setDescription("[Click here](https://get.touka.tv)").build();
        reply(builder);
    }
}
