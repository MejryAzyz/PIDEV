package controllers;

import Session.SessionManager;
import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Utilisateur;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherUserController {


    public TextField searchBar;
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
    //element
    @FXML
    private Pane sidebar;
    @FXML
    private ImageView logo;
    @FXML
    private VBox sidebarButtons;
    @FXML
    private ToggleButton toggleSidebarBtn;
    @FXML
    private StackPane sidebarBackground;
    @FXML
    private Button btnModifier;

    // List of all the label elements
    @FXML
    private Label labelDashboard;
    @FXML
    private Label labelUserManagement;
    @FXML
    private Label labelClinicalManagement;
    @FXML
    private Label labelSpecialtyManagement;
    @FXML
    private Label labelTransportManagement;
    @FXML
    private Label labelAccommodationManagement;
    @FXML
    private Label labelReservationManagement;
    @FXML
    private Label labelPaymentManagement;
    @FXML
    private Label labelSupportManagement;
    @FXML
    private Label labelLogout;
    @FXML
    private TableColumn<Utilisateur, String> colActions;  // Add this line

    //icon
    @FXML
    private Text iconDashboard;
    @FXML
    private Text iconUserManagement;
    @FXML
    private Text iconClinicalManagement;
    @FXML
    private Text iconSpecialtyManagement;
    @FXML
    private Text iconTransportManagement;
    @FXML
    private Text iconAccommodationManagement;
    @FXML
    private Text iconReservationManagement;
    @FXML
    private Text iconPaymentManagement;
    @FXML
    private Text iconSupportManagement;
    @FXML
    private Text iconLogout;
    @FXML
    private VBox contentBox;

    @FXML
    private HBox Logout;
    private boolean isSidebarVisible = true;

    private ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList();
    private final ServiceUtilisateur su = new ServiceUtilisateur();

    @FXML
    public void initialize() {
        System.out.println("le nom de user connecté est = "+SessionManager.getInstance().getUser().getNom());
        col_id.setCellValueFactory(new PropertyValueFactory<>("idUtilisateur"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        col_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        col_date_naissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colActions.setCellFactory(param -> new TableCell<Utilisateur, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {

                    Button modifyButton = new Button();
                    modifyButton.getStyleClass().add("button-action");  // Apply custom style
                    modifyButton.setOnAction(e -> modifierUtilisateur(getTableRow().getItem()));


                    Button deleteButton = new Button();
                    deleteButton.getStyleClass().add("button-delete");  // Apply custom style
                    deleteButton.setOnAction(e -> deleteAction(getTableRow().getItem()));

                    // Layout the buttons in ane HBox
                    HBox buttonsBox = new HBox(10, modifyButton, deleteButton);
                    buttonsBox.setSpacing(10); // Space between buttons
                    setGraphic(buttonsBox);
                }
            }
        });

        try {
            afficherUtilisateur();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filterUserList(newValue));

    }

    private void afficherUtilisateur() throws SQLException {
        utilisateurs.clear(); // Clear the list before adding new data

        List<Utilisateur> userList = su.recuperer(); // Get the user list from the database
        utilisateurs.addAll(userList); // Add users to the observable list

        table_utilisateur.setItems(utilisateurs); // Set the table data
        table_utilisateur.refresh(); // Refresh the table
    }

    @FXML
    private void deleteAction(Utilisateur utilisateur) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Suppression de l'utilisateur");
            alert.setContentText("Voulez-vous vraiment supprimer l'utilisateur " + utilisateur.getNom() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    su.supprimer(utilisateur.getIdUtilisateur());
                    table_utilisateur.getItems().remove(utilisateur);
                    System.out.println("Utilisateur supprimé avec succès !");
                } catch (SQLException e) {
                    System.err.println("Erreur SQL : " + e.getMessage());
                }
            }

    }



    @FXML
    private void modifierUtilisateur(Utilisateur utilisateur) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierUser.fxml"));
                Parent root = loader.load();
                ModifierUserController controller = loader.getController();
                controller.initData(utilisateur);
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

    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Toggle sidebar visibility
    public void toggleSidebarAction() {
        TranslateTransition sidebarTransition = new TranslateTransition(Duration.millis(300), sidebar);
        TranslateTransition contentTransition = new TranslateTransition(Duration.millis(300), contentBox);

        if (isSidebarVisible) {
            sidebarTransition.setToX(-sidebar.getWidth() + 70);
            sidebar.setPrefWidth(50);
            logo.setVisible(false);
            hideTextLabels(true);
            contentTransition.setByX(-70);
        } else {
            sidebarTransition.setToX(0);
            sidebar.setPrefWidth(230);
            logo.setVisible(true);
            hideTextLabels(false);
            contentTransition.setByX(70);
        }

        sidebarTransition.play();
        contentTransition.play();
        isSidebarVisible = !isSidebarVisible;
    }
    private void hideTextLabels(boolean hide) {
        labelDashboard.setVisible(!hide);
        labelUserManagement.setVisible(!hide);
        labelClinicalManagement.setVisible(!hide);
        labelSpecialtyManagement.setVisible(!hide);
        labelTransportManagement.setVisible(!hide);
        labelAccommodationManagement.setVisible(!hide);
        labelReservationManagement.setVisible(!hide);
        labelPaymentManagement.setVisible(!hide);
        labelSupportManagement.setVisible(!hide);
        labelLogout.setVisible(!hide);
        iconDashboard.setVisible(!hide);
        iconUserManagement.setVisible(!hide);
        iconClinicalManagement.setVisible(!hide);
        iconSpecialtyManagement.setVisible(!hide);
        iconTransportManagement.setVisible(!hide);
        iconAccommodationManagement.setVisible(!hide);
        iconReservationManagement.setVisible(!hide);
        iconPaymentManagement.setVisible(!hide);
        iconSupportManagement.setVisible(!hide);
        iconLogout.setVisible(!hide);
    }

    private void filterUserList(String searchText) {

        if (searchText.isEmpty()) {
            table_utilisateur.setItems(utilisateurs); // Rétablir la liste complète
        } else {
            ObservableList<Utilisateur> filteredList = FXCollections.observableArrayList();
            for (Utilisateur U : utilisateurs) {
                if (U.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        String.valueOf(U.getIdUtilisateur()).contains(searchText) ||
                        U.getAdresse().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(U);
                }
            }
            table_utilisateur.setItems(filteredList);
        }

        table_utilisateur.refresh();
    }


    @FXML
    private void logout() {
        SessionManager.getInstance().logout();  // Clear session

        try {    Stage currentStage = (Stage) Logout.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/new.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher Utilisateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
