package controllers;

import Session.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Utilisateur;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.ZoneId;

public class ProfileDetailController {
    @FXML
    public Label UserLabel;
    @FXML
    public HBox EditProfile;
    @FXML
    public HBox HChangePass;

    private Utilisateur utilisateurActuel;


    @FXML
    private Label userDate;

    @FXML
    private Label userAdd;

    @FXML
    private Label userLastName;

    @FXML
    private Label userMail;

    @FXML
    private Label userName;

    @FXML
    private Label userPhone;

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
            userDate.setText(utilisateurActuel.getDateNaissance().toString());
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

            Stage currentStage = (Stage) EditProfile.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface de modification.");
        }

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

            Stage currentStage = (Stage) HChangePass.getScene().getWindow();
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
