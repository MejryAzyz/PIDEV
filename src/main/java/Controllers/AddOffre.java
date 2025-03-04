package Controllers;

import DAO.OffreEmploisDAO;
import Modeles.OffreEmplois;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;

public class AddOffre {

    @FXML
    private Button addButton;

    @FXML
    private DatePicker datePublicationField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField titreField;

    @FXML
    private ComboBox<String> typePosteField;

    @FXML
    private TextField imageUrlField; // Ajout du champ pour l'URL de l'image

    @FXML
    private ImageView imagePreview; // Ajout de l'aperçu de l'image

    @FXML
    void addoffer(ActionEvent event) {
        try {
            if (titreField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            String typePoste = (typePosteField.getValue() != null) ? typePosteField.getValue() : "";
            String imageUrl = imageUrlField.getText(); // Récupération de l'URL de l'image

            OffreEmploisDAO offreDAO = new OffreEmploisDAO();
            OffreEmplois offre = new OffreEmplois(
                    titreField.getText(),
                    descriptionField.getText(),
                    typePoste,
                    datePublicationField.getValue(),
                    imageUrl // Ajout de l'URL de l'image dans l'objet
            );

            offreDAO.ajouter(offre);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'offre a été ajoutée avec succès.");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Problème lors de l'ajout de l'offre : " + e.getMessage());
        }
    }

    @FXML
    void updateImagePreview() {
        String url = imageUrlField.getText().trim();
        if (!url.isEmpty()) {
            try {
                imagePreview.setImage(new Image(url, true));
            } catch (Exception e) {
                System.out.println("Image invalide : " + e.getMessage());
                imagePreview.setImage(null);
            }
        } else {
            imagePreview.setImage(null);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
