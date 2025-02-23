package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Clinique;
import services.ServiceClinique;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class ModifierCliniqueController {
    private final ServiceClinique sc = new ServiceClinique();
    private Clinique clinique;
    @FXML
    private AfficherCliniqueController afficherCliniqueController;

    @FXML
    private TableView<Clinique> table_clinique;

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



    public void initData(Clinique clinique) {
        this.clinique = clinique;

        nomTF.setText(clinique.getNom());
        adresseTF.setText(clinique.getAdresse());
        telephoneTF.setText(clinique.getTelephone());
        emailTF.setText(clinique.getEmail());
        descriptionTF.setText(clinique.getDescription());
        prixTF.setText(String.valueOf(clinique.getPrix()));
    }



    @FXML
    void modifierClinique(ActionEvent event) {
        String nom = nomTF.getText().trim();
        String adresse = adresseTF.getText().trim();
        String telephone = telephoneTF.getText().trim();
        String email = emailTF.getText().trim();
        String description = descriptionTF.getText().trim();
        String prixStr = prixTF.getText().trim();

        if (nom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty() || description.isEmpty() || prixStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Tous les champs doivent être remplis.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

        if (!isValidPhone(telephone)) {
            showAlert(Alert.AlertType.ERROR, "Numéro invalide", "Le numéro de téléphone contenir entre 8 et 15 chiffres.");
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(prixStr);
            if (prix < 0) {
                showAlert(Alert.AlertType.ERROR, "Prix invalide", "Le prix doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Prix invalide", "Veuillez entrer un prix valide.");
            return;
        }

        clinique.setNom(nom);
        clinique.setAdresse(adresse);
        clinique.setTelephone(telephone);
        clinique.setEmail(email);
        clinique.setDescription(description);
        clinique.setPrix(prix);

        try {
            sc.modifier(clinique);
            showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Clinique modifiée avec succès !");
            Stage stage = (Stage) nomTF.getScene().getWindow();
            stage.close();
            if (afficherCliniqueController != null) {
                afficherCliniqueController.rechargerTableView();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue lors de la modification.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{8,15}");
    }}


