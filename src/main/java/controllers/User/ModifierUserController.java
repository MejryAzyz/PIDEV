package controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Models.User.Utilisateur;
import service.User.ServiceUtilisateur;

import java.sql.Date;

import java.time.ZoneId;
import java.util.regex.Pattern;

public class ModifierUserController {
    private final ServiceUtilisateur su = new ServiceUtilisateur();
    private Utilisateur utilisateurActuel;
    @FXML
    private AfficherUserController afficherUserController;

    @FXML
    private TableView<Utilisateur> table_User;
    @FXML
    private TextField idUser;

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
    private TextField userPhone;

    public void initData(Utilisateur utilisateur) {
        this.utilisateurActuel = utilisateur;

        idUser.setText(String.valueOf(utilisateur.getIdUtilisateur()));
        idRole.setText(String.valueOf(utilisateur.getIdRole()));
        userAdd.setText(utilisateur.getAdresse());
        userLastName.setText(utilisateur.getPrenom());
        userMail.setText(utilisateur.getEmail());
        userName.setText(utilisateur.getNom());
        userPhone.setText(utilisateur.getTelephone());

        java.sql.Date dateNaissance = (Date) utilisateur.getDateNaissance();
        if (dateNaissance != null) {
            // Convert to java.util.Date
            java.util.Date utilDate = new java.util.Date(dateNaissance.getTime());
            // Convert to LocalDate
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
}
