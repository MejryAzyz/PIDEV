package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Clinique;
import models.Specialite;
import services.ServiceClinique;
import services.ServiceSpecialite;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AfficherSpecialiteController {

    @FXML
    private TableColumn<Specialite, Integer> idSpecCol;

    @FXML
    private TableColumn<Specialite, String> nomSpecCol;

    @FXML
    private TableView<Specialite> table_specialite;

    @FXML
    private TableColumn<Specialite, String> colActions;

    private final ServiceSpecialite sc = new ServiceSpecialite();
    private ObservableList<Specialite> specialiteList = FXCollections.observableArrayList();

    public void initialize() {
        idSpecCol.setCellValueFactory(new PropertyValueFactory<>("id_specialite"));
        nomSpecCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colActions.setCellFactory(param -> new TableCell<Specialite, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                Specialite specialite = getTableView().getItems().get(getIndex());
                Button btnUpdate = new Button("🔄");
                btnUpdate.setStyle("-fx-background-color: #002966; -fx-background-radius: 60; -fx-border-radius: 60;");
                btnUpdate.setPrefHeight(33);
                btnUpdate.setPrefWidth(36);
                btnUpdate.setTextFill(Color.WHITE);
                btnUpdate.setFont(Font.font("System Bold", 15));
                btnUpdate.setOnAction(event -> openModifierSpecialite(specialite));

                Button btnDelete = new Button("❌");
                btnDelete.setStyle("-fx-background-color: #002966; -fx-background-radius: 60; -fx-border-radius: 60;");
                btnDelete.setPrefHeight(33);
                btnDelete.setPrefWidth(36);
                btnDelete.setTextFill(Color.WHITE);
                btnDelete.setFont(Font.font("System Bold", 14));
                btnDelete.setOnAction(event -> handleDeleteButton(specialite));


                HBox pane = new HBox(btnUpdate, btnDelete);
                pane.setSpacing(10);
                setGraphic(pane);
            }
        });

        table_specialite.setItems(specialiteList);

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
    private void openModifierSpecialite(Specialite specialite) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierSpecialite.fxml"));
            Parent root = loader.load();

            ModifierSpecialiteController controller = loader.getController();
            controller.setSpecialite(specialite,this);

            Stage stage = new Stage();
            stage.setTitle("Modifier Clinique");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleDeleteButton(Specialite specialite) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer cette clinique");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette clinique ?");

        ButtonType buttonTypeYes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                try {
                    sc.supprimer(specialite.getId_specialite()); // Suppression en base
                    specialiteList.remove(specialite); // Mise à jour de la TableView
                } catch (SQLException ex) {
                    Logger.getLogger(AfficherCliniqueController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}