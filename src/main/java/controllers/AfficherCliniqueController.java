package controllers;

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
import javafx.stage.Stage;
import models.Clinique;
import services.ServiceClinique;

import java.io.IOException;
import java.util.Optional;
import java.util.List;


import java.sql.SQLException;

public class AfficherCliniqueController {

    @FXML
    private TableView<Clinique> table_clinique;

    @FXML
    private TableColumn<Clinique, String> col_adesse;

    @FXML
    private TableColumn<Clinique, String> col_desc;

    @FXML
    private TableColumn<Clinique, String> col_email;

    @FXML
    private TableColumn<Clinique, Integer> col_id;

    @FXML
    private TableColumn<Clinique, String> col_nom;

    @FXML
    private TableColumn<Clinique, Double> col_prix;

    @FXML
    private TableColumn<Clinique, Integer> col_rate;

    @FXML
    private TableColumn<Clinique, String> col_tel;

    @FXML
    private TextField descAF;

    @FXML
    private TextField emailAF;

    @FXML
    private TextField nomAF;

    @FXML
    private TextField prixAF;

    @FXML
    private TextField telAF;

    @FXML
    private TextField adrAF;


    private final ServiceClinique sc = new ServiceClinique();
    private ObservableList<Clinique> cliniqueList = FXCollections.observableArrayList();

    //modification
    @FXML
    private Button btnModifier;

    @FXML
    public void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<Clinique, Integer>("idClinique"));
        col_nom.setCellValueFactory(new PropertyValueFactory<Clinique, String>("nom"));
        col_adesse.setCellValueFactory(new PropertyValueFactory<Clinique, String>("adresse"));
        col_tel.setCellValueFactory(new PropertyValueFactory<Clinique, String>("telephone"));
        col_email.setCellValueFactory(new PropertyValueFactory<Clinique, String>("email"));
        col_rate.setCellValueFactory(new PropertyValueFactory<Clinique, Integer>("rate"));
        col_desc.setCellValueFactory(new PropertyValueFactory<Clinique, String>("description"));
        col_prix.setCellValueFactory(new PropertyValueFactory<Clinique, Double>("prix"));

        try {
            afficherClinique();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*table_clinique.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnModifier.setDisable(newValue == null);  // Désactiver si aucune ligne n'est sélectionnée
        });*/
    }
    private void afficherClinique() throws SQLException {
        ServiceClinique clinique = new ServiceClinique();
        List<Clinique> cliniques = clinique.recuperer();
        table_clinique.getItems().clear();
        table_clinique.getItems().addAll(cliniques);
    }


    @FXML
    private void deleteAction() {
        Clinique selectedItem = table_clinique.getSelectionModel().getSelectedItem();
        ServiceClinique sc = new ServiceClinique();

        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Suppression de la clinique");
            alert.setContentText("Voulez-vous vraiment supprimer la clinique " + selectedItem.getNom() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    sc.supprimer(selectedItem.getIdClinique());

                    table_clinique.getItems().remove(selectedItem);
                    System.out.println("Clinique supprimée avec succès !");
                } catch (SQLException e) {
                    System.err.println("Erreur SQL : " + e.getMessage());
                }
            } else {
                System.out.println("Suppression annulée !");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucune clinique sélectionnée");
            alert.setContentText("Veuillez sélectionner une clinique à supprimer.");
            alert.showAndWait();
        }
    }


    @FXML
    private void ouvrirAjoutClinique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterClinique.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter une Clinique");
            stage.setScene(new Scene(root));
            stage.setOnHiding(events -> {
                try {
                    afficherClinique(); // Rafraîchir les spécialités
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible d'ouvrir l'interface d'ajout : " + e.getMessage());
        }
    }


    //modification
    @FXML
    private void modifierClinique(ActionEvent event) {
        Clinique selectedClinique = table_clinique.getSelectionModel().getSelectedItem();
        if (selectedClinique != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierClinique.fxml"));
                Parent root = loader.load();
                ModifierCliniqueController mc = loader.getController();
                mc.initData(selectedClinique);
                Stage stage = new Stage();
                stage.setTitle("Modifier Clinique");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                loadCliniqueData(); // Recharger après modification
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible d'ouvrir l'interface de modification : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner une clinique à modifier.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadCliniqueData() {
        try {
            cliniqueList.setAll(sc.recuperer());
            table_clinique.setItems(cliniqueList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de récupérer les données : " + e.getMessage());
        }
    }




    public void rechargerTableView() {
        try {
            ObservableList<Clinique> updatedList = FXCollections.observableArrayList(sc.recuperer());
            table_clinique.setItems(updatedList);
            table_clinique.refresh();  // Rafraîchir la TableView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setDescAF(String descAF) {
        this.descAF.setText(descAF);
    }

    public void setEmailAF(String emailAF) {
        this.emailAF.setText(emailAF);
    }

    public void setNomAF(String nomAF) {
        this.nomAF.setText(nomAF);
    }

    public void setPrixAF(double prixAF) {
        this.prixAF.setText(String.valueOf(prixAF));
    }

    public void setTelAF(String telAF) {
        this.telAF.setText(telAF);
    }

    public void setAdrAF(String adrAF) {
        this.adrAF.setText(adrAF);
    }

    @FXML
    void afficherDocteurs(ActionEvent event) {
        Clinique selectedClinique = table_clinique.getSelectionModel().getSelectedItem();
        if (selectedClinique != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDocteur.fxml"));
                Parent root = loader.load();

                AfficherDocteurController controller = loader.getController();
                controller.setClinique(selectedClinique);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface des docteurs.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune clinique sélectionnée", "Veuillez sélectionner une clinique.");
        }
    }

    @FXML
    void afficherLesSpecialite(ActionEvent event) {
        try {
            System.out.println("Navigation vers AfficherSpecialite.fxml...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSpecialite.fxml"));
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



