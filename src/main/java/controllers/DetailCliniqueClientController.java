package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import models.Clinique;
import models.Docteur;
import models.Photo;
import services.GeocodingService;
import services.MapService;
import services.ServiceDocteur;
import services.ServicePhoto;
import services.ServiceClinique;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class DetailCliniqueClientController {

    @FXML
    private ImageView cliniqueImageView;
    @FXML
    private Label nomLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label telLabel;
    @FXML
    private Label prixLabel;
    @FXML
    private VBox docteursContainer;

    @FXML
    private ImageView mapView;  // Ajout de la carte

    @FXML
    private ImageView imageView;

    @FXML
    private Button pdfButton;



    private ServiceDocteur serviceDocteur;
    private ServiceClinique serviceClinique;

    public DetailCliniqueClientController() {
        this.serviceDocteur = new ServiceDocteur();
        this.serviceClinique = new ServiceClinique();
    }



    public void afficherDetails(Clinique clinique, int specialiteId) throws SQLException {

        String imageUrl = clinique.getPhotoUrl() != null ? clinique.getPhotoUrl() : "clinique2.png";  // Fallback if no photo
        cliniqueImageView.setImage(new Image(imageUrl));


        nomLabel.setText(clinique.getNom());
        descriptionLabel.setText("Description: " + clinique.getDescription());
        adresseLabel.setText("Adresse: " + clinique.getAdresse());
        emailLabel.setText("Email: " + clinique.getEmail());
        telLabel.setText("Téléphone: " + clinique.getTelephone());
        prixLabel.setText("Prix: " + clinique.getPrix() + " €");

        // Récupération des coordonnées de la clinique
        //double[] coords = GeocodingService.getCoordinatesByNameAndAddress(clinique.getAdresse());
        double[] coords = GeocodingService.getCoordinates(clinique.getAdresse());
        if (coords != null) {
            System.out.println("Latitude: " + coords[0] + ", Longitude: " + coords[1]);
            MapService.afficherCarte(mapView, coords[0], coords[1]);
        } else {
            System.out.println("Adresse non trouvée !");
        }



        afficherDocteurs(clinique.getIdClinique(), specialiteId);
    }


    private void afficherDocteurs(int cliniqueId, int specialiteId) throws SQLException {
        List<Docteur> docteurs = serviceDocteur.recupererDocteursParCliniqueEtSpecialite(cliniqueId, specialiteId);

        docteursContainer.getChildren().clear();

        for (Docteur docteur : docteurs) {
            VBox docteurCard = createDocteurCard(docteur);
            docteursContainer.getChildren().add(docteurCard);
        }
    }

    private VBox createDocteurCard(Docteur docteur) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label nomLabel = new Label("Nom:  " + docteur.getNom());
        nomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        Label prenomLabel = new Label("Prénom:  " + docteur.getPrenom());
        prenomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        Label emailLabel = new Label("Email:  " + docteur.getEmail());
        emailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        Label telLabel = new Label("Téléphone:  " + docteur.getTelephone());
        telLabel.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;  -fx-text-fill: #555555;");

        card.getChildren().addAll(nomLabel, prenomLabel, emailLabel, telLabel);
        return card;
    }

    @FXML
    private void telechargerPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        fileChooser.setInitialFileName("detail_clinique.pdf");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                // Création du document PDF
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Ajout du titre
                Font fontTitre = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
                Paragraph titre = new Paragraph(nomLabel.getText(), fontTitre);
                titre.setAlignment(Element.ALIGN_CENTER);
                document.add(titre);

                document.add(new Paragraph("\n"));

                // Ajout de l'image de la clinique
                if (cliniqueImageView.getImage() != null) {
                    Image img = cliniqueImageView.getImage();
                    addImageToPDF(img, document);
                }

                // Ajout des détails de la clinique
                Font fontDetails = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.DARK_GRAY);
                document.add(new Paragraph("Description : " + descriptionLabel.getText(), fontDetails));
                document.add(new Paragraph("Adresse : " + adresseLabel.getText(), fontDetails));
                document.add(new Paragraph("Email : " + emailLabel.getText(), fontDetails));
                document.add(new Paragraph("Téléphone : " + telLabel.getText(), fontDetails));
                document.add(new Paragraph("\n"));

                // Ajout de la carte (si disponible)
                if (mapView.getImage() != null) {
                    Image mapImg = mapView.getImage();
                    addImageToPDF(mapImg, document);
                }

                // Ajout du prix
                Font fontPrix = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.GREEN);
                document.add(new Paragraph("Prix : " + prixLabel.getText(), fontPrix));

                // Récupérer les docteurs affichés dans l'interface
                List<Docteur> docteurs = getDocteursFromUI(); // Récupérer les docteurs à partir de l'UI

                // Ajouter les docteurs au PDF
                addDocteursToPDF(document, docteurs);

                document.close();
                System.out.println("PDF généré avec succès !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }}
    @FXML
    public void initialize() {
        pdfButton.setOnAction(event -> telechargerPDF());
    }
    private void addImageToPDF(Image image, Document document) throws IOException, DocumentException {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Lire les pixels de l'image
        PixelReader pixelReader = image.getPixelReader();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
            }
        }

        // Sauvegarder l'image temporairement en PNG
        File tempFile = File.createTempFile("temp-image", ".png");
        ImageIO.write(bufferedImage, "png", tempFile);

        // Ajouter l'image au PDF
        com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(tempFile.getAbsolutePath());
        pdfImage.scaleToFit(400, 300); // Ajuster la taille
        pdfImage.setAlignment(Element.ALIGN_CENTER);
        document.add(pdfImage);
    }

    private void addDocteursToPDF(Document document, List<Docteur> docteurs) throws SQLException, DocumentException {
        // Ajouter un titre pour la section des docteurs
        Font fontTitre = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titre = new Paragraph("\nDocteurs de la clinique :", fontTitre);
        titre.setAlignment(Element.ALIGN_LEFT);
        document.add(titre);

        // Ajouter chaque docteur dans le PDF
        Font fontTexte = new Font(Font.FontFamily.HELVETICA, 12);
        for (Docteur docteur : docteurs) {
            document.add(new Paragraph("Nom: " + docteur.getNom(), fontTexte));
            document.add(new Paragraph("Prénom: " + docteur.getPrenom(), fontTexte));
            document.add(new Paragraph("Email: " + docteur.getEmail(), fontTexte));
            document.add(new Paragraph("Téléphone: " + docteur.getTelephone(), fontTexte));
            document.add(new Paragraph("\n"));  // Ajouter un espace entre chaque docteur
        }
    }
    private List<Docteur> getDocteursFromUI() {
        List<Docteur> docteurs = new ArrayList<>();

        // Parcourir les cartes de docteurs dans le conteneur
        for (Node node : docteursContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox docteurCard = (VBox) node;

                // Extraire les informations des Labels dans la carte de docteur
                String nom = ((Label) docteurCard.getChildren().get(0)).getText().split(":")[1].trim();
                String prenom = ((Label) docteurCard.getChildren().get(1)).getText().split(":")[1].trim();
                String email = ((Label) docteurCard.getChildren().get(2)).getText().split(":")[1].trim();
                String telephone = ((Label) docteurCard.getChildren().get(3)).getText().split(":")[1].trim();

                // Créer un docteur et l'ajouter à la liste
                Docteur docteur = new Docteur(nom, prenom, email, telephone);
                docteurs.add(docteur);
            }
        }

        return docteurs;
    }
}
