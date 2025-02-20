package controllers.HT;

import Models.HT.Hebergement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.HT.ServiceHebergement;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class AjoutHebergement {
    ServiceHebergement sh = new ServiceHebergement();

    @FXML
    private TextField adresseheb;

    @FXML
    private TextField capaciteheb;

    @FXML
    private TextField emailheb;

    @FXML
    private TextField nomheb;

    @FXML
    private TextField tarifheb;

    @FXML
    private TextField telheb;

    @FXML
    void AjouterHebergement(ActionEvent event) {
        if (!validateInputs()) {
            return; // Stop execution if validation fails
        }

        try {
            Hebergement hebergement = new Hebergement(
                    nomheb.getText(),
                    adresseheb.getText(),
                    Integer.parseInt(telheb.getText()),
                    emailheb.getText(),
                    Integer.parseInt(capaciteheb.getText()),
                    Integer.parseInt(tarifheb.getText())
            );

            sh.ajouter(hebergement);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Hébergement ajouté avec succès !");
            clearFields();
            Stage stage = (Stage) emailheb.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue : " + e.getMessage());
        }
    }


    private boolean validateInputs() {
        if (nomheb.getText().isEmpty() || adresseheb.getText().isEmpty() || telheb.getText().isEmpty() ||
                emailheb.getText().isEmpty() || capaciteheb.getText().isEmpty() || tarifheb.getText().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Champ vide", "Veuillez remplir tous les champs.");
            return false;
        }

        if (!telheb.getText().matches("\\d{8,15}")) {
            showAlert(Alert.AlertType.WARNING, "Numéro de téléphone invalide", "Le téléphone doit contenir entre 8 et 15 chiffres.");
            return false;
        }

        if (!isValidEmail(emailheb.getText())) {
            showAlert(Alert.AlertType.WARNING, "Email invalide", "Veuillez entrer une adresse email valide.");
            return false;
        }

        try {
            int capacite = Integer.parseInt(capaciteheb.getText());
            if (capacite <= 0) {
                showAlert(Alert.AlertType.WARNING, "Capacité invalide", "La capacité doit être un nombre positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Capacité invalide", "Veuillez entrer un nombre entier valide.");
            return false;
        }

        try {
            double tarif = Double.parseDouble(tarifheb.getText());
            if (tarif < 0) {
                showAlert(Alert.AlertType.WARNING, "Tarif invalide", "Le tarif doit être un nombre positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Tarif invalide", "Veuillez entrer un nombre valide.");
            return false;
        }

        return true;
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void clearFields() {
        nomheb.clear();
        adresseheb.clear();
        telheb.clear();
        emailheb.clear();
        capaciteheb.clear();
        tarifheb.clear();
    }
}
