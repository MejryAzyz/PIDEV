package controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.ServiceHebergement;
import Models.Hebergement;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionHebergement implements Initializable {
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
    private TableColumn<Hebergement, String> colActions;  // Add this line


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

    // Accommodation-related elements
    @FXML
    private TableColumn<Hebergement, String> colId, colNom, colTelephone, colAdresse, colEmail, colCapacite, colTarif;
    @FXML
    private TableView<Hebergement> tableHebergement;
    @FXML
    private VBox ajoutPanel;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private TextField txtPrix, txtAdresse;
    @FXML
    private Label panelTitle;
    @FXML
    private TextField searchField;
    @FXML
    private Text totalHebergementsText;
    @FXML
    private Text capacityHebergementsText;
    @FXML
    private Text tarifHebergementsText;
    private ObservableList<Hebergement> hebergements = FXCollections.observableArrayList();
    private ServiceHebergement serviceHebergement = new ServiceHebergement();
    private boolean isSidebarVisible = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Fetch the data from the service and populate the list
            hebergements.addAll(serviceHebergement.recuperer());
            System.out.println("Loaded " + hebergements.size() + " accommodations");
            updateStatistics();
        } catch (SQLException ex) {
            Logger.getLogger(GestionHebergement.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Set up columns with PropertyValueFactory to bind them to the corresponding fields in Hebergement
        colId.setCellValueFactory(new PropertyValueFactory<>("id_hebergement"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colTarif.setCellValueFactory(new PropertyValueFactory<>("tarif_nuit"));

        // Set the cellFactory for the "Actions" column with styled buttons
        colActions.setCellFactory(param -> new TableCell<Hebergement, String>() {
            private final Button modifyButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox buttonsBox = new HBox(10, modifyButton, deleteButton);

            {
                // Initialize buttons and their styles
                modifyButton.getStyleClass().add("button-action");
                deleteButton.getStyleClass().add("button-delete");

                // Set button actions
                modifyButton.setOnAction(e -> {
                    Hebergement hebergement = getTableView().getItems().get(getIndex());
                    openModifierHebergement(hebergement);
                });

                deleteButton.setOnAction(e -> {
                    Hebergement hebergement = getTableView().getItems().get(getIndex());
                    handleDeleteButton(hebergement);
                });

                buttonsBox.setSpacing(10); // Space between buttons
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                }
            }
        });

        // Bind the data to the table
        tableHebergement.setItems(hebergements);

        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });
    }
    public void updateStatistics() {
        try {
            int totalHebergements = serviceHebergement.getNombreTotalHebergements();
            int capacityHebergements = serviceHebergement.getCapaciteTotale();
            double tarifHebergements = serviceHebergement.getTarifMoyenParNuit();
            tarifHebergementsText.setText(String.format("%.2f TND",tarifHebergements));
            capacityHebergementsText.setText(String.valueOf(capacityHebergements));

            totalHebergementsText.setText(String.valueOf(totalHebergements));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterTable(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableHebergement.setItems(hebergements); // Rétablir la liste complète
        } else {
            ObservableList<Hebergement> filteredList = FXCollections.observableArrayList();
            for (Hebergement hebergement : hebergements) {
                if (hebergement.getNom().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(hebergement);
                }
            }
            tableHebergement.setItems(filteredList);
        }

        // Forcer la mise à jour pour éviter la disparition des boutons Modifier/Supprimer
        tableHebergement.refresh();

        // Forcer JavaFX à redessiner la colonne des actions si nécessaire
        colActions.setVisible(false);
        colActions.setVisible(true);
    }



    private void handleModifyButton(Hebergement hebergement) {
        // Implement the modify action, e.g., opening a dialog to edit the accommodation details
        System.out.println("Modify accommodation: " + hebergement.getId_hebergement());
        // You can add a method to open the edit panel or dialog here
    }

    private void handleDeleteButton(Hebergement hebergement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'hébergement");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet hébergement ?");

        ButtonType buttonTypeYes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                try {
                    serviceHebergement.supprimer(hebergement.getId_hebergement()); // Suppression en base
                    hebergements.remove(hebergement); // Mise à jour de la TableView
                    updateStatistics();
                } catch (SQLException ex) {
                    Logger.getLogger(GestionHebergement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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

    @FXML
    void btnAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutHebergement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter Hébergement");
            stage.setScene(new Scene(root));

            stage.setOnHiding(events -> {
                try {
                    afficherHebergement();
                    updateStatistics();
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
    public void afficherHebergement() throws SQLException {
        ServiceHebergement service = new ServiceHebergement();
        List<Hebergement> h = service.recuperer();
        tableHebergement.getItems().clear();
        tableHebergement.getItems().addAll(h);
    }

    private void refreshTable() {
        try {
            hebergements.setAll(serviceHebergement.recuperer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openModifierHebergement(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierHebergement.fxml"));
            Parent root = loader.load();

            // Get the controller of ModifierHebergement
            ModifierHebergement controller = loader.getController();

            // Pass the hebergement object and the correct parent controller (ListeHebergement)
            GestionHebergement parentController = new GestionHebergement(); // This should be the actual parent controller instance
            controller.setHebergement(hebergement, parentController);

            Stage stage = new Stage();
            stage.setTitle("Modifier Hébergement");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHiding(events -> {
                try {
                    afficherHebergement();
                    updateStatistics();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToGestionTransport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionTransport.fxml"));
            Parent root = loader.load();

            // Get the current stage and replace the scene
            Stage stage = (Stage) labelAccommodationManagement.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de GestionTransport.fxml");
        }
    }

}
