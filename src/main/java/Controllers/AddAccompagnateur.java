package Controllers;

import Modeles.Accompagnateur;
import DAO.AccompagnateurDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddAccompagnateur {

    @FXML
    private TextField usernameField, emailField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    private TextArea experienceField, motivationField;

    @FXML
    private CheckBox francaisCheck, anglaisCheck, espagnolCheck;

    @FXML
    private ComboBox<String> francaisLevel, anglaisLevel, espagnolLevel;

    @FXML
    private Button cvUploadButton, photoUploadButton, postulerButton;

    @FXML
    private ImageView photoPreview;

    private final AccompagnateurDAO accompagnateurDAO = new AccompagnateurDAO();
    private final String uploadDir = "uploads/";

    private String cvPath = "";
    private String photoPath = "";

    @FXML
    private void postuler() {
        try {
            if (fieldsAreInvalid()) return;

            // Hachage du mot de passe
            String hashedPassword = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());

            // Gestion des langues parlées
            List<String> languesParlees = new ArrayList<>();
            if (francaisCheck.isSelected()) languesParlees.add("Français: " + francaisLevel.getValue());
            if (anglaisCheck.isSelected()) languesParlees.add("Anglais: " + anglaisLevel.getValue());
            if (espagnolCheck.isSelected()) languesParlees.add("Espagnol: " + espagnolLevel.getValue());

            String languesJson = new Gson().toJson(languesParlees);

            // Date actuelle
            String dateRecrutement = LocalDate.now().toString();

            // Ajouter l'accompagnateur à la base de données
            accompagnateurDAO.ajouter(new Accompagnateur(
                    usernameField.getText(),
                    hashedPassword,
                    emailField.getText(),
                    cvPath,
                    photoPath,
                    experienceField.getText(),
                    motivationField.getText(),
                    languesJson,
                    "En attente",
                    dateRecrutement
            ));

            showAlert("Succès", "Votre candidature a été envoyée !", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean fieldsAreInvalid() {
        if (usernameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.", Alert.AlertType.ERROR);
            return true;
        }
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas.", Alert.AlertType.ERROR);
            return true;
        }
        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Erreur", "Veuillez entrer une adresse e-mail valide.", Alert.AlertType.ERROR);
            return true;
        }
        if (cvPath.isEmpty() || photoPath.isEmpty()) {
            showAlert("Erreur", "Veuillez télécharger un CV et une photo.", Alert.AlertType.ERROR);
            return true;
        }
        return false;
    }

    @FXML
    private void uploadCv() {
        File selectedFile = chooseFile("Fichiers PDF", "*.pdf");
        if (selectedFile != null) {
            cvPath = saveFile(selectedFile);
            if (!cvPath.isEmpty()) {
                showAlert("Succès", "CV enregistré : " + cvPath, Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    private void uploadPhoto() {
        File selectedFile = chooseFile("Images", "*.png", "*.jpg", "*.jpeg");
        if (selectedFile != null) {
            photoPath = saveFile(selectedFile);
            File imageFile = new File(uploadDir + photoPath);
            if (imageFile.exists()) {
                photoPreview.setImage(new Image(imageFile.toURI().toString()));
                showAlert("Succès", "Photo enregistrée : " + photoPath, Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "L'image n'a pas été correctement enregistrée.", Alert.AlertType.ERROR);
            }
        }
    }

    private File chooseFile(String description, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extensions));
        return fileChooser.showOpenDialog(null);
    }

    private String saveFile(File file) {
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getName();
            File destFile = new File(directory, fileName);
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            if (destFile.exists()) {
                return fileName;
            } else {
                showAlert("Erreur", "Le fichier n'a pas pu être enregistré.", Alert.AlertType.ERROR);
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'enregistrer le fichier.", Alert.AlertType.ERROR);
            return "";
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
