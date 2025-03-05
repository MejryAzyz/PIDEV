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

public class GesHeb implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private TableView<Hebergement> tabview;

    @FXML
    private TableColumn<Hebergement, Integer> colId;

    @FXML
    private TableColumn<Hebergement, String> colNom;

    @FXML
    private TableColumn<Hebergement, String> colAdresse;

    @FXML
    private TableColumn<Hebergement, Integer> colTelephone;

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
    private Button downloadPdfButton;

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

    @FXML
    private Button uploadImageButton;

    // Statistics Labels
    @FXML
    private Label totalHebergementsLabel;

    @FXML
    private Label avgTarifLabel;

    @FXML
    private Label totalCapacityLabel;

    private ObservableList<Hebergement> hebergements = FXCollections.observableArrayList();
    private ServiceHebergement serviceHebergement = new ServiceHebergement();
    private File selectedImageFile;

    @FXML
    void addHebergement(ActionEvent event) {
        try {
            // Input validation
            if (nomField.getText().isEmpty() || adresseField.getText().isEmpty() || telephoneField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || capaciteField.getText().isEmpty() || tarifNuitField.getText().isEmpty()) {
                showErrorAlert("Tous les champs sont obligatoires !");
                return;
            }

            // Validate phone number (digits only, 8-15 characters)
            if (!telephoneField.getText().matches("\\d{8,15}")) {
                showErrorAlert("Le numéro de téléphone doit être composé de 8 à 15 chiffres.");
                return;
            }

            // Validate email format
            if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showErrorAlert("Veuillez entrer un email valide.");
                return;
            }

            // Validate capacite and tarifNuit
            int capacite;
            double tarifNuit;
            try {
                capacite = Integer.parseInt(capaciteField.getText());
                tarifNuit = Double.parseDouble(tarifNuitField.getText());
                if (capacite <= 0 || tarifNuit <= 0) {
                    showErrorAlert("La capacité et le tarif doivent être positifs.");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorAlert("La capacité et le tarif doivent être des nombres.");
                return;
            }

            // Handle image upload
            String imageUrl = "";
            if (selectedImageFile != null) {
                String[] uploadResult = ImageDbUtil.uploadFile(selectedImageFile.getAbsolutePath());
                imageUrl = uploadResult[1];
            }

            // Create and add new Hebergement
            Hebergement hebergement = new Hebergement(nomField.getText(), adresseField.getText(),
                    Integer.parseInt(telephoneField.getText()), emailField.getText(), capacite, tarifNuit, imageUrl);
            serviceHebergement.ajouter(hebergement);

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Hébergement ajouté avec succès !");
            alert.showAndWait();

            // Refresh data and reset form
            loadData();
            clearAddForm();
            closeSidePanel(null);

        } catch (Exception e) {
            showErrorAlert("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void handleImageUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            try {
                Image image = new Image(selectedImageFile.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                showErrorAlert("Erreur de chargement de l'image : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handlePdfDownload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                PdfWriter writer = new PdfWriter(file);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Liste des Hébergements"));
                Table table = new Table(7);
                table.addCell(new Cell().add(new Paragraph("ID")));
                table.addCell(new Cell().add(new Paragraph("Nom")));
                table.addCell(new Cell().add(new Paragraph("Adresse")));
                table.addCell(new Cell().add(new Paragraph("Téléphone")));
                table.addCell(new Cell().add(new Paragraph("Email")));
                table.addCell(new Cell().add(new Paragraph("Capacité")));
                table.addCell(new Cell().add(new Paragraph("Tarif Nuit")));

                for (Hebergement h : hebergements) {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(h.getId_hebergement()))));
                    table.addCell(new Cell().add(new Paragraph(h.getNom())));
                    table.addCell(new Cell().add(new Paragraph(h.getAdresse())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(h.getTelephone()))));
                    table.addCell(new Cell().add(new Paragraph(h.getEmail())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(h.getCapacite()))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(h.getTarif_nuit()))));
                }

                document.add(table);
                document.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le PDF a été téléchargé avec succès !");
                alert.showAndWait();

            } catch (Exception e) {
                showErrorAlert("Erreur lors de la génération du PDF : " + e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sidePanel.setVisible(false);

        // Set up table columns
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

        // Load initial data
        loadData();

        // Search listener
        search_input.textProperty().addListener((observable, oldValue, newValue) -> filterHebergementList(newValue));

        // Reset search
        cancel_search.setOnAction(e -> {
            search_input.clear();
            filterHebergementList("");
        });

        // Add button action
        add.setOnAction(this::handleAdd);
    }

    private void loadData() {
        try {
            hebergements.setAll(serviceHebergement.recuperer());
            tabview.setItems(hebergements);

            // Update statistics
            int totalHebergements = serviceHebergement.getNombreTotalHebergements();
            double avgTarif = serviceHebergement.getTarifMoyenParNuit();
            int totalCapacity = serviceHebergement.getCapaciteTotale();

            totalHebergementsLabel.setText("Nombre d'Hébergements: " + totalHebergements);
            avgTarifLabel.setText("Tarif Moyen Nuit: " + String.format("%.2f", avgTarif));
            totalCapacityLabel.setText("Capacité Totale: " + totalCapacity);

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
            for (Hebergement h : hebergements) {
                if (h.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        h.getAdresse().toLowerCase().contains(searchText.toLowerCase()) ||
                        String.valueOf(h.getTelephone()).contains(searchText) ||
                        h.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                        String.valueOf(h.getCapacite()).contains(searchText) ||
                        String.valueOf(h.getTarif_nuit()).contains(searchText)) {
                    displayedHebergements.add(h);
                }
            }
        }
        tabview.setItems(displayedHebergements);
        tabview.refresh();
    }

    private void handleAdd(ActionEvent event) {
        sidePanel.setVisible(true);
        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), sidePanel);
        slideIn.setToX(0);
        slideIn.play();
        submitAddButton.setOnAction(this::addHebergement);
    }

    private void handleUpdate(Hebergement hebergement) {
        nomField.setText(hebergement.getNom());
        adresseField.setText(hebergement.getAdresse());
        telephoneField.setText(String.valueOf(hebergement.getTelephone()));
        emailField.setText(hebergement.getEmail());
        capaciteField.setText(String.valueOf(hebergement.getCapacite()));
        tarifNuitField.setText(String.valueOf(hebergement.getTarif_nuit()));
        if (hebergement.getPhotoUrl() != null && !hebergement.getPhotoUrl().isEmpty()) {
            imageView.setImage(new Image(hebergement.getPhotoUrl()));
        } else {
            imageView.setImage(null);
        }

        sidePanel.setVisible(true);
        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), sidePanel);
        slideIn.setToX(0);
        slideIn.play();

        submitAddButton.setOnAction(event -> handleSubmitUpdate(hebergement));
    }

    private void handleSubmitUpdate(Hebergement hebergement) {
        try {
            // Input validation
            if (nomField.getText().isEmpty() || adresseField.getText().isEmpty() || telephoneField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || capaciteField.getText().isEmpty() || tarifNuitField.getText().isEmpty()) {
                showErrorAlert("Tous les champs sont obligatoires !");
                return;
            }

            // Validate phone number
            if (!telephoneField.getText().matches("\\d{8,15}")) {
                showErrorAlert("Le numéro de téléphone doit être composé de 8 à 15 chiffres.");
                return;
            }

            // Validate email
            if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showErrorAlert("Veuillez entrer un email valide.");
                return;
            }

            // Validate capacite and tarifNuit
            int capacite;
            double tarifNuit;
            try {
                capacite = Integer.parseInt(capaciteField.getText());
                tarifNuit = Double.parseDouble(tarifNuitField.getText());
                if (capacite <= 0 || tarifNuit <= 0) {
                    showErrorAlert("La capacité et le tarif doivent être positifs.");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorAlert("La capacité et le tarif doivent être des nombres.");
                return;
            }

            // Handle image upload
            String imageUrl = hebergement.getPhotoUrl();
            if (selectedImageFile != null) {
                String[] uploadResult = ImageDbUtil.uploadFile(selectedImageFile.getAbsolutePath());
                imageUrl = uploadResult[1];
            }

            // Update hebergement
            hebergement.setNom(nomField.getText());
            hebergement.setAdresse(adresseField.getText());
            hebergement.setTelephone(Integer.parseInt(telephoneField.getText()));
            hebergement.setEmail(emailField.getText());
            hebergement.setCapacite(capacite);
            hebergement.setTarif_nuit(tarifNuit);
            hebergement.setPhotoUrl(imageUrl);

            serviceHebergement.modifier(hebergement);
            loadData();
            clearAddForm();
            closeSidePanel(null);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Hébergement mis à jour avec succès !");
            alert.showAndWait();

        } catch (Exception e) {
            showErrorAlert("Erreur lors de la mise à jour : " + e.getMessage());
        }
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
                    loadData();
                } catch (SQLException ex) {
                    showErrorAlert("Erreur lors de la suppression : " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    private void closeSidePanel(ActionEvent event) {
        TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), sidePanel);
        slideOut.setToX(sidePanel.getWidth());
        slideOut.setOnFinished(e -> sidePanel.setVisible(false));
        slideOut.play();
    }

    private void clearAddForm() {
        nomField.clear();
        adresseField.clear();
        telephoneField.clear();
        emailField.clear();
        capaciteField.clear();
        tarifNuitField.clear();
        imageView.setImage(null);
        selectedImageFile = null;
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
}