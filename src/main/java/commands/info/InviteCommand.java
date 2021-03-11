package commands.info;

import cofig.Config;
import core.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

//TODO use invite command or change or delete it
public class InviteCommand extends Command {
    private static final String INVITE_LINK =
            "(<link>)";

    public InviteCommand() {
        super("invite", "Get the invite link for this bot");
    }

    @Override
    public void called(String args) {

        EmbedBuilder builder = Config.getDefaultEmbed()
                .setTitle("Invite Killua to your server!")
                .addField("Invite link", "[Invite]" + INVITE_LINK, true)
                .setFooter("This bot was made by myself");
        reply(builder.build());
    }
}
