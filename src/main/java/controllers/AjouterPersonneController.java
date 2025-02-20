package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Utilisateur;
import services.ServiceUtilisateur;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

public class AjouterPersonneController {
    private final ServiceUtilisateur service = new ServiceUtilisateur();
    private static final String API_KEY = "8db64f37771bda507f2c11e5663b41f2";

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
    private ImageView imagePreview;
    @FXML
    private String imageUrl;

    @FXML
    private void addUser(ActionEvent event) {
        try {
            if (userName.getText().trim().isEmpty() || userLastName.getText().trim().isEmpty() ||
                    userMail.getText().trim().isEmpty() || userPassword.getText().trim().isEmpty() ||
                    userPhone.getText().trim().isEmpty() || userAdd.getText().trim().isEmpty() ||
                    userDate.getValue() == null || imageUrl == null) {
                showAlert(Alert.AlertType.WARNING, "Champs vides", "Tous les champs doivent être remplis.");
                return;
            }

            if (!isValidEmail(userMail.getText().trim())) {
                showAlert(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer une adresse email valide.");
                return;
            }

            if (!isValidPhone(userPhone.getText().trim())) {
                showAlert(Alert.AlertType.ERROR, "Numéro invalide", "Le numéro de téléphone doit contenir entre 8 et 15 chiffres.");
                return;
            }

            LocalDate localDate = userDate.getValue();
            Date dateNaissance = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Utilisateur newUser = new Utilisateur(
                    userName.getText().trim(), userLastName.getText().trim(), userMail.getText().trim(),
                    userPassword.getText().trim(), userPhone.getText().trim(), dateNaissance, userAdd.getText().trim(), imageUrl
            );

            service.ajouter(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès !");
            navigateToAfficherUser();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout de l'utilisateur.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue.");
        }
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                // Show the image in the preview
                Image image = new Image(file.toURI().toString());
                imagePreview.setImage(image);

                // Upload the image to ImgBB and get the URL
                imageUrl = uploadImageToImgBB(file);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Image téléchargée avec succès.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du téléchargement de l'image.");
            }
        }
    }

    private String uploadImageToImgBB(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadPost = new HttpPost("https://api.imgbb.com/1/upload?key=" + API_KEY);
        uploadPost.setEntity(MultipartEntityBuilder.create()
                .addTextBody("image", encodedString)
                .build());

        CloseableHttpResponse response = httpClient.execute(uploadPost);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = new JSONObject(jsonResponse);
        return jsonObject.getJSONObject("data").getString("url");
    }

    private void navigateToAfficherUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher User");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) userName.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AfficherUser.");
        }
    }
    @FXML
    private void login() {
        try {
            Stage currentStage = (Stage) loginLink.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{8,15}");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
