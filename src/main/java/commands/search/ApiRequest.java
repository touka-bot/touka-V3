package commands.search;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApiRequest {

    private static final String BASE_URL = "http://4c3711.xyz:3000/touka/api/v2/getanime/";
    private final String provider;

    private List<String> shows;
    private String query;
    private List<String> episodes;
    private int showIndex;

    public ApiRequest(String provider) {
        this.provider = provider;
    }

    public void fetchShows(String query, Consumer<List<String>> callback) {
        String route = String.format("%s/%s/", provider, query); //  animekisa/hunter-x-hunter/
        this.query = query;

        System.out.println("Fetching shows...");

        shows = List.of("hunter-x-hunter-2011",
                "https://animekisa.tv/hunter-x-hunter",
                "https://animekisa.tv/hunter-x-hunter-ova",
                "https://animekisa.tv/hunter-x-hunter-ova-2",
                "https://animekisa.tv/hunter-x-hunter-ova-3",
                "https://animekisa.tv/hunter-x-hunter-2011-dubbed",
                "https://animekisa.tv/hunter-x-hunter-dubbed",
                "https://animekisa.tv/hunter-x-hunter-phantom-rouge-dubbed",
                "https://animekisa.tv/hunter-x-hunter-the-last-mission",
                "https://animekisa.tv/hunter-x-hunter-movie-2-the-last-mission-dubbed",
                "https://animekisa.tv/hunter-x-hunter-phantom-rouge",
                "https://animekisa.tv/hunter-x-hunter-movie-2-the-last-mission"
        );

        callback.accept(shows);
    }

    public List<String> getShows() {
        return shows;
    }

    public void fetchEpisodes(int showIndex, Consumer<List<String>> callback) {
        String route = String.format("%s/%s/%d", provider, query, showIndex);
        this.showIndex = showIndex;

        String show = shows.get(showIndex);
        episodes = IntStream.range(0, 1000).mapToObj(String::valueOf).map(e -> "hxh-episode-" + e).collect(Collectors.toUnmodifiableList());
        System.out.println("Fetching episodes...");

        callback.accept(episodes);
    }

    public void fetchEpisode(int episodeIndex, Consumer<String> callback) {
        String route = String.format("%s/%s/%d/%d", provider, query, showIndex, episodeIndex);
        String episode = "https://namespace.media/";

        callback.accept(episode);
    }

    public int getEpisodeAmount() {
        return episodes.size();
    }

    public List<String> getEpisodes() {
        return episodes;
    }

    public String getEpisode(int index) {
        return episodes.get(index);
    }
}