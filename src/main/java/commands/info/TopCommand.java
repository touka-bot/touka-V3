package commands.info;

import cofig.Config;
import core.command.Command;
import data.Storage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;


public class TopCommand extends Command {

    public TopCommand() {
        super("top", "Get a list of the top searches");
    }

    @Override
    public void called(String args) {
        EmbedBuilder builder = Config.getDefaultEmbed();
        builder.setTitle("Top searched shows");

        Storage.getTopSearches(10)
                .map(s -> new MessageEmbed.Field(s.getKey(), "> **" + s.getValue() + "** searches", false))
                .forEach(builder::addField);

        reply(builder.build());
    }
}