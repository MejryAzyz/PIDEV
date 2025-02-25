package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import models.Clinique;
import models.Specialite;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CliniquesParSpecialiteController {
    @FXML
    private GridPane cliniqueContainer;
    @FXML
    private Button retourButton;// Conteneur pour afficher les cliniques

    /*public void afficherCliniques(List<Clinique> cliniques, Specialite spec) {
        //cliniqueContainer.getChildren().clear();
        GridPane cliniqueGrid = new GridPane();
        cliniqueGrid.setVgap(10); // Espacement vertical
        cliniqueGrid.setHgap(10); // Espacement horizontal
        cliniqueGrid.setStyle("-fx-padding: 20px;");

        int row = 0;
        int col = 0;

        // Affichage des cliniques dans la nouvelle fenêtre
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
            Label prixLabel = new Label("Prix: " + clinique.getPrix() + " €");
            prixLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0dae6f;");

            infoBox.getChildren().addAll(nomLabel, adresseLabel, emailLabel, telLabel, prixLabel);
            card.getChildren().addAll(imageView, infoBox);

            card.setOnMouseClicked(event -> {
                try {
                    ouvrirDetailsClinique(clinique, spec.getId_specialite());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            cliniqueContainer.getChildren().add(card);



        }
    }*/

    public void afficherCliniques(List<Clinique> cliniques, Specialite spec) {
        // Vider le GridPane avant d'ajouter les nouvelles cliniques
        cliniqueContainer.getChildren().clear();

        // Configuration du GridPane (espacement entre les éléments)
        cliniqueContainer.setVgap(10);  // Espacement vertical
        cliniqueContainer.setHgap(10);  // Espacement horizontal
        cliniqueContainer.setStyle("-fx-padding: 20px;");

        int row = 0;
        int col = 0;

        // Affichage des cliniques
        for (Clinique clinique : cliniques) {
            // Créer un HBox pour chaque clinique (carte de la clinique)
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: #f8f8f8; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-alignment: center-left;");

            // Image de la clinique
            String imageUrl = "clinique2.png";  // Remplacez par votre propre image
            ImageView imageView = new ImageView(new Image(imageUrl));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            // Informations de la clinique
            VBox infoBox = new VBox(5);
            Label nomLabel = new Label(clinique.getNom());
            nomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label adresseLabel = new Label("Adresse: " + clinique.getAdresse());
            Label emailLabel = new Label("Email: " + clinique.getEmail());
            Label telLabel = new Label("Téléphone: " + clinique.getTelephone());
            Label prixLabel = new Label("Prix: " + clinique.getPrix() + " €");
            prixLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0dae6f;");

            infoBox.getChildren().addAll(nomLabel, adresseLabel, emailLabel, telLabel, prixLabel);
            card.getChildren().addAll(imageView, infoBox);

            // Ajouter la carte dans le GridPane à la position (col, row)
            cliniqueContainer.add(card, col, row);

            // Passer à la colonne suivante
            col++;

            // Si le nombre de colonnes atteint 3, passez à la ligne suivante
            if (col == 3) {
                col = 0;
                row++;
            }

            // Gérer le clic sur la carte pour afficher les détails de la clinique
            card.setOnMouseClicked(event -> {
                try {
                    ouvrirDetailsClinique(clinique, spec.getId_specialite());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void ouvrirDetailsClinique(Clinique clinique, int specialiteId) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailCliniqueClient.fxml"));
            Parent root = loader.load();
            DetailCliniqueClientController controller = loader.getController();
            controller.afficherDetails(clinique, specialiteId);

            Stage stage = new Stage();
            stage.setTitle("Détails de la Clinique");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void retourSpecialites() {
        // Vous pouvez ici fermer cette fenêtre ou revenir à la fenêtre précédente
        Stage stage = (Stage) cliniqueContainer.getScene().getWindow();
        stage.close(); // Fermer la fenêtre actuelle
    }
}
