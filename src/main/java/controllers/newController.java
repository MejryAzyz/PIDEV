package controllers;

import Session.SessionManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.log.Log;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Utilisateur;
import services.ServiceUtilisateur;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

public class newController {
    @FXML
    public JFXTextField emailField;

    @FXML
    JFXPasswordField passwordField;

    @FXML
    private AnchorPane login1;

    @FXML
    private AnchorPane login2;

    @FXML
    private JFXButton Enter_signin;

    @FXML
    private AnchorPane signup1;
    // If you have not this jar file I will give you in description box you download it.
    @FXML
    private AnchorPane signup2;

    @FXML
    private JFXButton Enter_signup;

    @FXML
    private JFXButton Login;

    @FXML
    private JFXButton close;

    // t3 signup
    @FXML
    public JFXTextField SignUpUserName;
    @FXML
    public JFXTextField SignUpUserLastName;
    @FXML
    public DatePicker SignUpUserDate;
    @FXML
    public JFXTextField SignUpUserPhone;
    @FXML
    public JFXTextField SignUpUserAddress;
    @FXML
    public JFXTextField SignUpEmail;
    @FXML
    public JFXPasswordField SignUpPassword;
    @FXML
    public JFXPasswordField ConfirmPassword;



    private ServiceUtilisateur serviceUtilisateur;

    public newController() {
        serviceUtilisateur = new ServiceUtilisateur();
    }

    @FXML
    private void handleChangeAction(ActionEvent event) {

        if(event.getSource()==Enter_signin)
        {
            login1.setVisible(true);
            login2.setVisible(false);
            signup2.setVisible(true);
            signup1.setVisible(false);
        }
        else if(event.getSource()==Enter_signup)
        {
            login1.setVisible(false);
            login2.setVisible(true);
            signup2.setVisible(false);
            signup1.setVisible(true);
        }
        else if(event.getSource()==close)
        {
            System.exit(0);
        }
    }


    public void handleChangeAction(javafx.event.ActionEvent event) { if(event.getSource()==Enter_signin)
    {
        login1.setVisible(true);
        login2.setVisible(false);
        signup2.setVisible(true);
        signup1.setVisible(false);
    }
    else if(event.getSource()==Enter_signup)
    {
        login1.setVisible(false);
        login2.setVisible(true);
        signup2.setVisible(false);
        signup1.setVisible(true);
    }
    else if(event.getSource()==close)
    {
        System.exit(0);
    }
    }

    @FXML
    public void handleLoginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter both email and password.");
        } else {
            try {
                Utilisateur utilisateur = serviceUtilisateur.login(email, password);

                if (utilisateur != null) {

                    if (utilisateur.getVerif()==0) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Verify your account please check your email.=>" + utilisateur.getEmail());
                    }
                    else {
                        SessionManager.getInstance().setUser(utilisateur);
                        redirectToPage(utilisateur.getIdRole());

                        Stage currentStage = (Stage) Login.getScene().getWindow();
                        currentStage.close();


                        System.out.println("Login successful!");
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
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    ///// hedhy partie signup mekhdhinha men ajouter Personne controller

    @FXML
    public void ActionSignUp(javafx.event.ActionEvent event) {
        try {
            if (SignUpUserName.getText().trim().isEmpty() || SignUpUserLastName.getText().trim().isEmpty() ||
                    SignUpEmail.getText().trim().isEmpty() || SignUpPassword.getText().trim().isEmpty() ||
                    SignUpUserPhone.getText().trim().isEmpty() || SignUpUserAddress.getText().trim().isEmpty() ||
                    SignUpUserDate.getValue() == null ){
                showAlert(Alert.AlertType.WARNING, "Champs vides", "Tous les champs doivent être remplis.");
                return;
            }

            if (!isValidEmail(SignUpEmail.getText().trim())) {
                showAlert(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer une adresse email valide.");
                return;
            }
            if (!SignUpPassword.getText().trim().equals(ConfirmPassword.getText().trim())) {
                showAlert(Alert.AlertType.ERROR, "Mots de passe non identiques", "Veuillez entrer deux mots de passe identiques.");
                return;
            }

            if (!isValidPhone(SignUpUserPhone.getText().trim())) {
                showAlert(Alert.AlertType.ERROR, "Numéro invalide", "Le numéro de téléphone doit contenir entre 8 et 15 chiffres.");
                return;
            }

            LocalDate localDate = SignUpUserDate.getValue();
            Date dateNaissance = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Utilisateur newUser = new Utilisateur(
                    SignUpUserName.getText().trim(), SignUpUserLastName.getText().trim(), SignUpEmail.getText().trim(),
                    SignUpPassword.getText().trim(), SignUpUserPhone.getText().trim(), dateNaissance, SignUpUserAddress.getText().trim(),null
            );

            serviceUtilisateur.ajouter(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "An Email has been sent to verify your account ! \n After verification you can login.");


        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout de l'utilisateur.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue.");
        }
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{8,15}");
    }

    private void redirectToPage(int roleId) {
        String PagePath = "";

        switch (roleId) {
            case 1:
                PagePath = "/AfficherUser.fxml";
                break;
            case 2:
                PagePath = "UserPath";
                break;
            case 3:
                PagePath = "ResponsablePath";
                break;
            case 4:
                PagePath = "AccPath";
                break;
            default:
                System.out.println("Invalid role.");
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PagePath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher Utilisateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
