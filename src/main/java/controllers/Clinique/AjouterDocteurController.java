package controllers.Clinique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Models.Clinique.Docteur;
import service.Clinique.ServiceClinique;
import service.Clinique.ServiceDocteur;
import service.Clinique.ServiceSpecialite;

import java.sql.SQLException;
import java.util.List;

public class AjouterDocteurController {

    @FXML
    private ComboBox<String> cliniqueDoc;

    @FXML
    private TextField emailDoc;

    @FXML
    private TextField nomDoc;

    @FXML
    private TextField prenomDoc;

    @FXML
    private ComboBox<String> specDoc;

    @FXML
    private TextField telDoc;

    private final ServiceDocteur serviceDocteur = new ServiceDocteur();
    private final ServiceClinique serviceClinique = new ServiceClinique();
    private final ServiceSpecialite serviceSpecialite = new ServiceSpecialite();

    @FXML
    public void initialize() {
        chargerCliniques();
        chargerSpecialites();
    }

    private void chargerCliniques() {
        try {
            List<String> cliniques = serviceClinique.getAllCliniqueNames();
            ObservableList<String> cliniqueList = FXCollections.observableArrayList(cliniques);
            cliniqueDoc.setItems(cliniqueList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chargerSpecialites() {
        try {
            List<String> specialites = serviceSpecialite.getAllSpecialiteNames();
            ObservableList<String> specialiteList = FXCollections.observableArrayList(specialites);
            specDoc.setItems(specialiteList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterDocteur(ActionEvent event) {
        String nom = nomDoc.getText().trim();
        String prenom = prenomDoc.getText().trim();
        String email = emailDoc.getText().trim();
        String telephone = telDoc.getText().trim();
        String clinique = cliniqueDoc.getValue();
        String specialite = specDoc.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || clinique == null || specialite == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        if (!telephone.matches("\\d{8,15}")) {
            afficherAlerte(Alert.AlertType.ERROR, "Numéro invalide", "Le téléphone doit contenir entre 8 et 15 chiffres.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            afficherAlerte(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

        try {
            int idClinique = serviceClinique.getIdByName(clinique);
            int idSpecialite = serviceSpecialite.getIdByName(specialite);
            Docteur docteur = new Docteur(0, idClinique, idSpecialite, nom, prenom, email, telephone);
            serviceDocteur.ajouter(docteur);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Docteur ajouté avec succès !");
            fermerFenetre();
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le docteur.");
            e.printStackTrace();
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) nomDoc.getScene().getWindow();
        stage.close();
    }

    }


