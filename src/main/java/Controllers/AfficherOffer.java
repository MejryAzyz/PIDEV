package Controllers;

import DAO.OffreEmploisDAO;
import Modeles.OffreEmplois;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherOffer {

    @FXML
    private GridPane gridPaneOffers;

    @FXML
    private Button btnRefresh;

    private final OffreEmploisDAO offreDAO = new OffreEmploisDAO();

    @FXML
    public void initialize() {
        chargerOffres();
        btnRefresh.setOnAction(event -> chargerOffres());
    }

    private void chargerOffres() {
        gridPaneOffers.getChildren().clear(); // Vider le GridPane avant de recharger les offres
        try {
            List<OffreEmplois> offres = offreDAO.recuperer();
            int row = 0;
            int col = 0;

            for (OffreEmplois offre : offres) {
                VBox offreBox = new VBox();
                offreBox.setSpacing(10);
                offreBox.setPadding(new Insets(10));
                offreBox.setStyle("-fx-border-color: #cccccc; -fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-border-radius: 10;");

                // Image de l’offre
                ImageView imageView = new ImageView();
                imageView.setFitHeight(100);
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true);

                try {
                    Image image = new Image(offre.getImageURL(), true);
                    imageView.setImage(image);
                } catch (Exception e) {
                    imageView.setImage(new Image(getClass().getResource("/acc.jpg").toExternalForm()));
                }

                // Titre et description
                Label titreLabel = new Label(offre.getTitre());
                titreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Label descriptionLabel = new Label(offre.getDescription());
                descriptionLabel.setWrapText(true);
                descriptionLabel.setMaxWidth(150);

                // Bouton "Postuler"
                Button btnPostuler = new Button("Postuler");
                btnPostuler.setOnAction(event -> ouvrirFenetrePostuler());

                // Ajouter les éléments à la carte
                offreBox.getChildren().addAll(imageView, titreLabel, descriptionLabel, btnPostuler);

                // Ajouter au GridPane avec marge
                gridPaneOffers.add(offreBox, col, row);
                GridPane.setMargin(offreBox, new Insets(10));

                col++;
                if (col == 3) { // 3 offres par ligne
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ouvrirFenetrePostuler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddAccompagnateur.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Postuler comme Accompagnateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
}
