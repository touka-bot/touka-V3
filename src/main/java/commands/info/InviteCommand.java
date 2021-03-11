package commands.info;

import cofig.Config;
import core.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class InviteCommand extends Command {

    public InviteCommand() {
        super("invite", "Get the invite link for this bot");
    }

    @Override
    public void called(String args) {

        MessageEmbed builder = Config.getDefaultEmbed()
                .setTitle("Invite me!")
                .setDescription("[Click here](https://discord.com/api/oauth2/authorize?client_id=783720725848129566&permissions=8192&scope=bot)").build();
        reply(builder);
    }
}
