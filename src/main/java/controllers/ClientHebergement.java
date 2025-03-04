package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Models.Hebergement;
import javafx.stage.Stage;
import service.ServiceHebergement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ClientHebergement {

    @FXML
    private VBox hebergementContainer;

    public void initialize() throws SQLException {
        afficherHebergements();
    }

    private void afficherHebergements() throws SQLException {
        ServiceHebergement serviceHebergement = new ServiceHebergement();
        List<Hebergement> hebergements = serviceHebergement.recuperer();

        for (Hebergement h : hebergements) {
            HBox card = createHebergementCard(h);
            hebergementContainer.getChildren().add(card);
        }
    }

    private HBox createHebergementCard(Hebergement hebergement) {
        HBox card = new HBox(15);
        card.getStyleClass().add("card");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(220);
        imageView.setPreserveRatio(false);

        String photoUrl = hebergement.getPhotoUrl();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            imageView.setImage(new Image(photoUrl));
        } else {
            imageView.setImage(new Image("/logo.png"));
        }

        VBox details = new VBox(8);
        Label nomLabel = new Label(hebergement.getNom() != null ? hebergement.getNom() : "Nom inconnu");
        nomLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label adresseLabel = new Label(hebergement.getAdresse() != null ? "\uD83D\uDCCD " + hebergement.getAdresse() : "Adresse inconnue");
        adresseLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        Label tarifLabel = new Label("\uD83D\uDCB0 " + hebergement.getTarif_nuit() + " € / nuit");
        tarifLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        Button viewMoreBtn = new Button("Voir plus");
        viewMoreBtn.getStyleClass().add("view-more");
        viewMoreBtn.setOnAction(e -> afficherDetailsHebergement(hebergement));

        details.getChildren().addAll(nomLabel, adresseLabel, tarifLabel, viewMoreBtn);
        card.getChildren().addAll(imageView, details);

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
            stage.setTitle("Détails de l'Hébergement");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateTo(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
