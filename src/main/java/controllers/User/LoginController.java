package controllers.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Models.User.Utilisateur;
import service.User.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
@FXML
private Button signupButton;


    private ServiceUtilisateur serviceUtilisateur;

    public LoginController() {
        serviceUtilisateur = new ServiceUtilisateur();
    }

    @FXML
    private void handleLoginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter both email and password.");
        } else {
            try {
                Utilisateur utilisateur = serviceUtilisateur.login(email, password);
                if (utilisateur != null) {
                    try {
                        Stage currentStage = (Stage) loginButton.getScene().getWindow();
                        currentStage.close();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
                        Parent root = loader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Afficher Utilisateur");
                        stage.setScene(new Scene(root));
                        stage.show();

                        System.out.println("Login successful!");
                    } catch (IOException e) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface de modification.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to connect to the database.");
            }
        }
    }
    @FXML
    private void handleSignupRedirect() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPersonne.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Person");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) signupButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Sign Up page.");
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

