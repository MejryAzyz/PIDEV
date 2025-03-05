package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Models.Hebergement;
import service.ServiceHebergement;
import API.ExchangeRateService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import org.json.JSONObject;

public class ClientHebergement {
    @FXML
    private GridPane hebergementContainer;

    private String userCurrency = "EUR"; // Default currency
    private double exchangeRate = 1.0; // Default exchange rate (EUR to EUR)
    private static final String GEO_API_URL = "http://ip-api.com/json/";
    private static final DecimalFormat df = new DecimalFormat("#.##");

    @FXML
    private void initialize() throws SQLException {
        detectUserLocationAndCurrency();
        afficherHebergements();
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

    private void afficherHebergements() throws SQLException {
        ServiceHebergement serviceHebergement = new ServiceHebergement();
        List<Hebergement> hebergements = serviceHebergement.recuperer();

        int column = 0;
        int row = 0;
        for (Hebergement h : hebergements) {
            HBox card = createHebergementCard(h);
            hebergementContainer.add(card, column, row);
            column++;
            if (column > 2) {
                column = 0;
                row++;
            }
        }
    }

    private HBox createHebergementCard(Hebergement hebergement) {
        HBox card = new HBox(10);
        card.getStyleClass().add("modern-card");

        VBox cardContent = new VBox(10);
        cardContent.setAlignment(Pos.CENTER_LEFT);
        cardContent.setPrefWidth(300);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(180);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(false);

        String photoUrl = hebergement.getPhotoUrl();
        try {
            imageView.setImage(new Image(photoUrl != null && !photoUrl.isEmpty() ? photoUrl : getClass().getResource("/default-image.jpg").toExternalForm()));
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Failed to load image: " + e.getMessage());
            imageView.setImage(null);
        }

        Label addressLabel = new Label(hebergement.getAdresse() != null ? "📍 " + hebergement.getAdresse() : "Address unknown");
        addressLabel.getStyleClass().add("card-address");

        VBox details = new VBox(10);
        details.setPrefWidth(200);
        Label nameLabel = new Label(hebergement.getNom() != null ? hebergement.getNom() : "Unknown Name");
        nameLabel.getStyleClass().add("card-title");

        // Convert price to user's currency
        double basePrice = hebergement.getTarif_nuit();
        double convertedPrice = basePrice * exchangeRate;
        Label priceLabel = new Label("💰 " + df.format(convertedPrice) + " " + userCurrency + " / night");
        priceLabel.getStyleClass().add("card-price");

        Button viewMoreBtn = new Button("View Details");
        viewMoreBtn.getStyleClass().add("modern-button");
        viewMoreBtn.setOnAction(e -> afficherDetailsHebergement(hebergement));

        details.getChildren().addAll(nameLabel, priceLabel, viewMoreBtn);
        cardContent.getChildren().addAll(imageView, addressLabel, details);
        card.getChildren().add(cardContent);

        card.setMaxHeight(Double.MAX_VALUE);
        cardContent.setMaxHeight(Double.MAX_VALUE);

        card.setOnMouseEntered(e -> card.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 0);"));
        card.setOnMouseExited(e -> card.setStyle(""));

        return card;
    }

    private void afficherDetailsHebergement(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewhebergement.fxml"));
            Parent root = loader.load();

            Viewhebergement controller = loader.getController();
            controller.setHebergement(hebergement);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Accommodation Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}