package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Models.Hebergement;

import java.io.IOException;

public class Viewhebergement {

    @FXML
    private Label titreLabel, nomLabel, adresseLabel, tarifLabel, descriptionLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Button retourButton, bookButton;

    private Hebergement hebergement;

    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;

        // Handle null or empty values gracefully
        String name = hebergement.getNom() != null ? hebergement.getNom() : "Unknown";
        String address = hebergement.getAdresse() != null ? hebergement.getAdresse() : "Unknown";
        String price = hebergement.getTarif_nuit() != 0 ? String.valueOf(hebergement.getTarif_nuit()) : "0";

        titreLabel.setText("Accommodation Details: " + name);
        nomLabel.setText("Name: " + name);
        adresseLabel.setText("📍 Address: " + address);
        tarifLabel.setText("💰 Price: " + price + " € / night");

        // Load image dynamically with fallback
        try {
            String photoUrl = hebergement.getPhotoUrl();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                imageView.setImage(new Image(photoUrl));
            } else {
                imageView.setImage(new Image(getClass().getResource("/default-image.jpg").toExternalForm()));
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Failed to load image: " + e.getMessage());
            imageView.setImage(new Image(getClass().getResource("/default-image.jpg").toExternalForm()));
        }
    }

    @FXML
    private void retour() {
        Stage stage = (Stage) retourButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void book() {
        // Add dynamic booking logic here (e.g., open a booking form, show an alert, etc.)
        String name = hebergement.getNom() != null ? hebergement.getNom() : "Unknown";
        System.out.println("Booking for " + name + " initiated!");
        // Example: Show an alert or navigate to a booking page
    }
}