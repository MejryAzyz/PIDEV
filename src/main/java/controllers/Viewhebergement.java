package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import Models.Hebergement;
import service.GeocodingService;
import service.MapService;
import API.ExchangeRateService; // Import your ExchangeRateService

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import org.json.JSONObject;

public class Viewhebergement {

    @FXML
    private Label titreLabel, nomLabel, adresseLabel, tarifLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView mapView;

    @FXML
    private Button retourButton;

    private Hebergement hebergement;
    private String userCurrency = "EUR"; // Default currency
    private double exchangeRate = 1.0; // Default exchange rate (EUR to EUR)
    private static final String GEO_API_URL = "http://ip-api.com/json/";
    private static final DecimalFormat df = new DecimalFormat("#.##");

    @FXML
    private void initialize() {
        detectUserLocationAndCurrency();
    }

    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;

        titreLabel.setText("Détails de : " + hebergement.getNom());
        nomLabel.setText("Nom : " + hebergement.getNom());
        adresseLabel.setText("📍 Adresse : " + hebergement.getAdresse());

        // Convert price to user's currency
        double basePrice = hebergement.getTarif_nuit();
        double convertedPrice = basePrice * exchangeRate;
        tarifLabel.setText("💰 Tarif : " + df.format(convertedPrice) + " " + userCurrency + " / nuit");

        imageView.setImage(new Image(hebergement.getPhotoUrl()));

        displayMap(hebergement.getAdresse());
    }

    // Detect user's location and set currency
    private void detectUserLocationAndCurrency() {
        try {
            // Get user location
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest geoRequest = HttpRequest.newBuilder()
                    .uri(URI.create(GEO_API_URL))
                    .build();

            HttpResponse<String> geoResponse = client.send(geoRequest, HttpResponse.BodyHandlers.ofString());
            JSONObject geoJson = new JSONObject(geoResponse.body());
            String countryCode = geoJson.getString("countryCode");

            // Map country code to currency
            userCurrency = getCurrencyFromCountryCode(countryCode);

            // Get exchange rate using your ExchangeRateService
            exchangeRate = ExchangeRateService.getExchangeRate("EUR", userCurrency);

        } catch (Exception e) {
            System.err.println("Failed to fetch location/currency: " + e.getMessage());
            // Fallback to EUR
            userCurrency = "EUR";
            exchangeRate = 1.0;
        }
    }

    private String getCurrencyFromCountryCode(String countryCode) {
        return switch (countryCode) {
            case "US" -> "USD";  // United States - US Dollar
            case "GB" -> "GBP";  // United Kingdom - British Pound
            case "JP" -> "JPY";  // Japan - Japanese Yen
            case "CA" -> "CAD";  // Canada - Canadian Dollar
            case "AU" -> "AUD";  // Australia - Australian Dollar
            case "TN" -> "TND";  // Tunisia - Tunisian Dinar
            case "CN" -> "CNY";  // China - Chinese Yuan
            case "IN" -> "INR";  // India - Indian Rupee
            case "BR" -> "BRL";  // Brazil - Brazilian Real
            case "RU" -> "RUB";  // Russia - Russian Rubles
            case "ZA" -> "ZAR";  // South Africa - South African Rand
            case "MX" -> "MXN";  // Mexico - Mexican Peso
            case "KR" -> "KRW";  // South Korea - South Korean Won
            case "SG" -> "SGD";  // Singapore - Singapore Dollar
            case "AE" -> "AED";  // United Arab Emirates - UAE Dirham
            case "SA" -> "SAR";  // Saudi Arabia - Saudi Riyal
            case "CH" -> "CHF";  // Switzerland - Swiss Franc
            case "TR" -> "TRY";  // Turkey - Turkish Lira
            case "EG" -> "EGP";  // Egypt - Egyptian Pound
            case "MA" -> "MAD";  // Morocco - Moroccan Dirham
            default -> "EUR";    // Default to Euro for European countries or unknown
        };
    }

    private void displayMap(String address) {
        try {
            double[] coords = GeocodingService.getCoordinates(address);
            if (coords != null) {
                System.out.println("Latitude: " + coords[0] + ", Longitude: " + coords[1]);
                MapService.afficherCarte(mapView, coords[0], coords[1]);
            } else {
                System.out.println("Adresse non trouvée !");
                mapView.setImage(new Image("file:resources/images/map_not_found.png"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'affichage de la carte : " + e.getMessage());
            mapView.setImage(new Image("file:resources/images/map_error.png"));
        }
    }

    @FXML
    private void retour() {
        try {
            // Load the previous scene (replace "previous.fxml" with your actual FXML file)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientHebergement.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) retourButton.getScene().getWindow();

            // Set the previous scene
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the updated stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}