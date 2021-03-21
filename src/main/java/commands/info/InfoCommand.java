package commands.info;

import cofig.Config;
import core.command.Command;
import data.Storage;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.text.NumberFormat;
import java.util.Locale;

public class InfoCommand extends Command {

    public InfoCommand() {
        super("info");
        setDescription("Info about me!");
    }

    @Override
    public void called(String args) {
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);

        int serverCount = Config.getGuildCount();

        MessageEmbed embed = Config.getDefaultEmbed()
                .setTitle("Info about me")
                .setDescription("Watch & Browse Anime Series & Movies without any Ads!")
                .addField("Servers", nf.format(serverCount), true)
                .addField("Searches", "" + nf.format(Storage.getSearchesCount()), true)
                .addField("Discord Server", "[Join!](https://discord.gg/tvDXKZSzqd)", false)
                .addField("Contribute to the Project", "[GitHub Repo](https://github.com/touka-bot)",true)
                .addField("top.gg", "[top.gg/touka](https://top.gg/bot/783720725848129566)", true)
                .setFooter("Version v" + Config.VERSION).build();
        reply(embed);
    }
}
