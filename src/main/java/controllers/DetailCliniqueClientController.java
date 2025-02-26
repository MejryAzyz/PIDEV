package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import models.Clinique;
import models.Docteur;
import models.Photo;
import services.GeocodingService;
import services.MapService;
import services.ServiceDocteur;
import services.ServicePhoto;
import services.ServiceClinique;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.List;


public class DetailCliniqueClientController {

    @FXML
    private ImageView cliniqueImageView;
    @FXML
    private Label nomLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label telLabel;
    @FXML
    private Label prixLabel;
    @FXML
    private VBox docteursContainer;

    @FXML
    private ImageView mapView;  // Ajout de la carte

    @FXML
    private ImageView imageView;



    private ServiceDocteur serviceDocteur;
    private ServiceClinique serviceClinique;

    public DetailCliniqueClientController() {
        this.serviceDocteur = new ServiceDocteur();
        this.serviceClinique = new ServiceClinique();
    }



    public void afficherDetails(Clinique clinique, int specialiteId) throws SQLException {

        String imageUrl = clinique.getPhotoUrl() != null ? clinique.getPhotoUrl() : "clinique2.png";  // Fallback if no photo
        cliniqueImageView.setImage(new Image(imageUrl));


        nomLabel.setText(clinique.getNom());
        descriptionLabel.setText("Description: " + clinique.getDescription());
        adresseLabel.setText("Adresse: " + clinique.getAdresse());
        emailLabel.setText("Email: " + clinique.getEmail());
        telLabel.setText("Téléphone: " + clinique.getTelephone());
        prixLabel.setText("Prix: " + clinique.getPrix() + " €");

        // Récupération des coordonnées de la clinique
        //double[] coords = GeocodingService.getCoordinatesByNameAndAddress(clinique.getAdresse());
        double[] coords = GeocodingService.getCoordinates(clinique.getAdresse());
        if (coords != null) {
            System.out.println("Latitude: " + coords[0] + ", Longitude: " + coords[1]);
            MapService.afficherCarte(mapView, coords[0], coords[1]);
        } else {
            System.out.println("Adresse non trouvée !");
        }



        afficherDocteurs(clinique.getIdClinique(), specialiteId);
    }


    private void afficherDocteurs(int cliniqueId, int specialiteId) throws SQLException {
        List<Docteur> docteurs = serviceDocteur.recupererDocteursParCliniqueEtSpecialite(cliniqueId, specialiteId);

        docteursContainer.getChildren().clear();

        for (Docteur docteur : docteurs) {
            VBox docteurCard = createDocteurCard(docteur);
            docteursContainer.getChildren().add(docteurCard);
        }
    }

    private VBox createDocteurCard(Docteur docteur) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label nomLabel = new Label("Nom:  " + docteur.getNom());
        nomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        Label prenomLabel = new Label("Prénom:  " + docteur.getPrenom());
        prenomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        Label emailLabel = new Label("Email:  " + docteur.getEmail());
        emailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        Label telLabel = new Label("Téléphone:  " + docteur.getTelephone());
        telLabel.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;  -fx-text-fill: #555555;");

        card.getChildren().addAll(nomLabel, prenomLabel, emailLabel, telLabel);
        return card;
    }
}
