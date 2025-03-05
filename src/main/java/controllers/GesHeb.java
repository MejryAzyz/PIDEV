package controllers;

import API.ImageDbUtil;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
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
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;


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
            if (nomField.getText().isEmpty() || adresseField.getText().isEmpty() || telephoneField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || capaciteField.getText().isEmpty() || tarifNuitField.getText().isEmpty()) {
                showErrorAlert("Tous les champs sont obligatoires !");
                return;
            }

            if (!telephoneField.getText().matches("\\d{8,15}")) {
                showErrorAlert("Le numéro de téléphone doit être composé de 8 à 15 chiffres.");
                return;
            }

            if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showErrorAlert("Veuillez entrer un email valide.");
                return;
            }

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

            String imageUrl = "";
            if (selectedImageFile != null) {
                String[] uploadResult = ImageDbUtil.uploadFile(selectedImageFile.getAbsolutePath());
                imageUrl = uploadResult[1];
            }

            Hebergement hebergement = new Hebergement(nomField.getText(), adresseField.getText(),
                    Integer.parseInt(telephoneField.getText()), emailField.getText(), capacite, tarifNuit, imageUrl);
            serviceHebergement.ajouter(hebergement);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Hébergement ajouté avec succès !");
            alert.showAndWait();

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
                // Initialize PDF document
                PdfWriter writer = new PdfWriter(file);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // Add header with logo and date
                Table headerTable = new Table(UnitValue.createPercentArray(new float[]{20, 60, 20}));
                headerTable.setWidth(UnitValue.createPercentValue(100));

                // Logo (assuming you have a logo file in your resources)
                try {
                    Image logo = new Image(getClass().getResource("/logo.png").toString());
                    com.itextpdf.layout.element.Image pdfLogo =
                            new com.itextpdf.layout.element.Image(ImageDataFactory.create(logo.getUrl()))
                                    .setWidth(150)
                                    .setHeight(150);
                    headerTable.addCell(new Cell()
                            .add(pdfLogo)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE));
                } catch (Exception e) {
                    headerTable.addCell(new Cell()
                            .add(new Paragraph("Logo")));

                }

                // Title
                headerTable.addCell(new Cell()
                        .add(new Paragraph("Liste des Hébergements")
                                .setFontSize(20)
                                .setBold()
                                .setFontColor(new DeviceRgb(0, 41, 102)))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE));

                // Date
                headerTable.addCell(new Cell()
                        .add(new Paragraph("Date: " + LocalDate.now().format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                .setFontSize(10))
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE));

                document.add(headerTable);
                document.add(new Paragraph(" ").setMarginBottom(10));

                // Statistics Section
                Table statsTable = new Table(UnitValue.createPercentArray(new float[]{33, 33, 33}));
                statsTable.setWidth(UnitValue.createPercentValue(100));
                statsTable.setMarginBottom(20);

                statsTable.addCell(createStatCell("Total Hébergements",
                        String.valueOf(serviceHebergement.getNombreTotalHebergements())));
                statsTable.addCell(createStatCell("Tarif Moyen/Nuit",
                        String.format("%.2f", serviceHebergement.getTarifMoyenParNuit())));
                statsTable.addCell(createStatCell("Capacité Totale",
                        String.valueOf(serviceHebergement.getCapaciteTotale())));

                document.add(statsTable);

                // Main Table
                float[] columnWidths = {40, 90, 90, 70, 90, 60, 70};
                Table table = new Table(columnWidths);
                table.setWidth(UnitValue.createPercentValue(100));
                table.setBorder(new SolidBorder(new DeviceRgb(0, 41, 102), 1));

                // Add table headers with modern styling
                String[] headers = {"ID", "Nom", "Adresse", "Téléphone", "Email", "Capacité", "Tarif/Nuit"};
                PdfFont headerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                for (String header : headers) {
                    Cell headerCell = new Cell()
                            .add(new Paragraph(header)
                                    .setFont(headerFont)
                                    .setFontSize(11))
                            .setBackgroundColor(new DeviceRgb(0, 41, 102))
                            .setFontColor(DeviceRgb.WHITE)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPadding(8)
                            .setBorder(new SolidBorder(new DeviceRgb(255, 255, 255), 0.5f));
                    table.addHeaderCell(headerCell);
                }

                // Add data rows with modern styling
                PdfFont bodyFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                boolean alternate = true;
                for (Hebergement hebergement : hebergements) {
                    table.addCell(createModernCell(String.valueOf(hebergement.getId_hebergement()), alternate, bodyFont));
                    table.addCell(createModernCell(hebergement.getNom(), alternate, bodyFont));
                    table.addCell(createModernCell(hebergement.getAdresse(), alternate, bodyFont));
                    table.addCell(createModernCell(String.valueOf(hebergement.getTelephone()), alternate, bodyFont));
                    table.addCell(createModernCell(hebergement.getEmail(), alternate, bodyFont));
                    table.addCell(createModernCell(String.valueOf(hebergement.getCapacite()), alternate, bodyFont));
                    table.addCell(createModernCell(String.format("%.2f TND", hebergement.getTarif_nuit()), alternate, bodyFont));
                    alternate = !alternate;
                }

                document.add(table);

                // Add footer with page number and additional info
                pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new IEventHandler() {
                    @Override
                    public void handleEvent(Event event) {
                        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                        PdfPage page = docEvent.getPage();
                        PdfCanvas canvas = new PdfCanvas(page);
                        int pageNumber = pdf.getPageNumber(page);
                        Rectangle pageSize = page.getPageSize();

                        try {
                            PdfFont footerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                            canvas.beginText()
                                    .setFontAndSize(footerFont, 9)
                                    .setColor(DeviceRgb.BLACK, true);

                            // Left footer
                            canvas.moveText(36, 20)
                                    .showText("Généré par: Elyes Msehli")
                                    .endText();

                            // Center page number
                            canvas.beginText()
                                    .moveText(pageSize.getWidth() / 2 - 20, 20)
                                    .showText("Page " + pageNumber + " / " + pdf.getNumberOfPages())
                                    .endText();

                            // Right footer
                            canvas.beginText()
                                    .moveText(pageSize.getWidth() - 150, 20)
                                    .showText("Contact: support@medtravel.com")
                                    .endText();

                            canvas.stroke();
                            canvas.release();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                document.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le PDF a été généré avec succès !");
                alert.showAndWait();

            } catch (Exception e) {
                showErrorAlert("Erreur lors de la génération du PDF : " + e.getMessage());
            }
        }
    }

    // Helper method for statistic cells
    private Cell createStatCell(String title, String value) {
        return new Cell()
                .add(new Paragraph(title)
                        .setFontSize(10)
                        .setFontColor(DeviceRgb.BLACK))
                .add(new Paragraph(value)
                        .setFontSize(14)
                        .setBold()
                        .setFontColor(new DeviceRgb(0, 41, 102)))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5)
                .setBorder(new SolidBorder(new DeviceRgb(230, 230, 230), 1));
    }

    // Helper method for modern table cells
    private Cell createModernCell(String content, boolean alternate, PdfFont font) {
        Cell cell = new Cell()
                .add(new Paragraph(content)
                        .setFont(font)
                        .setFontSize(10))
                .setPadding(6)
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(new SolidBorder(new DeviceRgb(230, 230, 230), 0.5f));

        if (alternate) {
            cell.setBackgroundColor(new DeviceRgb(245, 248, 255));
        } else {
            cell.setBackgroundColor(DeviceRgb.WHITE);
        }
        return cell;
    }
    // Helper method to create styled table cells
    private Cell createCell(String content, boolean alternate) {
        Cell cell = new Cell()
                .add(new Paragraph(content))
                .setPadding(5)
                .setTextAlignment(TextAlignment.CENTER);
        if (alternate) {
            cell.setBackgroundColor(new DeviceRgb(235, 245, 255));
        }
        return cell;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        loadData();

        search_input.textProperty().addListener((observable, oldValue, newValue) -> filterHebergementList(newValue));

        cancel_search.setOnAction(e -> {
            search_input.clear();
            filterHebergementList("");
        });

        add.setOnAction(this::handleAdd);
    }

    private void loadData() {
        try {
            hebergements.setAll(serviceHebergement.recuperer());
            tabview.setItems(hebergements);

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
            if (nomField.getText().isEmpty() || adresseField.getText().isEmpty() || telephoneField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || capaciteField.getText().isEmpty() || tarifNuitField.getText().isEmpty()) {
                showErrorAlert("Tous les champs sont obligatoires !");
                return;
            }

            if (!telephoneField.getText().matches("\\d{8,15}")) {
                showErrorAlert("Le numéro de téléphone doit être composé de 8 à 15 chiffres.");
                return;
            }

            if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showErrorAlert("Veuillez entrer un email valide.");
                return;
            }

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

            String imageUrl = hebergement.getPhotoUrl();
            if (selectedImageFile != null) {
                String[] uploadResult = ImageDbUtil.uploadFile(selectedImageFile.getAbsolutePath());
                imageUrl = uploadResult[1];
            }

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