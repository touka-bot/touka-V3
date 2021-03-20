package commands.search;

import cofig.Config;
import core.sections.Section;
import data.Storage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.discordbots.api.client.DiscordBotListAPI;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletionStage;

public class SearchSection extends Section {

    private static final String URL_FILTER_PATTERN = "https?://.+?\\.\\w\\w/";

    private static final long TIMEOUT = 60000;


    private static final DiscordBotListAPI dblApi = new DiscordBotListAPI.Builder()
            .token(Config.DBL_TOKEN)
            .botId("783720725848129566")
            .build();
    private static final int MAX_SHOW_LINES = 10;

    private CompletionStage<Boolean> dplRequest;
    private SearchState state;
    private final ApiRequest request;


    public SearchSection(long textChannelID, long userID, String query, MessageReceivedEvent event) {
        super(textChannelID, userID);
        this.event = event;
        this.request = new ApiRequest();

        if (query.isBlank()) {
            reply("Query must not be empty");
            dispose();
            return;
        }

        setSectionTimeout(TIMEOUT);
        dplRequest = dblApi.hasVoted(String.valueOf(userID));

        state = SearchState.ENTERED_QUERY;
        nextStep(query);
    }

    private void nextStep(String args) {
        try {
            switch (state) {
                case ENTERED_QUERY -> searchShow(args);
                case RECEIVED_SHOW_LIST -> selectShow(args);
                case RECEIVED_EPISODE_LIST -> selectEpisode(args);
            }
        } catch (IOException e) {
            sendUnexpectedError(e);
        }
    }

    @Override
    public void called(String args) {
        if (args.equals("x")) {
            reply("Section closed.");
            dispose();
            return;
        }

        switch (state) {
            case ENTERED_QUERY, SELECTED_SHOW -> reply("Please wait until the results are found. Exit the section with 'x'");
            default -> nextStep(args);
        }
    }

    private void searchShow(String query) {
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


    private void selectShow(String args) throws IOException {
        try {
            int showIndex = Integer.parseInt(args);
            sendEpisodes(request.fetchEpisodes(showIndex).size());
            state = SearchState.RECEIVED_EPISODE_LIST;
        } catch (NumberFormatException e) {
            sendExpectedError(e);
        }
    }

    private void selectEpisode(String args) throws IOException {
        try {
            if (args.trim().matches("\\d+ *- *\\d+")) { // x-y
                String[] split = args.trim().split(" *- *");
                int from = Integer.parseInt(split[0]); //inclusive
                int to = Integer.parseInt(split[1]); //inclusive

                if (to - from > 12) {
                    reply("Please do not request more than 12 episodes at once.");
                    return;
                }

                for (int i = from; i <= to; i++) {
                    sendEpisode(request.fetchEpisode(i), i);
                }

            } else {
                int episode = Integer.parseInt(args.trim());

                sendEpisode(request.fetchEpisode(episode), episode);
            }

            dispose();
        } catch (NumberFormatException e) {
            sendExpectedError(e);
        }
    }

    private void sendShows(List<String> shows) {

        List<MessageEmbed> embeds = createShowEmbeds(shows);

        if (embeds.size() > 1) {
            reply(60000, embeds.toArray(new MessageEmbed[0]));
        } else {
            reply(embeds.get(0));
        }
    }

    private List<MessageEmbed> createShowEmbeds(List<String> shows) {
        StringBuilder sb = new StringBuilder();
        List<MessageEmbed> embeds = new ArrayList<>();

        int lines = 0;
        sb.append("```"); //open
        for (int i = 0; i < shows.size(); i++) {
            sb.append(i).append(" ").append(shows.get(i).replaceAll(URL_FILTER_PATTERN, "")).append("\n");
            lines++;

            if ((sb.length() > 800 || lines > MAX_SHOW_LINES) && i < shows.size() - 1) { //pgae is full; create a new page but only if its not the last show
                sb.append("```"); //close
                embeds.add(Config.getDefaultEmbed()
                        .addField("Enter the number of the show.", sb.toString(), false)
                        .build());
                sb = new StringBuilder();
                sb.append("```"); //open
                lines = 0;
            }
        }

        sb.append("```"); //close
        embeds.add(Config.getDefaultEmbed()
                .addField("Shows", sb.toString(), false)
                .build());

        return embeds;
    }

    private void sendEpisodes(int episodeAmount) {
        String reply = "This Anime has " + episodeAmount + "  episodes!\n" +
                "Type a Number from 1 to " + episodeAmount + "  to select an episode. :ringed_planet:\n" +
                "Tip: To select multiple episodes, simply type the range of episodes you want to watch! e.g. 4 - 10";

        reply(reply);
    }

    private void sendEpisode(String link, int episodeIndex) throws IOException {
        String show = request.getShowName();

        EmbedBuilder builder = Config.getDefaultEmbed()
                .setDescription("Ready to Watch!")
                .setTitle(show + " - episode " + episodeIndex);

        boolean hasVoted = dplRequest.toCompletableFuture().join();

        if (hasVoted) {
            builder.addField("Downloads", "[[Server 1]](" + link + ")", false);
        } else {
            builder.addField("Downloads", "[Vote for Downloads](https://top.gg/bot/783720725848129566/vote)", false);
        }

        String webViewLink = formatWebViewLink(link, episodeIndex);
        String thumbnail = request.fetchThumbnail();

        builder.addField("Web View", "[Web View](" + webViewLink + ")", false)
                .setThumbnail(thumbnail);

        reply(builder.build());
    }

    private String formatWebViewLink(String link, int episodeIndex) throws UnsupportedEncodingException {
        return String.format("https://4c3711.xyz/touka/watch?url=%s&title=%s&ep=%s",
                Base64.getUrlEncoder().encodeToString(link.getBytes(StandardCharsets.UTF_8)),
                URLEncoder.encode(request.getShowName(), StandardCharsets.UTF_8.toString()),
                episodeIndex);
    }

    private void sendExpectedError(Exception e) {
        reply("Invalid input, please try again. Exit the section with 'x'");
    }

    private void sendUnexpectedError(Exception e) {
        reply("An unexpected error occurred and has been reported to the Touka dev team. Please try again.");
        dispose();
        e.printStackTrace();
        request.dump();
    }


    @Override
    protected void dispose() {
        if (Math.random() > 0.8) {
            reply(getToolTip());
        }
        super.dispose();
    }

    public static String getToolTip() {
        int i = new Random().nextInt(4);
        return switch (i) {
            case 0 -> "**Like this Bot?** Recommend it to your Friends: <https://bit.ly/36Dwbij> :sparkling_heart:";
            case 1 -> ":sparkling_heart: **Like this Bot?** Leave a review here:\nhttps://top.gg/bot/783720725848129566";
            case 2 -> ":sparkling_heart: Join our **Anime Community** here:\nhttps://discord.gg/tvDXKZSzqd";
            default -> ":sparkling_heart: **Like this Bot?** Vote for **Touka** here:\nhttps://top.gg/bot/783720725848129566";
        };
    }
}

enum SearchState {
    ENTERED_QUERY, RECEIVED_SHOW_LIST, SELECTED_SHOW, RECEIVED_EPISODE_LIST, ERROR
}