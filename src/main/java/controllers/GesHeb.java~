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
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import javafx.stage.FileChooser;

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
            // Input validation for fields
            if (nomField.getText().isEmpty() || adresseField.getText().isEmpty() || telephoneField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || capaciteField.getText().isEmpty() || tarifNuitField.getText().isEmpty()) {
                showErrorAlert("Tous les champs sont obligatoires !");
                return;
            }

            // Validate telephone field (should be a valid phone number)
            // Validate the phone number (only digits, length between 8 and 15)
            if (telephoneField.getText().isEmpty() || !telephoneField.getText().matches("\\d{8,15}")) {
                showErrorAlert("Le numéro de téléphone doit être composé uniquement de chiffres et avoir entre 8 et 15 caractères.");
                return;
            }


            // Validate email format
            if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showErrorAlert("Veuillez entrer un email valide.");
                return;
            }

            // Validate capacite and tarifNuit (should be positive numbers)
            int capacite;
            double tarifNuit;
            try {
                capacite = Integer.parseInt(capaciteField.getText());
                tarifNuit = Double.parseDouble(tarifNuitField.getText());

                if (capacite <= 0 || tarifNuit <= 0) {
                    showErrorAlert("La capacité et le tarif doivent être des nombres positifs.");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorAlert("La capacité et le tarif doivent être des nombres.");
                return;
            }

            // Upload image
            String[] uploadResult = ImageDbUtil.uploadFile(selectedImageFile.getAbsolutePath());
            String imageUrl = uploadResult[1];

            // Create and add a new Hebergement object using the input data
            Hebergement hebergement = new Hebergement(
                    nomField.getText(), adresseField.getText(), Integer.parseInt(telephoneField.getText()),
                    emailField.getText(), capacite, tarifNuit, imageUrl
            );

            serviceHebergement.ajouter(hebergement);

            tabview.refresh();

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Hébergement ajouté avec succès !");
            alert.showAndWait();

            // Reload data
            loadData();

            // Clear form after submission
            clearAddForm();
            closeSidePanel();

        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

// Other methods remain the same...


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
        sidePanel.setVisible(false);
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

        // Load the image for the selected hebergement (if any)
        if (hebergement.getPhotoUrl() != null && !hebergement.getPhotoUrl().isEmpty()) {
            try {
                Image image = new Image(hebergement.getPhotoUrl());  // Assuming getImageUrl() returns a valid image URL
                imageView.setImage(image);
            } catch (Exception e) {
                // Handle errors if the image cannot be loaded
                System.out.println("Erreur de chargement de l'image : " + e.getMessage());
            }
        }

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
            // Input validation for fields
            if (nomField.getText().isEmpty() || adresseField.getText().isEmpty() || telephoneField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || capaciteField.getText().isEmpty() || tarifNuitField.getText().isEmpty()) {
                showErrorAlert("Tous les champs sont obligatoires !");
                return;
            }

            // Validate telephone field (should be a valid phone number)
            // Validate the phone number (only digits, length between 8 and 15)
            if (telephoneField.getText().isEmpty() || !telephoneField.getText().matches("\\d{8,15}")) {
                showErrorAlert("Le numéro de téléphone doit être composé uniquement de chiffres et avoir entre 8 et 15 caractères.");
                return;
            }


            // Validate email format
            if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showErrorAlert("Veuillez entrer un email valide.");
                return;
            }

            // Validate capacite and tarifNuit (should be positive numbers)
            int capacite;
            double tarifNuit;
            try {
                capacite = Integer.parseInt(capaciteField.getText());
                tarifNuit = Double.parseDouble(tarifNuitField.getText());

                if (capacite <= 0 || tarifNuit <= 0) {
                    showErrorAlert("La capacité et le tarif doivent être des nombres positifs.");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorAlert("La capacité et le tarif doivent être des nombres.");
                return;
            }

            // If a new image was uploaded, use the new image URL, otherwise retain the existing one
            String imageUrl;
            if (selectedImageFile != null) {
                // Upload the new image and get the URL
                String[] uploadResult = ImageDbUtil.uploadFile(selectedImageFile.getAbsolutePath());
                imageUrl = uploadResult[1];
            } else {
                // If no new image is uploaded, keep the existing image URL
                imageUrl = hebergement.getPhotoUrl();
            }

            // Update the hebergement object with new values
            hebergement.setNom(nomField.getText());
            hebergement.setAdresse(adresseField.getText());
            hebergement.setTelephone(Integer.parseInt(telephoneField.getText()));
            hebergement.setEmail(emailField.getText());
            hebergement.setCapacite(capacite);
            hebergement.setTarif_nuit(tarifNuit);
            hebergement.setPhotoUrl(imageUrl);  // Update the image URL

            // Update in the service and reload data
            serviceHebergement.modifier(hebergement);

            // Reload data to reflect changes
            loadData();

            // Clear the form and close the side panel
            clearAddForm();
            closeSidePanel();

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Hébergement mis à jour avec succès !");
            alert.showAndWait();

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
    private void handlePdfDownload(ActionEvent event) {
        // Use a file chooser to select the location where the PDF will be saved
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                // Create a PdfWriter instance to write to the file
                PdfWriter writer = new PdfWriter(file);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // Add title
                document.add(new Paragraph("Liste des Hébergements"));

                // Create a table with the same columns as the table in your UI
                Table table = new Table(7);  // Adjust the number of columns as needed
                table.addCell(new Cell().add(new Paragraph("ID")));
                table.addCell(new Cell().add(new Paragraph("Nom")));
                table.addCell(new Cell().add(new Paragraph("Adresse")));
                table.addCell(new Cell().add(new Paragraph("Téléphone")));
                table.addCell(new Cell().add(new Paragraph("Email")));
                table.addCell(new Cell().add(new Paragraph("Capacité")));
                table.addCell(new Cell().add(new Paragraph("Tarif Nuit")));

                // Add data for each hebergement
                for (Hebergement hebergement : hebergements) {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(hebergement.getId_hebergement()))));
                    table.addCell(new Cell().add(new Paragraph(hebergement.getNom())));
                    table.addCell(new Cell().add(new Paragraph(hebergement.getAdresse())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(hebergement.getTelephone()))));
                    table.addCell(new Cell().add(new Paragraph(hebergement.getEmail())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(hebergement.getCapacite()))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(hebergement.getTarif_nuit()))));
                }

                // Add the table to the document
                document.add(table);
                document.close();

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le PDF a été téléchargé avec succès !");
                alert.showAndWait();

            } catch (Exception e) {
                // Handle error
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Erreur lors de la génération du PDF : " + e.getMessage());
                alert.showAndWait();
            }
        }
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

