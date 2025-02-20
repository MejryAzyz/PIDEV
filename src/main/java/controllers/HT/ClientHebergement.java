package controllers.HT;

import Models.HT.Hebergement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.HT.ServiceHebergement;

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
        card.setStyle("-fx-padding: 15px; -fx-border-color: #ddd; -fx-border-radius: 10px; -fx-background-color: #ffffff; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setMinWidth(800);
        card.setMaxWidth(Double.MAX_VALUE);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(120);
        imageView.setFitWidth(180);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(hebergement.getPhotoUrl()));

        VBox details = new VBox(8);
        details.setStyle("-fx-padding: 10px; -fx-background-color: #ffffff; -fx-border-radius: 5px;");

        Label nomLabel = new Label(hebergement.getNom() != null ? hebergement.getNom() : "Nom inconnu");
        nomLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        Label adresseLabel = new Label(hebergement.getAdresse() != null ? "📍 " + hebergement.getAdresse() : "Adresse inconnue");
        adresseLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");

        Label tarifLabel = new Label("💰 " + hebergement.getTarif_nuit() + " € / nuit");
        tarifLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

        Button viewMoreBtn = new Button("Voir plus");
        viewMoreBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-padding: 8px 15px; -fx-background-radius: 5px; -fx-font-size: 14px;");
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
}}
