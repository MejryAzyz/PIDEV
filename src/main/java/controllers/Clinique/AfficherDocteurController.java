package controllers.Clinique;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Models.Clinique.Clinique;
import Models.Clinique.Docteur;
import service.Clinique.ServiceDocteur;
import service.Clinique.ServiceSpecialite;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherDocteurController {

    @FXML
    private TableColumn<Docteur, String> emailDocCol;

    @FXML
    private TableColumn<Docteur, Integer> idDocCol;

    @FXML
    private TableColumn<Docteur, String> nomDocCol;

    @FXML
    private TableColumn<Docteur, String> prenomDocCol;

    @FXML
    private TableColumn<Docteur, String> specDocCol;

    @FXML
    private TableView<Docteur> table_docteur;

    @FXML
    private TableColumn<Docteur, String> telDocCol;

    private final ServiceDocteur serviceDocteur = new ServiceDocteur();
    private final ServiceSpecialite serviceSpecialite = new ServiceSpecialite();
    private ObservableList<Docteur> docteurList = FXCollections.observableArrayList();
    private Clinique selectedClinique;

    public void setClinique(Clinique clinique) {
        this.selectedClinique = clinique;
        loadDocteurs();
    }

    @FXML
    public void initialize() {
        idDocCol.setCellValueFactory(new PropertyValueFactory<>("id_docteur"));
        nomDocCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomDocCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailDocCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        telDocCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        specDocCol.setCellValueFactory(cellData -> {
            try {
                String specName = serviceSpecialite.getNameById(cellData.getValue().getId_specialite());
                return new javafx.beans.property.SimpleStringProperty(specName);
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("N/A");
            }
        });
    }

    private void loadDocteurs() {
        if (selectedClinique != null) {
            try {
                docteurList.setAll(serviceDocteur.recupererParClinique(selectedClinique.getIdClinique()));
                table_docteur.setItems(docteurList);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les docteurs.");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML private Button ajouterDocteur;
    @FXML
    public void ajouterDocteur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterDocteur.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter Docteur");
            stage.setScene(new Scene(root));
            stage.setOnHiding(event -> {
                try {
                    afficherDocteurs();
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
    private void afficherDocteurs() throws SQLException {
        ServiceDocteur docteur = new ServiceDocteur();
        List<Docteur> docteurs = docteur.recuperer();
        table_docteur.getItems().clear();
        table_docteur.getItems().addAll(docteurs);
    }

    @FXML
    public void modifierDocteur(ActionEvent event) {
        Docteur selectedDocteur = table_docteur.getSelectionModel().getSelectedItem();

        if (selectedDocteur != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierDocteur.fxml"));
                Parent root = loader.load();

                ModifierDocteurController controller = loader.getController();
                controller.setDocteur(selectedDocteur, this);

                Stage stage = new Stage();
                stage.setTitle("Modifier Docteur");
                stage.setScene(new Scene(root));
                stage.setOnHiding(ev -> loadDocteurs());  // Rafraîchir la liste des docteurs après modification
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void supprimerDocteur() {
        Docteur selectedItem = table_docteur.getSelectionModel().getSelectedItem();
        ServiceDocteur serviceDocteur = new ServiceDocteur();

        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Suppression du docteur");
            alert.setContentText("Voulez-vous vraiment supprimer le docteur " + selectedItem.getNom() + " " + selectedItem.getPrenom() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    serviceDocteur.supprimer(selectedItem.getId_docteur());
                    table_docteur.getItems().remove(selectedItem);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Docteur supprimé avec succès !");
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
            alert.setHeaderText("Aucun docteur sélectionné");
            alert.setContentText("Veuillez sélectionner un docteur à supprimer.");
            alert.showAndWait();
        }
    }


}
