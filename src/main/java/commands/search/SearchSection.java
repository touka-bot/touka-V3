package commands.search;

import cofig.Config;
import core.sections.Section;
import data.Storage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.discordbots.api.client.DiscordBotListAPI;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class SearchSection extends Section {

    private static final String URL_FILTER_PATTERN = "https?://.+?\\.\\w\\w/";

    private static final DiscordBotListAPI dblApi = new DiscordBotListAPI.Builder()
            .token(Config.DBL_TOKEN)
            .botId("783720725848129566")
            .build();

    private CompletionStage<Boolean> dplRequest;
    private SearchState state;
    private ApiRequest request;


    public SearchSection(long textChannelID, long userID, String query, MessageReceivedEvent event) {
        super(textChannelID, userID);
        this.event = event;

        if (query.isBlank()) {
            reply("Query must not be empty");
            dispose();
            return;
        }

        this.request = new ApiRequest();

        state = SearchState.ENTERED_QUERY;

        dplRequest = dblApi.hasVoted(String.valueOf(userID));

        try {
            List<String> shows = request.fetchShows(query);
            if (shows.isEmpty()) {
                reply("No shows found.");
                dispose();
                state = SearchState.ERROR;
                return;
            }
            sendShows(shows);
        } catch (IOException e) {
            sendUnexpectedError(e);
        }
        state = SearchState.RECEIVED_SHOW_LIST;

        Storage.addSearch(request.getShowName());
    }

    @Override
    public void called(String args) {
        switch (state) {
            case ENTERED_QUERY, SELECTED_EPISODE, SELECTED_SHOW -> reply("Please wait until the results are found.");
            case RECEIVED_SHOW_LIST -> selectShow(args);
            case RECEIVED_EPISODE_LIST -> selectEpisode(args);
        }
    }


    private void selectShow(String args) {
        try {
            int showIndex = Integer.parseInt(args);
            System.out.println("Fetched Episodes");
            sendEpisodes(request.fetchEpisodes(showIndex).size());
            state = SearchState.RECEIVED_EPISODE_LIST;

        } catch (NumberFormatException e) {
            reply("Invalid input, please try again.");
        } catch (IOException e) {
            sendUnexpectedError(e);
        }
    }

    private void selectEpisode(String args) {
        try {
            if (args.trim().matches("\\d+ *- *\\d+")) {
                String[] split = args.trim().split(" *- *");
                int from = Integer.parseInt(split[0]); //inclusive
                int to = Integer.parseInt(split[1]); //inclusive

                for (int i = from; i < to; i++) {
                    sendEpisode(request.fetchEpisode(i), i);
                }

            } else {
                int episode = Integer.parseInt(args.trim());

                sendEpisode(request.fetchEpisode(episode), episode);
            }

            state = SearchState.SELECTED_EPISODE;
        } catch (NumberFormatException e) {
            reply("Invalid input, please try again.");
        } catch (IOException e) {
            sendUnexpectedError(e);
        }
    }

    private void sendShows(List<String> shows) {
        StringBuilder sb = new StringBuilder();
        sb.append("```");

        for (int i = 0; i < shows.size(); i++) {
            sb.append(i).append(" ").append(shows.get(i).replaceAll(URL_FILTER_PATTERN, "")).append("\n");
        }

        sb.append("```");
        reply(sb.toString());
    }

    private void sendEpisodes(int episodeAmount) {
        String s = "This Anime has " + episodeAmount + "  episodes!\n" +
                "Type a Number from 1 to " + episodeAmount + "  to select an episode. :ringed_planet:\n" +
                "Tip: To select multiple episodes, simply type the range of episodes you want to watch! e.g. 4 - 10";

        reply(s);
    }

    private void sendEpisode(String link, int episodeIndex) {
        String show = request.getShowName();

        EmbedBuilder builder = Config.getDefaultEmbed()
                .setDescription("Ready to Watch!")
                .setTitle(show + " - episode " + episodeIndex);


        System.out.println("Waiting for DBL...");
        boolean hasVoted = dplRequest.toCompletableFuture().join();
        System.out.println("User has " + (hasVoted ? "" : "not ") + "voted");

        System.out.println("link: " + link);

        if (hasVoted) {
            builder.addField("Direct View", "[[Host 1]](" + link + ")", false);
        } else {
            builder.addField("Direct View", "[Vote to get Direct View](https://top.gg/bot/783720725848129566/vote)", false);
        }
        builder.addField("Web View", "[FLUID](" + link + ")", false)
                .setFooter("'Direct View' might not always work. In that case use the Web View.");

        reply(builder.build());
        dispose();
    }

    private void sendUnexpectedError(Exception e) {
        reply(Config.getDefaultEmbed()
                .setTitle("Error while requesting episode...")
                .addField("Message: ", e.getMessage(), false)
                .addField("", "Please report this on the [Touka dev server](https://discord.gg/tvDXKZSzqd)", false)
                .build());
    }

}

enum SearchState {
    ENTERED_QUERY, RECEIVED_SHOW_LIST, SELECTED_SHOW, RECEIVED_EPISODE_LIST, SELECTED_EPISODE, ERROR
}