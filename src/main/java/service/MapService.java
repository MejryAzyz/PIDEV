package service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapService {

    public static void afficherCarte(ImageView mapView, double latitude, double longitude) {
        // Yandex expects longitude,latitude order
        String mapUrl = "https://static-maps.yandex.ru/1.x/?ll=" + longitude + "," + latitude +
                "&size=650,450" + // Use a standard size
                "&z=12&l=map" +
                "&pt=" + longitude + "," + latitude + ",pm2rdm" +
                "&lang=fr_FR";

        System.out.println("Generated Map URL: " + mapUrl);

        // Load the image asynchronously
        Image mapImage = new Image(mapUrl, true);

        // Add listeners for debugging
        mapImage.progressProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 1.0) {
                System.out.println("Map image loaded successfully.");
            }
        });
        mapImage.errorProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                System.out.println("Error loading map: " + mapImage.getException().getMessage());
            }
        });

        mapView.setImage(mapImage);
        mapView.setPreserveRatio(true);
        mapView.setFitWidth(700);  // Match your FXML
        mapView.setFitHeight(300); // Match your FXML

        // Immediate error check
        if (mapImage.isError()) {
            System.out.println("Immediate error: " + mapImage.getException());
            mapView.setImage(new Image("file:resources/images/map_error.png"));
        }
    }
}