package Controllers;

import DAO.AccompagnateurDAO;
import Modeles.Accompagnateur;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AccompagnateursMainView {

    @FXML
    private FlowPane flowPane;

    private final AccompagnateurDAO accompagnateurDAO = new AccompagnateurDAO();

    // Constantes pour les styles
    private static final String CARD_STYLE = "-fx-background-color: #ffffff; -fx-border-radius: 15px; " +
            "-fx-padding: 20px; -fx-alignment: center; -fx-min-width: 260px; -fx-max-width: 260px; " +
            "-fx-min-height: 320px; -fx-max-height: 320px; -fx-background-radius: 15px; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);";

    private static final String BUTTON_STYLE = "-fx-border-radius: 20px; -fx-text-fill: white;";
    private static final String BUTTON_VOIR_PLUS_STYLE = "-fx-background-color: #007bff;";
    private static final String BUTTON_CV_STYLE = "-fx-background-color: #28a745;";

    @FXML
    public void initialize() {
        chargerAccompagnateurs();
    }

    private void chargerAccompagnateurs() {
        try {
            List<Accompagnateur> accompagnateurs = accompagnateurDAO.recupererParStatut("recruté");
            if (accompagnateurs.isEmpty()) {
                showErrorAlert("Aucun accompagnateur trouvé.");
            }

            for (Accompagnateur ac : accompagnateurs) {
                Pane card = createAccompagnateurCard(ac);
                flowPane.getChildren().add(card);
            }
        } catch (SQLException e) {
            showErrorAlert("Erreur SQL lors du chargement des accompagnateurs : " + e.getMessage());
        }
    }

    private Pane createAccompagnateurCard(Accompagnateur ac) {
        VBox card = new VBox(10);
        card.setStyle(CARD_STYLE);

        // Image de profil
        ImageView imageView = createProfileImageView(ac);

        // Labels pour les informations
        Label lblUsername = new Label(ac.getUsername());
        lblUsername.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblEmail = new Label(ac.getEmail());
        lblEmail.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        // Boutons
        Button btnVoirPlus = new Button("Voir Plus");
        btnVoirPlus.setStyle(BUTTON_STYLE + BUTTON_VOIR_PLUS_STYLE);
        btnVoirPlus.setOnAction(e -> afficherDetails(ac));

        Button btnCv = new Button("Voir CV");
        btnCv.setStyle(BUTTON_STYLE + BUTTON_CV_STYLE);
        btnCv.setOnAction(e -> ouvrirCV(ac));

        // Ajout des éléments à la carte
        card.getChildren().addAll(imageView, lblUsername, lblEmail, btnVoirPlus, btnCv);
        return card;
    }

    private ImageView createProfileImageView(Accompagnateur ac) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imageView.setClip(new Circle(50, 50, 50));

        // Chargement asynchrone de l'image
        loadImageAsync(imageView, "uploads/" + ac.getPhotoProfil());
        return imageView;
    }

    private void loadImageAsync(ImageView imageView, String imagePath) {
        Task<Image> task = new Task<>() {
            @Override
            protected Image call() {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    return new Image(imageFile.toURI().toString(), true);
                } else {
                    return new Image("file:uploads/default.png");
                }
            }
        };

        task.setOnSucceeded(event -> imageView.setImage(task.getValue()));
        new Thread(task).start();
    }

    private void afficherDetails(Accompagnateur ac) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Profil Accompagnateur");

        VBox popupContent = new VBox(15);
        popupContent.setStyle("-fx-padding: 20px; -fx-alignment: center; -fx-background-color: white;");

        // Image de profil
        ImageView imageView = createProfileImageView(ac);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        // Labels pour les informations détaillées
        Label lblUsername = new Label("Nom : " + ac.getUsername());
        Label lblEmail = new Label("Email : " + ac.getEmail());
        Label lblExperience = new Label("Expérience : " + ac.getExperience());
        Label lblLangues = new Label("Langues : " + ac.getLangues());
        Label lblMotivation = new Label("Motivation : " + ac.getMotivation());

        // Boutons
        Button btnCv = new Button("Voir CV");
        btnCv.setOnAction(e -> ouvrirCV(ac));

        Button btnFermer = new Button("Fermer");
        btnFermer.setOnAction(e -> popupStage.close());

        // Ajout des éléments à la popup
        popupContent.getChildren().addAll(imageView, lblUsername, lblEmail, lblExperience, lblLangues, lblMotivation, btnCv, btnFermer);
        popupStage.setScene(new Scene(popupContent, 350, 450));
        popupStage.showAndWait();
    }

    private void ouvrirCV(Accompagnateur ac) {
        File cvFile = new File("uploads/" + ac.getFichierCv());
        if (!cvFile.exists()) {
            showErrorAlert("Le fichier CV n'existe pas.");
            return;
        }

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(cvFile);
            } catch (IOException e) {
                showErrorAlert("Erreur lors de l'ouverture du fichier CV.");
            }
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}