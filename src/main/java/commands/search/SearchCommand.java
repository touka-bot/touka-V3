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
        new SearchSection(event.getTextChannel().getIdLong(), event.getAuthor().getIdLong(), args, event);
    }
}