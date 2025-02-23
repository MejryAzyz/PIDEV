package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Clinique;
import services.ServiceClinique;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class LesCliniquesClientsController {
    @FXML
    private GridPane cliniqueContainer;
    private ServiceClinique serviceClinique;

    public LesCliniquesClientsController() {
        this.serviceClinique = new ServiceClinique();
    }
    @FXML
    public void initialize() {
        try {
            afficherCliniques();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void afficherCliniques() throws SQLException {
        List<Clinique> cliniques = serviceClinique.recuperer();
        cliniqueContainer.getChildren().clear();

        int row = 0;
        int col = 0;

        for (Clinique clinique : cliniques) {
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: #f8f8f8; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-alignment: center-left;");
            String imageUrl = "clinique2.png";
            ImageView imageView = new ImageView(new Image(imageUrl));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            VBox infoBox = new VBox(5);
            Label nomLabel = new Label(clinique.getNom());
            nomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label adresseLabel = new Label("Adresse: " + clinique.getAdresse());
            Label emailLabel = new Label("Email: " + clinique.getEmail());
            Label telLabel = new Label("Téléphone: " + clinique.getTelephone());
            Label prixLabel = new Label("Prix: " + clinique.getPrix() + " €");  // Ajout du label pour le prix
            prixLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0dae6f;");

            infoBox.getChildren().addAll(nomLabel, adresseLabel, emailLabel, telLabel,prixLabel);
            card.getChildren().addAll(imageView, infoBox);



            cliniqueContainer.add(card, col, row);
            col++;

            if (col > 1) {
                col = 0;
                row++;
            }
        }


        }
    }



