package commands.search;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class ApiRequest {

    private static final String BASE_URL = "http://touka.tv:3000/touka/api/v2/";
    private static final String GET_ANIME_URL = BASE_URL + "getanime/";
    private final String provider = "animekisa";

    private List<String> shows;
    private String query;
    private int showIndex;

    private String thumbnail;

    public List<String> fetchShows(String query) throws IOException {
        this.query = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());

        String route = String.format("%s/%s/", provider, this.query); //  animekisa/hunter-x-hunter/

        shows = jsonArrayToList(request(route));

        return shows;
    }

    public List<String> fetchEpisodes(int showIndex) throws IOException {
        String route = String.format("%s/%s/%d", provider, query, showIndex);
        this.showIndex = showIndex;

        return jsonArrayToList(request(route));
    }

    public String fetchEpisode(int episodeIndex) throws IOException {

        String route = String.format("%s/%s/%d/%d", provider, query, showIndex, episodeIndex - 1); //the api works 0 based, the real world is 1 based

        return requestSingle(route);
    }


    public String fetchThumbnail() throws IOException {

        if (thumbnail != null) {
            return thumbnail;
        }

        String route = String.format("%s/%s/%d/thumbnail", provider, query, showIndex);
        thumbnail = requestSingle(route);
        return thumbnail;
    }

    public String getShowName() {
        return replaceLink(shows.get(showIndex));
    }

    public String getShowByKey(String key) throws IOException {
        return readContentFromUrl(BASE_URL + "keys/get/" + key);
    }

    public String getW2GSession(String url) throws IOException {
        String url64 = Base64.getEncoder().encodeToString(url.getBytes());
        return readContentFromUrl(BASE_URL + "w2g/getsession/" + url64);
    }

    private String replaceLink(String s) {
        return s.replace("https://4anime.to/anime/", "")
                .replace("https://animekisa.tv/", "")
                .replace("-", " ");
    }


    private JSONArray request(String route) throws IOException {
        return readJsonFromUrl(GET_ANIME_URL + route);
    }

    private String requestSingle(String route) throws IOException {
        return readContentFromUrl(GET_ANIME_URL + route);
    }

    private JSONArray readJsonFromUrl(String url) throws IOException {
        return new JSONArray(readContentFromUrl(url));
    }

    private String readContentFromUrl(String url) throws IOException {
        //System.out.println("Requesting: '" + url + "'");
        URLConnection conn = new URL(url).openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(20000);

        try (InputStream is = conn.getInputStream();
             InputStreamReader rd = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return readAll(rd);
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static List<String> jsonArrayToList(JSONArray arr) {
        return arr.toList().stream()
                .map(o -> (String) o)
                .collect(Collectors.toList());
    }

    public List<String> getShows() {
        return shows;
    }

    public void dump() {
        System.err.println("Query: " + query);
        System.err.println("Received shows: " + shows);
        System.err.println("Selected show: " + showIndex);
    }
}