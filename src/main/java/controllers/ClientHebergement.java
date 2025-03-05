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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ClientHebergement {

    @FXML
    private GridPane hebergementContainer;

    @FXML
    private void initialize() throws SQLException {
        afficherHebergements();
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
            if (column > 2) { // 3 cards per row
                column = 0;
                row++;
            }
        }
    }

    private HBox createHebergementCard(Hebergement hebergement) {
        HBox card = new HBox(10);
        card.getStyleClass().add("modern-card");

        // Vertical box for the entire card content (image + details)
        VBox cardContent = new VBox(10);
        cardContent.setAlignment(Pos.CENTER_LEFT);
        // Remove or increase prefWidth/prefHeight to allow dynamic sizing
        cardContent.setPrefWidth(300); // Keep width but allow dynamic height
        // cardContent.setPrefHeight(450); // Remove or increase this if needed

        // Image
        ImageView imageView = new ImageView();
        imageView.setFitHeight(180);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(false);

        String photoUrl = hebergement.getPhotoUrl();
        try {
            imageView.setImage(new Image(photoUrl != null && !photoUrl.isEmpty() ? photoUrl : getClass().getResource("/default-image.jpg").toExternalForm()));
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Failed to load image: " + e.getMessage());
            imageView.setImage(null); // Use a placeholder or no image
        }

        // Address/Description under the image
        Label addressLabel = new Label(hebergement.getAdresse() != null ? "📍 " + hebergement.getAdresse() : "Address unknown");
        addressLabel.getStyleClass().add("card-address");

        // Card Details (name, price, button)
        VBox details = new VBox(10);
        details.setPrefWidth(200);
        Label nameLabel = new Label(hebergement.getNom() != null ? hebergement.getNom() : "Unknown Name");
        nameLabel.getStyleClass().add("card-title");

        Label priceLabel = new Label("💰 " + hebergement.getTarif_nuit() + " € / night");
        priceLabel.getStyleClass().add("card-price");

        Button viewMoreBtn = new Button("View Details");
        viewMoreBtn.getStyleClass().add("modern-button");
        viewMoreBtn.setOnAction(e -> afficherDetailsHebergement(hebergement));

        details.getChildren().addAll(nameLabel, priceLabel, viewMoreBtn);
        cardContent.getChildren().addAll(imageView, addressLabel, details);
        card.getChildren().add(cardContent);

        // Ensure card can grow vertically
        card.setMaxHeight(Double.MAX_VALUE);
        cardContent.setMaxHeight(Double.MAX_VALUE);

        // Hover Effect
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