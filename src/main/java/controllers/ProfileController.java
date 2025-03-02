package controllers;

import Session.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Utilisateur;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;

import java.sql.Date;

import java.time.ZoneId;
import java.util.regex.Pattern;

public class ProfileController {
    private final ServiceUtilisateur su = new ServiceUtilisateur();
    @FXML
    public Label UserLabel;
    @FXML
    public HBox ChangePassword;
    @FXML
    public HBox HProfile;
    private Utilisateur utilisateurActuel;


    @FXML
    private DatePicker userDate;

    @FXML
    private TextField userAdd;

    @FXML
    private TextField userLastName;

    @FXML
    private TextField userMail;

    @FXML
    private TextField userName;

    @FXML
    private TextField userPhone;

    public void initData() {

        this.utilisateurActuel = SessionManager.getInstance().getUser();
UserLabel.setText(utilisateurActuel.getNom()+" "+utilisateurActuel.getPrenom());
        userAdd.setText(utilisateurActuel.getAdresse());
        userLastName.setText(utilisateurActuel.getPrenom());
        userMail.setText(utilisateurActuel.getEmail());
        userName.setText(utilisateurActuel.getNom());
        userPhone.setText(utilisateurActuel.getTelephone());

        java.sql.Date dateNaissance = (Date) utilisateurActuel.getDateNaissance();
        if (dateNaissance != null) {
            java.util.Date utilDate = new java.util.Date(dateNaissance.getTime());
            userDate.setValue(utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }

    @FXML
    void modifierUtilisateur(ActionEvent event) {
        String nom = userName.getText().trim();
        String prenom = userLastName.getText().trim();
        String email = userMail.getText().trim();
        String telephone = userPhone.getText().trim();
        String adresse = userAdd.getText().trim();
        Date dateNaissance = Date.valueOf(userDate.getValue());

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || adresse.isEmpty() || dateNaissance == null) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Tous les champs doivent être remplis.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

        if (!isValidPhone(telephone)) {
            showAlert(Alert.AlertType.ERROR, "Numéro invalide", "Le numéro de téléphone doit contenir entre 8 et 15 chiffres.");
            return;
        }

        utilisateurActuel.setNom(nom);
        utilisateurActuel.setPrenom(prenom);
        utilisateurActuel.setEmail(email);
        utilisateurActuel.setTelephone(telephone);
        utilisateurActuel.setAdresse(adresse);
        utilisateurActuel.setDateNaissance(dateNaissance);


        su.modifier(utilisateurActuel);
        showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Utilisateur modifié avec succès !");
        Stage stage = (Stage) userName.getScene().getWindow();
        stage.close();
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
    }
    public void ChangePassword(MouseEvent mouseEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangePassword.fxml"));
            Parent root = loader.load();
            ChangePasswordController controller = loader.getController();
            controller.setUserLabel();
            Stage stage = new Stage();

            stage.setTitle("Add New Person");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ChangePassword.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Sign Up page.");
        }
    }
    public void ProfileDetail(MouseEvent mouseEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileDetail.fxml"));
            Parent root = loader.load();
            ProfileDetailController controller = loader.getController();
            controller.initData();
            Stage stage = new Stage();
            stage.setTitle("Modifier Utilisateur");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ChangePassword.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface de modification.");
        }

    }
}
