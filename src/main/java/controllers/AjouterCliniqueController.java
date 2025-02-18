package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Clinique;
import services.ServiceClinique;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AjouterCliniqueController {

    private AfficherCliniqueController afficherCliniqueController;

    public void setAfficherCliniqueController(AfficherCliniqueController controller) {
        this.afficherCliniqueController = controller;
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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{8,15}"); // 8 à 15 chiffres
    }




    @FXML
    void ajouterClinique(ActionEvent event) {
        // Récupération des valeurs des champs
        String nom = nomTF.getText().trim();
        String adresse = adresseTF.getText().trim();
        String telephone = telephoneTF.getText().trim();
        String email = emailTF.getText().trim();
        String description = descriptionTF.getText().trim();
        String prixStr = prixTF.getText().trim();


        // Vérification des champs vides
        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty() || description.isEmpty() || prixStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérification du format de l'email
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Format d'email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

        // Vérification du numéro de téléphone
        if (!isValidPhoneNumber(telephone)) {
            showAlert(Alert.AlertType.ERROR, "Numéro de téléphone invalide", "Le numéro de téléphone doit contenir entre 8 et 15 chiffres.");
            return;
        }

        try {
            // Conversion du prix en double
            double prix = Double.parseDouble(prixStr);
            if (prix < 0) {
                showAlert(Alert.AlertType.ERROR, "Valeur incorrecte", "Le prix doit être un nombre positif.");
                return;
            }

            Clinique clinique = new Clinique(nom, adresse, telephone, email, description, prix);

            sc.ajouter(clinique);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Clinique ajoutée avec succès !");

            // Fermer la fenêtre d'ajout
            Stage stage = (Stage) nomTF.getScene().getWindow();
            stage.close();

            /*Parent root = loader.load();
            AfficherCliniqueController ac = loader.getController();
            ac.rechargerTableView();  // Actualiser la TableView pour y ajouter la nouvelle clinique

            // Charger l'écran d'affichage dans la scène principale
            nomTF.getScene().setRoot(root);*/

           /* try {
                Parent root = loader.load();
                AfficherCliniqueController ac = loader.getController();
                ac.setNomAF(nom);
                ac.setDescAF(description);
                ac.setAdrAF(adresse);
                ac.setTelAF(telephone);
                ac.setEmailAF(email);
                ac.setPrixAF(prix);
                nomTF.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }*/


        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer une valeur numérique valide pour le prix.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue : " + e.getMessage());
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
