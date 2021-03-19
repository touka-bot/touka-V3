package data;

import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Properties;
import java.util.stream.Stream;

public class Storage {

    private static final String SEARCHES_PATH = "searches.s";

    private static Properties searches;

    public static Stream<KeyPair> getTopSearches(int amount) {
        return searches.keySet().stream()
                .map(k -> new KeyPair(k, searches.get(k)))
                .sorted(Comparator.reverseOrder())
                .limit(amount);
    }

    public static long getSearchesCount() {
        return searches.keySet().stream()
                .mapToLong(s -> Long.parseLong((String) searches.get(s)))
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

    public static void init() throws IOException {
        searches = new Properties();

        searches.load(new FileReader(SEARCHES_PATH));
    }

    public static class KeyPair implements Comparable<KeyPair>{
        private final String key;
        private final int value;

        public KeyPair(Object key, Object value) {
            this.key = (String) key;
            this.value = Integer.parseInt((String) value);
        }

        public String getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }


        @Override
        public int compareTo(@NotNull Storage.KeyPair o) {
            return Integer.compare(this.value, o.value);
        }
    }
}
