package commands.search;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApiRequest {

    private static final String BASE_URL = "http://4c3711.xyz:3000/touka/api/v2/getanime/";

    private List<String> shows;
    private List<String> episodes;

    public void fetchShows(String _provider, String _query) {
        System.out.println("Fetching shows...");

        shows = List.of("hunter-x-hunter-2011",
                "/hunter-x-hunter",
                "/hunter-x-hunter-ova",
                "/hunter-x-hunter-ova-2",
                "/hunter-x-hunter-ova-3",
                "/hunter-x-hunter-2011-dubbed",
                "/hunter-x-hunter-dubbed",
                "/hunter-x-hunter-phantom-rouge-dubbed",
                "/hunter-x-hunter-the-last-mission",
                "/hunter-x-hunter-movie-2-the-last-mission-dubbed",
                "/hunter-x-hunter-phantom-rouge",
                "/hunter-x-hunter-movie-2-the-last-mission"
        );
    }

    public List<String> getShows() {

        return shows;
    }

    public void fetchEpisodes(int showIndex) {
        String show = shows.get(showIndex);
        episodes = IntStream.range(0, 1000).mapToObj(String::valueOf).collect(Collectors.toUnmodifiableList());
        System.out.println("Fetching episodes...");
    }

    public int getEpisodeAmount(int index) {
        return episodes.size();
    }

    public List<String> getEpisodes() {
        return episodes;
    }

    public String getEpisode(int index) {
        return episodes.get(index);
    }
}