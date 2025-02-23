package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Clinique;
import models.Specialite;
import services.ServiceClinique;
import services.ServiceDocteur;
import services.ServiceSpecialite;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherSpecialiteClientController {
    @FXML
    private GridPane specialiteContainer;

    private ServiceClinique serviceClinique;
    private ServiceDocteur serviceDocteur;

    public AfficherSpecialiteClientController() {
        this.serviceClinique = new ServiceClinique();
        this.serviceDocteur = new ServiceDocteur();
    }

    public void initialize() {
        try {
            afficherSpecialites();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void afficherSpecialites() throws SQLException {
        ServiceSpecialite service = new ServiceSpecialite();
        List<Specialite> specialites = service.recuperer();

        specialiteContainer.getChildren().clear();

        int row = 0;
        int col = 0;

        for (Specialite spec : specialites) {
            StackPane card = createSpecialiteCard(spec);
            specialiteContainer.add(card, col, row);
            col++;

            if (col == 3) {
                col = 0;
                row++;
            }
            card.setOnMouseClicked(event -> {
                try {
                    afficherCliniquesParSpecialite(spec.getId_specialite(), spec);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void afficherCliniquesParSpecialite(int specialiteId, Specialite spec) throws SQLException {
        List<Integer> cliniqueIds = serviceDocteur.recupererCliniqueIdsParSpecialite(specialiteId);
        List<Clinique> cliniques = serviceClinique.recupererCliniquesParIds(cliniqueIds);
        afficherCliniques(cliniques, spec);
    }

    private void afficherCliniques(List<Clinique> cliniques, Specialite spec) {
        specialiteContainer.getChildren().clear();

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
            Label prixLabel = new Label("Prix: " + clinique.getPrix() + " €");
            prixLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0dae6f;");

            infoBox.getChildren().addAll(nomLabel, adresseLabel, emailLabel, telLabel, prixLabel );
            card.getChildren().addAll(imageView, infoBox);

            card.setOnMouseClicked(event -> {
                try {
                    ouvrirDetailsClinique(clinique, spec.getId_specialite());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            specialiteContainer.add(card, col, row);
            col++;

            if (col > 1) {
                col = 0;
                row++;
            }
        }
    }

    private StackPane createSpecialiteCard(Specialite spec) {
        StackPane card = new StackPane();
        card.setPrefSize(220, 140);

        Rectangle rect = new Rectangle(220, 140);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        rect.setFill(createGradient());  // Dégradé
        rect.setEffect(new DropShadow(10, Color.LIGHTGRAY));

        Label label = new Label(spec.getNom());
        label.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Arial Rounded MT Bold';");
        label.setAlignment(javafx.geometry.Pos.CENTER);

        card.getChildren().addAll(rect, label);



        return card;
    }

    private LinearGradient createGradient() {
        return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#85C1E9")),  // Bleu clair
                new Stop(1, Color.web("#A8E6CF")));  // Vert pastel
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


}
