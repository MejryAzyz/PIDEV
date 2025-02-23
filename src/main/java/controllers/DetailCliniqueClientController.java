package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Clinique;

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

    public void afficherDetails(Clinique clinique) {
        // Afficher l'image de la clinique
        String imageUrl = "clinique2.png"; // Remplace par l'image réelle de la clinique
        cliniqueImageView.setImage(new Image(imageUrl));

        // Remplir les labels avec les données de la clinique
        nomLabel.setText(clinique.getNom());
        descriptionLabel.setText("Description: " + clinique.getDescription());
        adresseLabel.setText("Adresse: " + clinique.getAdresse());
        emailLabel.setText("Email: " + clinique.getEmail());
        telLabel.setText("Téléphone: " + clinique.getTelephone());
        prixLabel.setText("Prix: " + clinique.getPrix() + " €");

    }
}
