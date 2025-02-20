package controllers.Clinique;

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

public class ModifierDocteurController {

    @FXML
    private ComboBox<String> cliniqueDocM;

    @FXML
    private TextField emailDocM;

    @FXML
    private TextField nomDocM;

    @FXML
    private TextField prenomDocM;

    @FXML
    private ComboBox<String> specDocM;

    @FXML
    private TextField telDocM;

    private Docteur docteur;
    private AfficherDocteurController afficherDocteurController;
    private ServiceDocteur serviceDocteur = new ServiceDocteur();
    private ServiceClinique serviceClinique = new ServiceClinique();
    private ServiceSpecialite serviceSpecialite = new ServiceSpecialite();


    public void setDocteur(Docteur docteur) {
        this.docteur = docteur;
        nomDocM.setText(docteur.getNom());
        prenomDocM.setText(docteur.getPrenom());
        emailDocM.setText(docteur.getEmail());
        telDocM.setText(docteur.getTelephone());
        chargerCliniques();
        chargerSpecialites();
    }
    public void setDocteur(Docteur docteur, AfficherDocteurController afficherDocteurController) {
        this.docteur = docteur;
        this.afficherDocteurController = afficherDocteurController;
        nomDocM.setText(docteur.getNom());
        prenomDocM.setText(docteur.getPrenom());
        emailDocM.setText(docteur.getEmail());
        telDocM.setText(docteur.getTelephone());
        chargerCliniques();
        chargerSpecialites();

    }

    private void chargerCliniques() {
        try {
            List<String> cliniques = serviceClinique.getAllCliniqueNames();
            cliniqueDocM.getItems().clear();
            cliniqueDocM.getItems().addAll(cliniques);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les cliniques.");
        }
    }

    private void chargerSpecialites() {
        try {
            List<String> specialites = serviceSpecialite.getAllSpecialiteNames();
            specDocM.getItems().clear();
            specDocM.getItems().addAll(specialites);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les spécialités.");
        }
    }

    @FXML
    void modifierDocteur(ActionEvent event) {
        String nom = nomDocM.getText().trim();
        String prenom = prenomDocM.getText().trim();
        String email = emailDocM.getText().trim();
        String telephone = telDocM.getText().trim();
        String clinique = cliniqueDocM.getValue();
        String specialite = specDocM.getValue();

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
            int idClinique = serviceClinique.getIdByName(cliniqueDocM.getValue());
            int idSpecialite = serviceSpecialite.getIdByName(specDocM.getValue());


            docteur.setNom(nom);
            docteur.setPrenom(prenom);
            docteur.setEmail(email);
            docteur.setTelephone(telephone);
            docteur.setId_clinique(idClinique);
            docteur.setId_specialite(idSpecialite);

            serviceDocteur.modifier(docteur);
            afficherAlerte(Alert.AlertType.INFORMATION, "Modification réussie", "Docteur modifié avec succès !");
            fermerFenetre();

        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de modifier le docteur.");
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
        Stage stage = (Stage) nomDocM.getScene().getWindow();
        stage.close();
    }


}
