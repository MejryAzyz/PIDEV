package service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeocodingService {

    public static double[] getCoordinates(String adresse) {
        try {
            // Construct the Nominatim API URL
            String url = "https://nominatim.openstreetmap.org/search?q=" +
                    adresse.replace(" ", "%20") +
                    "&format=json&limit=1";

            // Set up the HTTP connection
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // Nominatim requires a user-agent

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Parse JSON response
            JSONArray jsonArray = new JSONArray(response.toString());
            if (jsonArray.length() > 0) {
                JSONObject obj = jsonArray.getJSONObject(0);
                double lat = obj.getDouble("lat");
                double lon = obj.getDouble("lon");
                return new double[]{lat, lon};
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if the address isn’t found or an error occurs
    }
}