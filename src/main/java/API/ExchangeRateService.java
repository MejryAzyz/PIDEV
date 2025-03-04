package API;
import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ExchangeRateService {
    private static final String API_KEY = "973a9ff7ac02f9537b9c9949";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static double getExchangeRate(String baseCurrency, String targetCurrency) throws IOException {
        String url = BASE_URL + API_KEY + "/latest/" + baseCurrency;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur API: " + response);
            }

            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);

            if (!json.has("conversion_rates")) {
                throw new IOException("Réponse invalide de l'API.");
            }

            JSONObject rates = json.getJSONObject("conversion_rates");
            return rates.getDouble(targetCurrency);
        }
    }
    public class ImageUploader {
        private static final String API_KEY = "38589e312a02b67fcd8133b20f51e63e";

        public static String uploadImage(File imageFile) throws IOException {
            OkHttpClient client = new OkHttpClient();

            // Read image as bytes
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);

            // Create request body
            RequestBody requestBody = new FormBody.Builder()
                    .add("key", API_KEY)
                    .add("image", base64Image)
                    .build();

            // Send request
            Request request = new Request.Builder()
                    .url("https://api.imgbb.com/1/upload")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getJSONObject("data").getString("url");
        }
    }
}
