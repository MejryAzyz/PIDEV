package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Specialite;
import services.ServiceSpecialite;

import java.sql.SQLException;

public class ModifierSpecialiteController {

    @FXML
    private TextField nomSp;
    private Specialite specialite;
    private AfficherSpecialiteController parentController;


    public void setSpecialite(Specialite specialite, AfficherSpecialiteController parentController) {
        this.specialite = specialite;
        this.parentController = parentController;
        nomSp.setText(specialite.getNom());
    }

    @FXML
    void modifierSpecialite(ActionEvent event) {
        String nouveauNom = nomSp.getText().trim();

        if (!nouveauNom.isEmpty()) {
            try {
                specialite.setNom(nouveauNom);

                ServiceSpecialite serviceSpecialite = new ServiceSpecialite();
                serviceSpecialite.modifier(specialite);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Modification réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Spécialité modifiée avec succès !");
                successAlert.showAndWait();

                Stage stage = (Stage) nomSp.getScene().getWindow();
                stage.close();
                parentController.refreshTable();

            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Erreur lors de la modification");
                errorAlert.setContentText("Impossible de modifier la spécialité. Vérifiez votre connexion à la base de données.");
                errorAlert.showAndWait();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ vide");
            alert.setHeaderText("Nom invalide");
            alert.setContentText("Veuillez entrer un nom de spécialité valide.");
            alert.showAndWait();
        }

    }

}