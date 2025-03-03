package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Clinique;
import models.Docteur;
import models.Specialite;
import org.json.JSONArray;
import org.json.JSONObject;
import services.ServiceClinique;
import services.ServiceDocteur;
import services.ServiceGiminAI;
import services.ServicePhoto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CliniquesParSpecialiteController {
    @FXML
    private GridPane cliniqueContainer;
    @FXML
    private Button retourButton;
    @FXML
    private ImageView imageView ;

    @FXML
    private TextField searchField;  // Champ de recherche
    @FXML
    private Button searchButton;

    private ServicePhoto servicePhoto;
    private ServiceGiminAI serviceGiminAI;
    @FXML
    private Pane specialiteContainer;



    public CliniquesParSpecialiteController() {
        this.servicePhoto = new ServicePhoto();
        this.serviceGiminAI = new ServiceGiminAI();
    }



    //2
    /*public void afficherCliniques(List<Clinique> cliniques, Specialite spec) {
        cliniqueContainer.getChildren().clear();  // Vider le conteneur avant d'afficher les nouvelles cliniques

        // Configuration du GridPane
        cliniqueContainer.setVgap(10);
        cliniqueContainer.setHgap(10);
        cliniqueContainer.setStyle("-fx-padding: 20px;");

        int row = 0;
        int col = 0;

        // Affichage des cliniques
        for (Clinique clinique : cliniques) {
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: #f8f8f8; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-alignment: center-left;");

            // Récupérer l'URL de l'image depuis la base de données
            String imageUrl = null;
            try {
                imageUrl = servicePhoto.getPhotoUrlByClinique(clinique.getIdClinique());
                System.out.println("Image URL: " + imageUrl);  // Vérifier l'URL
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // ImageView pour l'image de la clinique
            ImageView imageView = new ImageView();
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    // Si l'URL est relative, tu peux ajouter un chemin complet comme "file:///"
                    imageView.setImage(new Image("file:///" + imageUrl));
                } catch (Exception e) {
                    System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
                    imageView.setImage(new Image("file:clinique2.png"));  // Image par défaut
                }
            } else {
                imageView.setImage(new Image("file:clinique2.png"));  // Image par défaut
            }



         // Ajouter l'image à la carte
            card.getChildren().add(imageView);

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
            card.getChildren().add(infoBox);

            // Ajouter la carte au GridPane
            cliniqueContainer.add(card, col, row);
            col++;

            if (col == 3) {  // Limiter à 3 colonnes
                col = 0;
                row++;
            }

            // Gérer le clic pour ouvrir les détails
            card.setOnMouseClicked(event -> {
                try {
                    ouvrirDetailsClinique(clinique, spec.getId_specialite());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }*/

    public void afficherCliniques(List<Clinique> cliniques, Specialite spec) {
        // Clear existing content
        cliniqueContainer.getChildren().clear();

        // Configure the GridPane (spacing between items)
        cliniqueContainer.setVgap(10);  // Vertical spacing
        cliniqueContainer.setHgap(10);  // Horizontal spacing
        cliniqueContainer.setStyle("-fx-padding: 20px;");

        int row = 0;
        int col = 0;

        for (Clinique clinique : cliniques) {
            // Create a HBox for each clinic (clinic card)
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: #f8f8f8; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-alignment: center-left;");
            System.out.println("Photo URL: " + clinique.getPhotoUrl());

            // Image of the clinic
            String imageUrl = clinique.getPhotoUrl() != null ? clinique.getPhotoUrl() : "clinique2.png";  // Fallback if no photo
            ImageView imageView = new ImageView(new Image(imageUrl));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            // Clinic information
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
            cliniqueContainer.add(card, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
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
    private void handleRetourButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSpecialiteClient.fxml"));
        AnchorPane root = loader.load();

        // Obtenir la scène actuelle
        Stage stage = (Stage) retourButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(Specialite spec) {
        ServiceClinique serviceClinique = new ServiceClinique();
        ServiceDocteur serviceDocteur = new ServiceDocteur();

        try {
            List<Clinique> cliniques = serviceClinique.recupererCliniques();
            List<Docteur> docteurs = serviceDocteur.recuperer();

            List<Docteur> docteursSpec = docteurs.stream()
                    .filter(docteur -> docteur.getId_specialite() == (spec.getId_specialite()))
                    .collect(Collectors.toList());

            Set<Integer> cliniqueIds = new HashSet<>();
            for (Docteur docteur : docteursSpec) {
                cliniqueIds.add(docteur.getId_clinique());
            }

            List<Clinique> cliniquesSpec = cliniques.stream()
                    .filter(clinique -> cliniqueIds.contains(clinique.getIdClinique()))
                    .collect(Collectors.toList());
            afficherCliniques(cliniquesSpec, spec);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rechercherGiminAI() {
        String recherche = searchField.getText();  // Récupérer la question de l'utilisateur

        if (recherche != null && !recherche.trim().isEmpty()) {
            try {
                JSONArray cliniquesArray = serviceGiminAI.rechercherCliniquesParBesoins(recherche);

                cliniqueContainer.getChildren().clear();  // Vider la liste actuelle

                for (int i = 0; i < cliniquesArray.length(); i++) {
                    JSONObject cliniqueJson = cliniquesArray.getJSONObject(i);

                    Clinique clinique = new Clinique(
                            cliniqueJson.getInt("id_clinique"),
                            cliniqueJson.getString("nom"),
                            cliniqueJson.getString("adresse"),
                            cliniqueJson.getString("description"),
                            cliniqueJson.getString("telephone"),
                            cliniqueJson.getString("email"),
                            cliniqueJson.getDouble("prix")
                    );

                    Specialite spec = new Specialite();
                    afficherCliniqueDansUI(Arrays.asList(clinique), spec);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void afficherCliniqueDansUI(List<Clinique> cliniques, Specialite spec) {
        /*Label cliniqueLabel = new Label(clinique.getNom() + " - Prix: " + clinique.getPrix() + " €");
        cliniqueLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        cliniqueContainer.add(cliniqueLabel, 0, index);*/

        cliniqueContainer.getChildren().clear();

        // Configure the GridPane (spacing between items)
        cliniqueContainer.setVgap(10);  // Vertical spacing
        cliniqueContainer.setHgap(10);  // Horizontal spacing
        cliniqueContainer.setStyle("-fx-padding: 20px;");

        int row = 0;
        int col = 0;

        for (Clinique clinique : cliniques) {
            // Create a HBox for each clinic (clinic card)
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: #f8f8f8; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-alignment: center-left;");
            System.out.println("Photo URL: " + clinique.getPhotoUrl());

            // Image of the clinic
            String imageUrl = clinique.getPhotoUrl() != null ? clinique.getPhotoUrl() : "clinique2.png";  // Fallback if no photo
            ImageView imageView = new ImageView(new Image(imageUrl));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            // Clinic information
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
            cliniqueContainer.add(card, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
            card.setOnMouseClicked(event -> {
                if (spec != null) {
                    try {
                        ouvrirDetailsClinique(clinique, spec.getId_specialite());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("La spécialité est null. Impossible de charger les détails de la clinique.");
                }
            });
        }
        cliniqueContainer.requestLayout();

    }


}
