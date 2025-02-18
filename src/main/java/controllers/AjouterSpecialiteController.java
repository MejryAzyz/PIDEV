package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Specialite;
import services.ServiceSpecialite;

import java.sql.SQLException;

public class AjouterSpecialiteController {

    @FXML
    private TextField nomSp;

    @FXML
    void ajouterSpecialite(ActionEvent event) {
        String nom = nomSp.getText();

        if (nom.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le nom de la spécialité ne peut pas être vide !");
            alert.showAndWait();
            return;
        }

        Specialite specialite = new Specialite();
        specialite.setNom(nom);
        ServiceSpecialite service = new ServiceSpecialite();

        try {
            service.ajouter(specialite);

            // Afficher une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Spécialité ajoutée avec succès !");
            alert.showAndWait();

            // Fermer la fenêtre après l'ajout
            Stage stage = (Stage) nomSp.getScene().getWindow();
            stage.close(); // Fermer la fenêtre d'ajout
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout de la spécialité !");
            alert.showAndWait();
        }

    }

}
