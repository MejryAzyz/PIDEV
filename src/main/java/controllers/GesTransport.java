package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import Models.Transport;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import service.ServiceTransport;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GesTransport implements Initializable {

    @FXML
    private TableView<Transport> tabview;

    @FXML
    private TableColumn<Transport, Integer> colId;

    @FXML
    private TableColumn<Transport, String> colType;

    @FXML
    private TableColumn<Transport, Integer> colCapacite;

    @FXML
    private TableColumn<Transport, Double> colPrix;

    @FXML
    private TableColumn<Transport, String> colActions;

    @FXML
    private TextField search_input;

    @FXML
    private Button search_button;

    @FXML
    private Button cancel_search;

    @FXML
    private Button add;



    private ObservableList<Transport> transports = FXCollections.observableArrayList();
    private ServiceTransport serviceTransport = new ServiceTransport();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id_transport"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("tarif"));

        // Configuration de la colonne "Actions" avec des boutons modifier et supprimer
        colActions.setCellFactory(col -> new TableCell<Transport, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                Transport transport = getTableView().getItems().get(getIndex());
                Button btnUpdate = new Button("🔄");
                btnUpdate.setStyle("-fx-background-color: #002966; -fx-background-radius: 60; -fx-border-radius: 60;");
                btnUpdate.setPrefHeight(33);
                btnUpdate.setPrefWidth(36);
                btnUpdate.setTextFill(Color.WHITE);
                btnUpdate.setFont(Font.font("System Bold", 15));
                btnUpdate.setOnAction(event -> handleUpdate(transport));

// Create the Delete button with emoji and style
                Button btnDelete = new Button("❌");
                btnDelete.setStyle("-fx-background-color: #002966; -fx-background-radius: 60; -fx-border-radius: 60;");
                btnDelete.setPrefHeight(33);
                btnDelete.setPrefWidth(36);
                btnDelete.setTextFill(Color.WHITE);
                btnDelete.setFont(Font.font("System Bold", 14));
                btnDelete.setOnAction(event -> handleDelete(transport));

// Create a container for the buttons
                HBox pane = new HBox(btnUpdate, btnDelete);
                pane.setSpacing(10);

// Set the buttons in the row's graphic
                setGraphic(pane);
            }
        });

        // Chargement initial des données
        loadData();

        // Recherche dynamique sur le champ de recherche
        search_input.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTransportList(newValue);
        });

        // Actions des boutons situés à droite
        add.setOnAction(this::handleAdd);


        cancel_search.setOnAction(e -> search_input.clear());
    }

    private void loadData() {
        try {
            transports.setAll(serviceTransport.recuperer());
            tabview.setItems(transports);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Erreur lors du chargement des données.");
        }
    }

    private void filterTransportList(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tabview.setItems(transports);
        } else {
            ObservableList<Transport> filteredList = FXCollections.observableArrayList();
            for (Transport t : transports) {
                if (t.getType().toLowerCase().contains(searchText.toLowerCase()) ||
                        String.valueOf(t.getCapacite()).contains(searchText) ||
                        String.valueOf(t.getTarif()).contains(searchText)) {
                    filteredList.add(t);
                }
            }
            tabview.setItems(filteredList);
        }
    }

    private void handleAdd(ActionEvent event) {
        // Logique d'ajout : vous pouvez ouvrir une nouvelle fenêtre ou afficher un formulaire
        System.out.println("Ajouter transport");
    }

    private void handleUpdate(Transport transport) {
        // Logique de modification : par exemple, ouvrir une fenêtre pour modifier le transport
        System.out.println("Modifier transport: " + transport.getId_transport());
    }

    private void handleDelete(Transport transport) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Suppression du transport");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce transport ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    serviceTransport.supprimer(transport.getId_transport());
                    transports.remove(transport);
                    tabview.refresh();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Erreur lors de la suppression du transport.");
                }
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.show();
    }
}
