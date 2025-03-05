package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import Models.Hebergement;
import service.GeocodingService;
import service.MapService;

public class Viewhebergement {

    @FXML
    private Label titreLabel, nomLabel, adresseLabel, tarifLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView mapView;

    @FXML
    private Button retourButton;

    private Hebergement hebergement;

    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;

        titreLabel.setText("Détails de : " + hebergement.getNom());
        nomLabel.setText("Nom : " + hebergement.getNom());
        adresseLabel.setText("📍 Adresse : " + hebergement.getAdresse());
        tarifLabel.setText("💰 Tarif : " + hebergement.getTarif_nuit() + " € / nuit");

        imageView.setImage(new Image(hebergement.getPhotoUrl()));

        displayMap(hebergement.getAdresse());
    }

    private void displayMap(String address) {
        try {
            double[] coords = GeocodingService.getCoordinates(address);
            if (coords != null) {
                System.out.println("Latitude: " + coords[0] + ", Longitude: " + coords[1]);
                MapService.afficherCarte(mapView, coords[0], coords[1]);
            } else {
                System.out.println("Adresse non trouvée !");
                mapView.setImage(new Image("file:resources/images/map_not_found.png"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'affichage de la carte : " + e.getMessage());
            mapView.setImage(new Image("file:resources/images/map_error.png"));
        }
    }

    @FXML
    private void retour() {
        Stage stage = (Stage) retourButton.getScene().getWindow();
        stage.close();
    }
}