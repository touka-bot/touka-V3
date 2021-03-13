package data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Storage {

    private static final String DEFAULT_PROVIDER = "animekisa";
    private static final String SEARCHES_PATH = "searches.txt";
    private static final String PROVIDER_PATH = "providers.txt";

    private static Properties providers;
    private static Properties searches;

    public static long getSearchesCount() {
        return searches.keySet().stream()
                .mapToLong(s -> (long) searches.get(s))
                .sum();
    }

    public static void addSearch(String name) {
        if (searches.containsKey(name)) {
            searches.put(name, String.valueOf(Long.parseLong((String) searches.get(name)) + 1));
        } else {
            searches.put(name, "1");
        }
        try {
            searches.store(new FileWriter(SEARCHES_PATH), "");
        } catch (IOException e) {
            System.err.println("Error writing Searches File: " + e.getMessage() + "\n" +
                    "Search: " + name + "\n");
        }
    }

    public static void setProvider(String uid, String provider) {
        providers.setProperty(uid, provider);
        try {
            providers.store(new FileWriter(PROVIDER_PATH), "");
        } catch (IOException e) {
            System.err.println("Error writing Providers File: " + e.getMessage() + "\n" +
                    "User: " + uid + "\n" +
                    "Provider: " + provider);
        }
    }


    public static String getProvider(String userID) {
        return (String) providers.getOrDefault(userID, DEFAULT_PROVIDER);
    }

    public static void init() throws IOException {
        providers = new Properties();
        searches = new Properties();

        providers.load(new FileReader(PROVIDER_PATH));
        searches.load(new FileReader(SEARCHES_PATH));
    }
}
