package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Specialite;
import services.ServiceSpecialite;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherSpecialiteController {

    @FXML
    private TableColumn<Specialite, Integer> idSpecCol;

    @FXML
    private TableColumn<Specialite, String> nomSpecCol;

    @FXML
    private TableView<Specialite> table_specialite;
    public void initialize() {
        idSpecCol.setCellValueFactory(new PropertyValueFactory<>("id_specialite"));
        nomSpecCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        try {
            afficherSpecialites();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void afficherSpecialites() throws SQLException {
        ServiceSpecialite service = new ServiceSpecialite();
        List<Specialite> specialites = service.recuperer();
        table_specialite.getItems().clear();
        table_specialite.getItems().addAll(specialites);
    }

    @FXML
    public void ajouterSpecialite() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterSpecialite.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajouter Spécialité");
            stage.setScene(new Scene(root));
            stage.setOnHiding(event -> {
                try {
                    afficherSpecialites();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ouverture de la fenêtre d'ajout");
        }
    }

    @FXML
    private void supprimerSpecialite() {
        Specialite selectedItem = table_specialite.getSelectionModel().getSelectedItem();
        ServiceSpecialite serviceSpecialite = new ServiceSpecialite();

        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Suppression de la spécialité");
            alert.setContentText("Voulez-vous vraiment supprimer la spécialité " + selectedItem.getNom() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    serviceSpecialite.supprimer(selectedItem.getId_specialite());
                    table_specialite.getItems().remove(selectedItem);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Spécialité supprimée avec succès !");
                    successAlert.showAndWait();

                } catch (SQLException e) {
                    System.err.println("Erreur SQL : " + e.getMessage());
                }
            } else {
                System.out.println("Suppression annulée !");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucune spécialité sélectionnée");
            alert.setContentText("Veuillez sélectionner une spécialité à supprimer.");
            alert.showAndWait();
        }

}
    @FXML
    private void modifierSpecialite() {
        Specialite selectedItem = table_specialite.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierSpecialite.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                ModifierSpecialiteController controller = loader.getController();
                controller.setSpecialite(selectedItem, this);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Modifier Spécialité");
                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucune spécialité sélectionnée");
            alert.setContentText("Veuillez sélectionner une spécialité à modifier.");
            alert.showAndWait();
        }
    }
    public void refreshTable() {
        ServiceSpecialite service = new ServiceSpecialite();
        try {
            table_specialite.getItems().setAll(service.recuperer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void afficherLesCliniques(ActionEvent event) {
        try {
            System.out.println("Navigation vers AfficherClinique.fxml...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherClinique.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
}