package controllers.User;

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
import Models.User.Utilisateur;
import service.User.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherUserController {

    @FXML
    private TableView<Utilisateur> table_utilisateur;

    @FXML
    private TableColumn<Utilisateur, Integer> col_id;

    @FXML
    private TableColumn<Utilisateur, String> col_nom;

    @FXML
    private TableColumn<Utilisateur, String> col_prenom;

    @FXML
    private TableColumn<Utilisateur, String> col_email;

    @FXML
    private TableColumn<Utilisateur, String> col_telephone;

    @FXML
    private TableColumn<Utilisateur, String> col_adresse;

    @FXML
    private TableColumn<Utilisateur, String> col_date_naissance;

    @FXML
    private Button btnModifier;

    private final ServiceUtilisateur su = new ServiceUtilisateur();
    private ObservableList<Utilisateur> utilisateurList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("idUtilisateur"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        col_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        col_date_naissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));

        try {
            afficherUtilisateur();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void afficherUtilisateur() throws SQLException {
        List<Utilisateur> utilisateurs = su.recuperer();
        table_utilisateur.getItems().clear();
        table_utilisateur.getItems().addAll(utilisateurs);
    }

    @FXML
    private void deleteAction() {
        Utilisateur selectedItem = table_utilisateur.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Suppression de l'utilisateur");
            alert.setContentText("Voulez-vous vraiment supprimer l'utilisateur " + selectedItem.getNom() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    su.supprimer(selectedItem.getIdUtilisateur());
                    table_utilisateur.getItems().remove(selectedItem);
                    System.out.println("Utilisateur supprimé avec succès !");
                } catch (SQLException e) {
                    System.err.println("Erreur SQL : " + e.getMessage());
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    @FXML
    private void ouvrirAjoutUtilisateur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUtilisateur.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Utilisateur");
            stage.setScene(new Scene(root));
            stage.setOnHiding(evt -> {
                try {
                    afficherUtilisateur();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible d'ouvrir l'interface d'ajout.");
        }
    }

    @FXML
    private void modifierUtilisateur(ActionEvent event) {
        Utilisateur selectedUser = table_utilisateur.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierUser.fxml"));
                Parent root = loader.load();
                ModifierUserController controller = loader.getController();
                controller.initData(selectedUser);
                Stage stage = new Stage();
                stage.setTitle("Modifier Utilisateur");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                afficherUtilisateur();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface de modification.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML

    public void navUser(ActionEvent actionEvent) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML

    public void NavSpec(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSpecialite.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML

    public void NavTransport(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeTransport.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML
    void NavHeb(ActionEvent event) {
        try {
            System.out.println("Navigation vers ListeHebergement.fxml...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeHebegement.fxml"));
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
    @FXML

    public void NavRes(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML

    public void NavPlanning(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/display.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void NavClinique(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherClinique.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
