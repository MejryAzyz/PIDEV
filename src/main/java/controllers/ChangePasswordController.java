package controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Utilisateur;
import services.ServiceUtilisateur;
import Session.SessionManager;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

public class ChangePasswordController {
    @FXML
    public Label UserLabel ;
    @FXML
    public HBox HProfil;
    @FXML
    public HBox HEdit;
    @FXML
    private PasswordField userAncPass;

    @FXML
    private PasswordField userNewPass;

    @FXML
    private PasswordField userConfPass;

    private final ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();



    @FXML
    private void modifierUtilisateur() {
        Utilisateur currentUser = SessionManager.getInstance().getUser();

        if (currentUser == null) {
            showAlert("Error", "User not found.", Alert.AlertType.ERROR);
            return;
        }

        String oldPassword = userAncPass.getText();
        String newPassword = userNewPass.getText();
        String confirmPassword = userConfPass.getText();

        if (!BCrypt.checkpw(oldPassword, currentUser.getMotDePasse())) {
            showAlert("Error", "Incorrect current password.", Alert.AlertType.ERROR);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "New passwords do not match.", Alert.AlertType.ERROR);
            return;
        }

        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        currentUser.setMotDePasse(hashedNewPassword);

        serviceUtilisateur.modifier(currentUser);
        showAlert("Success", "Password updated successfully.", Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
            Stage currentStage = (Stage) HProfil.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface de modification.");
        }

    }
    public void Profile(MouseEvent mouseEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
            Parent root = loader.load();
            ProfileController controller = loader.getController();
            controller.initData();
            Stage stage = new Stage();
            stage.setTitle("Modifier Utilisateur");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) HProfil.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface de modification.");
        }

    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUserLabel() {
        UserLabel.setText(SessionManager.getInstance().getUser().getNom()+" "+SessionManager.getInstance().getUser().getPrenom());
    }
}

