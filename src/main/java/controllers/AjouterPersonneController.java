package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Utilisateur;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

public class AjouterPersonneController {
    private final ServiceUtilisateur service = new ServiceUtilisateur();
    @FXML
    private Hyperlink loginLink;
    @FXML
    private TextField idRole;

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
    private TextField userPassword;

    @FXML
    private TextField userPhone;


    @FXML
    private void addUser(ActionEvent event) throws SQLException {
        // Convert DatePicker value to java.sql.Date
        Date dateNaissance = Date.from(userDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Create a new Utilisateur object with the form data
        Utilisateur newUser = new Utilisateur(userName.getText(), userLastName.getText(), userMail.getText(),
                userPassword.getText(), userPhone.getText(), dateNaissance, userAdd.getText());

        // Call the service to add the user

        if (newUser != null) {
            service.ajouter(newUser);
            navigateToAfficherUser();
        } else {
            // If adding the user failed, show an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user.");
        }

    }

    // Utility method to navigate to the AfficherUser page
    private void navigateToAfficherUser() {
        try {
            // Load the AfficherUser.fxml page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher User");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current "Ajouter Personne" page
            Stage currentStage = (Stage) userName.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the AfficherUser page.");
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void login() {
        try {
            // Close the current stage (window)
            Stage currentStage = (Stage) loginLink.getScene().getWindow();
            currentStage.close();

            // Load the new scene (Login page)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();

            // Create a new stage for the Login page
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
}}