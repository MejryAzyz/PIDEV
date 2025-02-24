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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.ServiceTransport;
import Models.Transport;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionTransport implements Initializable {
    @FXML
    private Pane sidebar;
    @FXML
    private ImageView logo;
    @FXML
    private VBox sidebarButtons;  // VBox holding the buttons
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
    private TableColumn<Transport, String> colActions;  // Add this line

    // List of all the icon elements
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
    private TextField searchBar;
    // Transport-related elements
    @FXML
    private TableColumn<Transport, String> colId, colCapacite, colPrix, colType;
    @FXML
    private TableView<Transport> tableTransport;
    @FXML
    private VBox ajoutPanel;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private TextField txtCapacite, txtPrix;
    @FXML
    private Label panelTitle;
    @FXML
    private Text card1Value;
    @FXML
    private Text card2Value;
    @FXML
    private Text cardTotalCapacity;

    private ObservableList<Transport> transports = FXCollections.observableArrayList();
    private ServiceTransport serviceTransport = new ServiceTransport();
    private boolean isSidebarVisible = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            transports.addAll(serviceTransport.recuperer());
            System.out.println("Loaded " + transports.size() + " transports");
            refreshCard();
        } catch (SQLException ex) {
            Logger.getLogger(GestionTransport.class.getName()).log(Level.SEVERE, null, ex);
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("id_transport"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("tarif"));

        // Set the cellFactory for the "Actions" column with styled buttons
        colActions.setCellFactory(param -> new TableCell<Transport, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Create buttons for actions
                    Transport transport = getTableView().getItems().get(getIndex());
                    Button modifyButton = new Button();
                    modifyButton.getStyleClass().add("button-action");  // Apply custom style
                    modifyButton.setOnAction(e -> openModifierTransport(transport));


                    Button deleteButton = new Button();
                    deleteButton.getStyleClass().add("button-delete");  // Apply custom style
                    deleteButton.setOnAction(e -> handleDeleteButton(getTableRow().getItem()));

                    // Layout the buttons in ane HBox
                    HBox buttonsBox = new HBox(10, modifyButton, deleteButton);
                    buttonsBox.setSpacing(10); // Space between buttons
                    setGraphic(buttonsBox);
                }
            }
        });

        tableTransport.setItems(transports);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTransportList(newValue);
        });
    }
    private void filterTransportList(String searchText) {
        if (searchText.isEmpty()) {
            tableTransport.setItems(transports); // Rétablir la liste complète
        } else {
            ObservableList<Transport> filteredList = FXCollections.observableArrayList();
            for (Transport t : transports) {
                if (t.getType().toLowerCase().contains(searchText.toLowerCase()) ||
                        String.valueOf(t.getCapacite()).contains(searchText) ||
                        String.valueOf(t.getTarif()).contains(searchText)) {
                    filteredList.add(t);
                }
            }
            tableTransport.setItems(filteredList);
        }

        tableTransport.refresh();
    }




    private void handleDeleteButton(Transport transport) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le transport");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce transport ?");

        ButtonType buttonTypeYes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                try {
                    serviceTransport.supprimer(transport.getId_transport()); // Suppression en base
                    transports.remove(transport); // Mise à jour de la TableView
                    refreshCard();
                } catch (SQLException ex) {
                    Logger.getLogger(GestionTransport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutTransport.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter Transport");
            stage.setScene(new Scene(root));

            stage.setOnHiding(events -> {
                try {
                    afficherTransport();
                    refreshCard();
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
    public void refreshCard() {
        // Rafraîchir le nombre de transports
        try {
            int totalTransport = serviceTransport.getNombreTotalTransport();
            card1Value.setText(String.valueOf(totalTransport));
            double tarifMoyen = serviceTransport.getTarifMoyenTransport();
            card2Value.setText(String.format("%.2f TND", tarifMoyen));
            int totalCapacity = serviceTransport.getTotalCapacity();
            cardTotalCapacity.setText(String.valueOf(totalCapacity) + " places");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void afficherTransport() throws SQLException {
        ServiceTransport service = new ServiceTransport();
        List<Transport> t = service.recuperer();
        tableTransport.getItems().clear();
        tableTransport.getItems().addAll(t);
    }
    private void refreshTable() {
        try {
            transports.setAll(serviceTransport.recuperer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void openModifierTransport(Transport transport) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTransport.fxml"));
            Parent root = loader.load();

            ModifierTransport controller = loader.getController();
            controller.setTransport(transport, this);

            Stage stage = new Stage();
            stage.setTitle("Modifier Transport");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

}
    @FXML
    private void goToGestionHebergement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionHebergement.fxml"));
            Parent root = loader.load();

            // Get the current stage and replace the scene
            Stage stage = (Stage) labelAccommodationManagement.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de GestionHebergement.fxml");
        }
    }

}

