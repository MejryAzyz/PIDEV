package controllers;

import API.ImageDbUtil;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import Models.Hebergement;
import service.ServiceHebergement;
import javafx.scene.image.Image;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GesHeb implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private TableView<Hebergement> tabview;

    @FXML
    private TableColumn<Hebergement, Integer> colId;
    private File selectedImageFile;
    @FXML
    private TableColumn<Hebergement, String> colNom;

    @FXML
    private TableColumn<Hebergement, String> colAdresse;

    @FXML
    private TableColumn<Hebergement, String> colTelephone;

    @FXML
    private TableColumn<Hebergement, String> colEmail;

    @FXML
    private TableColumn<Hebergement, Integer> colCapacite;

    @FXML
    private TableColumn<Hebergement, Double> colTarifNuit;

    @FXML
    private TableColumn<Hebergement, String> colActions;

    @FXML
    private TextField search_input;

    @FXML
    private Button search_button;

    @FXML
    private Button cancel_search;

    @FXML
    private Button add;

    @FXML
    private VBox sidePanel;

    @FXML
    private TextField nomField;

    @FXML
    private TextField adresseField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField capaciteField;

    @FXML
    private TextField tarifNuitField;

    @FXML
    private Button submitAddButton;

    private ObservableList<Hebergement> hebergements = FXCollections.observableArrayList();
    private ServiceHebergement serviceHebergement = new ServiceHebergement();


    @FXML
    void addHebergement(ActionEvent event) {
        try {
            String[] uploadResult = ImageDbUtil.uploadFile(selectedImageFile.getAbsolutePath());
            String imageUrl = uploadResult[1];
            System.out.println(imageUrl);
            // Create and add a new hebergement object using the input data
            Hebergement hebergement = new Hebergement(
                    nomField.getText(), adresseField.getText(), Integer.parseInt(telephoneField.getText()),
                    emailField.getText(), Integer.parseInt(capaciteField.getText()),
                    Double.parseDouble(tarifNuitField.getText()) , imageUrl
            );

            serviceHebergement.ajouter(hebergement);

            tabview.refresh();

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Hébergement ajouté avec succès !");
            alert.showAndWait();
             // L'URL de l'image uploadée
            // Reload data
            loadData();

            // Clear form after submission
            clearAddForm();
            closeSidePanel();
        } catch (NumberFormatException e) {
            showErrorAlert("Veuillez entrer des valeurs valides pour la capacité et le tarif.");
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    // Show error alert with a custom message
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Close the side panel
    @FXML
    private void closeSidePanel() {
        TranslateTransition slideOutTransition = new TranslateTransition(Duration.seconds(0.5), sidePanel);
        slideOutTransition.setToX(sidePanel.getWidth());
        slideOutTransition.setOnFinished(e -> sidePanel.setVisible(false));
        slideOutTransition.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id_hebergement"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colTarifNuit.setCellValueFactory(new PropertyValueFactory<>("tarif_nuit"));
        colActions.setCellFactory(col -> new TableCell<Hebergement, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                Hebergement hebergement = getTableView().getItems().get(getIndex());
                Button btnUpdate = new Button("🔄");
                btnUpdate.setStyle("-fx-background-color: #002966; -fx-background-radius: 60; -fx-border-radius: 60;");
                btnUpdate.setPrefHeight(33);
                btnUpdate.setPrefWidth(36);
                btnUpdate.setTextFill(Color.WHITE);
                btnUpdate.setFont(Font.font("System Bold", 15));
                btnUpdate.setOnAction(event -> handleUpdate(hebergement));

                Button btnDelete = new Button("❌");
                btnDelete.setStyle("-fx-background-color: #002966; -fx-background-radius: 60; -fx-border-radius: 60;");
                btnDelete.setPrefHeight(33);
                btnDelete.setPrefWidth(36);
                btnDelete.setTextFill(Color.WHITE);
                btnDelete.setFont(Font.font("System Bold", 14));
                btnDelete.setOnAction(event -> handleDelete(hebergement));

                HBox pane = new HBox(btnUpdate, btnDelete);
                pane.setSpacing(10);
                setGraphic(pane);
            }
        });

        // Initial loading of data
        loadData();

        // Dynamic search listener
        search_input.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHebergementList(newValue);
        });

        // Reset table when search is cleared
        cancel_search.setOnAction(e -> {
            search_input.clear();
            filterHebergementList("");  // Clear the search and reset to show all
        });

        // Adding new hebergement
        add.setOnAction(this::handleAdd);
    }

    private void loadData() {
        try {
            hebergements.setAll(serviceHebergement.recuperer());
            tabview.setItems(hebergements);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showErrorAlert("Erreur lors du chargement des données.");
        }
    }

    private void filterHebergementList(String searchText) {
        ObservableList<Hebergement> displayedHebergements;

        if (searchText == null || searchText.isEmpty()) {
            displayedHebergements = FXCollections.observableArrayList(hebergements);
        } else {
            displayedHebergements = FXCollections.observableArrayList();
            for (Hebergement hebergement : hebergements) {
                if (hebergement.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        hebergement.getAdresse().toLowerCase().contains(searchText.toLowerCase()) ||
                        String.valueOf(hebergement.getTelephone()).contains(searchText) ||
                        hebergement.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                        String.valueOf(hebergement.getCapacite()).contains(searchText) ||
                        String.valueOf(hebergement.getTarif_nuit()).contains(searchText)) {
                    displayedHebergements.add(hebergement);
                }
            }
        }

        tabview.setItems(displayedHebergements);
        tabview.refresh();
    }

    private void handleAdd(ActionEvent event) {
        // Slide in the side panel with transition
        sidePanel.setVisible(true);
        TranslateTransition slideInTransition = new TranslateTransition(Duration.seconds(0.5), sidePanel);
        slideInTransition.setToX(0);
        slideInTransition.play();
    }

    private void handleUpdate(Hebergement hebergement) {
        // Set the values in the side panel's fields based on the selected hebergement
        nomField.setText(hebergement.getNom());
        adresseField.setText(hebergement.getAdresse());
        telephoneField.setText(String.valueOf(hebergement.getTelephone()));
        emailField.setText(hebergement.getEmail());
        capaciteField.setText(String.valueOf(hebergement.getCapacite()));
        tarifNuitField.setText(String.valueOf(hebergement.getTarif_nuit()));

        // Show the side panel with a slide-in effect
        sidePanel.setVisible(true);
        TranslateTransition slideInTransition = new TranslateTransition(Duration.seconds(0.5), sidePanel);
        slideInTransition.setToX(0);
        slideInTransition.play();

        // Update button action to handle modification
        submitAddButton.setOnAction(event -> handleSubmitUpdate(hebergement));
    }

    private void handleSubmitUpdate(Hebergement hebergement) {
        try {
            // Get the new values from the input fields
            String nom = nomField.getText();
            String adresse = adresseField.getText();
            String telephone = telephoneField.getText();
            String email = emailField.getText();
            int capacite = Integer.parseInt(capaciteField.getText());
            double tarifNuit = Double.parseDouble(tarifNuitField.getText());

            // Update the hebergement object
            hebergement.setNom(nom);
            hebergement.setAdresse(adresse);
            hebergement.setTelephone(Integer.parseInt(telephone));
            hebergement.setEmail(email);
            hebergement.setCapacite(capacite);
            hebergement.setTarif_nuit((int) tarifNuit);

            // Update in the service and reload data
            serviceHebergement.modifier(hebergement);
            loadData();
            clearAddForm();
            closeSidePanel();
        } catch (NumberFormatException e) {
            showErrorAlert("Veuillez entrer des valeurs valides pour la capacité et le tarif.");
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void clearAddForm() {
        nomField.clear();
        adresseField.clear();
        telephoneField.clear();
        emailField.clear();
        capaciteField.clear();
        tarifNuitField.clear();
    }

    private void handleDelete(Hebergement hebergement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Suppression de l'hébergement");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet hébergement ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    serviceHebergement.supprimer(hebergement.getId_hebergement());
                    hebergements.remove(hebergement);
                    tabview.refresh();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showErrorAlert("Erreur lors de la suppression de l'hébergement.");
                }
            }
        });
    }
    @FXML
    public void handleImageUpload(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");

        // Add the correct extension filters with an asterisk (*) before the extensions
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            try {
                // Display the image preview
                Image image = new Image(selectedImageFile.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                // Handle errors in case the image cannot be loaded
                System.out.println("Erreur de chargement de l'image : " + e.getMessage());
            }
        }
    }


    }

