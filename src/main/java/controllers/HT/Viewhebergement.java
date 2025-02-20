package controllers.HT;

import Models.HT.Hebergement;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Viewhebergement {

    @FXML
    private Label titreLabel, nomLabel, adresseLabel, tarifLabel;

    @FXML
    private ImageView imageView;

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
    }

    @FXML
    private void retour() {
        Stage stage = (Stage) retourButton.getScene().getWindow();
        stage.close();
    }
}
