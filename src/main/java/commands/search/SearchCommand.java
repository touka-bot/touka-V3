package commands.search;

import core.command.Command;

public class SearchCommand extends Command {
    public SearchCommand() {
        super("search", "search for an anime!", "search hunter x hunter", "<animename>");
    }

    @Override
    public void called(String args) {
        new SearchSection(event.getTextChannel().getIdLong(), event.getAuthor().getIdLong(), args, event);
    }
}
