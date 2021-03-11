package commands.search;

import core.sections.Section;

public class SearchSection extends Section {

    private SearchState state;
    private String query;

    public SearchSection(long textChannelID, long userID, String query) {
        super(textChannelID, userID);
        this.query = query;
        state = SearchState.ENTERED_QUERY;
    }

    @Override
    public void called(String args) {
        switch (state) {
            case ENTERED_QUERY, SELECTED_EPISODE, SELECTED_SHOW -> reply("Please wait until the results are found");
            case RECEIVED_SHOW_LIST -> selectShow(args);
            case RECEIVED_EPISODE_LIST -> selectEpisode(args);
        }
    }

    private void selectEpisode(String args) {

        state = SearchState.SELECTED_EPISODE;
    }

    private void selectShow(String args) {

        state = SearchState.SELECTED_EPISODE;
    }
}

enum SearchState {
    ENTERED_QUERY, RECEIVED_SHOW_LIST, SELECTED_SHOW, RECEIVED_EPISODE_LIST, SELECTED_EPISODE
}
