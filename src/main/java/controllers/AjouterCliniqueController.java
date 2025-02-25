package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Clinique;
import models.Photo;
import services.ServiceClinique;
import services.ServicePhoto;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AjouterCliniqueController {
    private File selectedFile;
    private ServiceClinique serviceClinique;
    private ServicePhoto servicePhoto;
    private AfficherCliniqueController afficherCliniqueController;

    public void setAfficherCliniqueController(AfficherCliniqueController controller) {
        this.afficherCliniqueController = controller;
    }
    public AjouterCliniqueController() {
        this.servicePhoto = new ServicePhoto(); // Assure-toi que l'instance est créée
    }

    private final ServiceClinique sc = new ServiceClinique();

    @FXML
    private TextField adresseTF;

    @FXML
    private TextField descriptionTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField prixTF;

    @FXML
    private TextField telephoneTF;

    @FXML
    private Button uploadPhotoBtn;

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{8,15}"); // 8 à 15 chiffres
    }

    /*@FXML
    void ajouterClinique(ActionEvent event) {
        String nom = nomTF.getText().trim();
        String adresse = adresseTF.getText().trim();
        String telephone = telephoneTF.getText().trim();
        String email = emailTF.getText().trim();
        String description = descriptionTF.getText().trim();
        String prixStr = prixTF.getText().trim();

        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty() || description.isEmpty() || prixStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Format d'email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

        if (!isValidPhoneNumber(telephone)) {
            showAlert(Alert.AlertType.ERROR, "Numéro de téléphone invalide", "Le numéro de téléphone doit contenir entre 8 et 15 chiffres.");
            return;
        }

        try {
            double prix = Double.parseDouble(prixStr);
            if (prix < 0) {
                showAlert(Alert.AlertType.ERROR, "Valeur incorrecte", "Le prix doit être un nombre positif.");
                return;
            }

            Clinique clinique = new Clinique(nom, adresse, telephone, email, description, prix);
            sc.ajouter(clinique);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Clinique ajoutée avec succès !");
            Stage stage = (Stage) nomTF.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer une valeur numérique valide pour le prix.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue : " + e.getMessage());
        }
    }*/
    @FXML
    void ajouterClinique(ActionEvent event) {
        String nom = nomTF.getText().trim();
        String adresse = adresseTF.getText().trim();
        String telephone = telephoneTF.getText().trim();
        String email = emailTF.getText().trim();
        String description = descriptionTF.getText().trim();
        String prixStr = prixTF.getText().trim();

        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty() || description.isEmpty() || prixStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Format d'email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

        if (!isValidPhoneNumber(telephone)) {
            showAlert(Alert.AlertType.ERROR, "Numéro de téléphone invalide", "Le numéro de téléphone doit contenir entre 8 et 15 chiffres.");
            return;
        }

        try {
            double prix = Double.parseDouble(prixStr);
            if (prix < 0) {
                showAlert(Alert.AlertType.ERROR, "Valeur incorrecte", "Le prix doit être un nombre positif.");
                return;
            }

            Clinique clinique = new Clinique(nom, adresse, telephone, email, description, prix);
            sc.ajouterAvecId(clinique);
            if (selectedFile != null) {
                String photoUrl = selectedFile.toURI().toString();
                Photo photo = new Photo(clinique.getIdClinique(), photoUrl);
                servicePhoto.ajouterPhoto(photo);
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Clinique ajoutée avec succès !");
            Stage stage = (Stage) nomTF.getScene().getWindow();
            stage.close();



        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer une valeur numérique valide pour le prix.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue : " + e.getMessage());
        }
    }



    @FXML
    void uploadPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Afficher un message ou une prévisualisation si nécessaire
            System.out.println("Photo sélectionnée : " + selectedFile.getName());
        }
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
