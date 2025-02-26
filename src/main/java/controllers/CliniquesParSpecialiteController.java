package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Clinique;
import models.Docteur;
import models.Specialite;
import services.ServiceClinique;
import services.ServiceDocteur;
import services.ServicePhoto;

import java.io.IOException;
import java.sql.SQLException;
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

    private ServicePhoto servicePhoto;
    @FXML
    private Pane specialiteContainer;

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

    public CliniquesParSpecialiteController() {
        this.servicePhoto = new ServicePhoto();
    }

    /*public void afficherCliniques(List<Clinique> cliniques, Specialite spec) {
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
    }*/

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

            // Add the card to the GridPane at (col, row)
            cliniqueContainer.add(card, col, row);

            // Move to the next column
            col++;

            // If the number of columns reaches 3, move to the next row
            if (col == 3) {
                col = 0;
                row++;
            }

            // Handle the click on the card to show clinic details
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

    /*@FXML
    public void retourSpecialites() throws SQLException {
        try {
            // Charger la scène des spécialités à nouveau
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSpecialiteClient.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur pour afficher les spécialités à nouveau
            AfficherSpecialiteClientController controller = loader.getController();
            controller.afficherSpecialites(); // Appel pour ré-afficher les spécialités

            // Changer la scène de la fenêtre actuelle
            Stage stage = (Stage) cliniqueContainer.getScene().getWindow();
            stage.setScene(new Scene(root)); // Réinitialiser la scène
            stage.show(); // Afficher la nouvelle scène
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    @FXML
    private void handleRetourButton() throws IOException {
        // Charger la vue des spécialités (ou la page précédente)
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
        ServiceDocteur serviceDocteur = new ServiceDocteur(); // Service pour récupérer les docteurs

        try {
            // Récupérer la liste de toutes les cliniques et docteurs
            List<Clinique> cliniques = serviceClinique.recupererCliniques();
            List<Docteur> docteurs = serviceDocteur.recuperer();

            // Filtrer les docteurs qui ont la spécialité donnée
            List<Docteur> docteursSpec = docteurs.stream()
                    .filter(docteur -> docteur.getId_specialite() == (spec.getId_specialite())) // On suppose que chaque Docteur a une méthode getSpecialite()
                    .collect(Collectors.toList());

            // Extraire les IDs des cliniques associées à ces docteurs
            Set<Integer> cliniqueIds = new HashSet<>();
            for (Docteur docteur : docteursSpec) {
                cliniqueIds.add(docteur.getId_clinique()); // On suppose que chaque Docteur a une méthode getCliniqueId()
            }

            // Filtrer les cliniques qui sont associées à l'une des cliniques des docteurs filtrés
            List<Clinique> cliniquesSpec = cliniques.stream()
                    .filter(clinique -> cliniqueIds.contains(clinique.getIdClinique())) // On suppose que chaque Clinique a une méthode getId()
                    .collect(Collectors.toList());

            // Afficher les cliniques filtrées
            afficherCliniques(cliniquesSpec, spec);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
