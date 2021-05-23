package data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.*;

public class Premium {
    private static Properties properties;
    static File file = new File("premium.tv");

    public static void init(){
        properties = new Properties();
        try {
            InputStream in = new FileInputStream(file);
            properties.load(in);
            //in.close();
            //load();
        } catch (IOException e) {
            fileNotFoundAction(file);
            init();
        }
    }

    public static boolean propExist(String key) {
        return properties.getProperty(key) != null;
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static String addKey(String key, String value) {
        properties.put(key, value);
        return value;
    }

    public static void deleteKey(String hash) {
        properties.remove(hash);
    }

    private static void fileNotFoundAction(File f){
        try {
            properties.store(new FileOutputStream(f), "touka.tv premium users");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
