package data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class External {

    public static String post(String url, String body) throws IOException {

        URL UrlObj = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) UrlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setDoOutput(true);
        connection.setRequestProperty("content-type", "application/json");
        connection.setRequestProperty("X-Api-Key", "3b497a7f-1d0b-46f1-9c46-a8da3dc40b9a");

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

        outputStream.writeBytes(body);

        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = inputReader.readLine()) != null) {
                response.append(inputLine);
            }
            inputReader.close();

            return response.toString();
        }
        return "";
    }

}
