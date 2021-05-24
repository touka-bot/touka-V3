package commands.search;

import core.command.Command;

public class SearchCommand extends Command {
    public SearchCommand() {
        super("search");
        setDescription("Search for an anime!");
        setExampleUsage("search hunter x hunter");
        setArguments("<animename>");
        setAlias("s");
    }

    @Override
    public void called(String args) {

//        boolean premium = Premium.propExist(event.getAuthor().getId());
//
//        if (event.getGuild().getId().equals("783764380398518292") || premium) {
            new SearchSection(event.getTextChannel().getIdLong(), event.getAuthor().getIdLong(), args, event);
//            return;
//        }
//
//        reply(Config.getDefaultEmbed()
//                .setTitle("Searches unavailable!")
//                .setDescription("Due to too many searches, we had to limit this function to our main discord server.\nPlease join [here](https://join.touka.tv) and try again on our server.\nSorry for the inconvenience\n - touka.tv dev team")
//                .build()
//        );
    }
}