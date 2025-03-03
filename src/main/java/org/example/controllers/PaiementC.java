package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.entities.Paiement;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.service.PaiementService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaiementC {

    @FXML
    private Label linfo1;

    @FXML
    private Label linfo2;

    @FXML
    private Label ltype;

    @FXML
    private Pane pn_res;

    Paiement p = new Paiement();
    public void setData(Paiement paiement, String type) {
        p=paiement;
        ltype.setText(" Date " + type +paiement.getDatePaiement());
        linfo2.setText("Montant : "+paiement.getMontant()+" dt");
        linfo1.setText("Payé par : "+paiement.getMethode());
    }

    @FXML
    void print(ActionEvent event) {
        try {
            genererPDF(p, HomePage.stg.getScene().getWindow());
        } catch (DocumentException ex) {
            Logger.getLogger(PaiementC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PaiementC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void genererPDF(Paiement p, Window parentWindow) throws DocumentException, FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer les données");
        File selectedFile = fileChooser.showSaveDialog(parentWindow);

        if (selectedFile != null) {
            try {
                // Créer un document PDF
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                // Ajouter les éléments de l'interface utilisateur pour le ticket d'achat
                com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Paragraph title = new Paragraph("Recu du paiment", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(10f);
                document.add(title);

                com.itextpdf.text.Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                Paragraph date = new Paragraph("Date: " + LocalDate.now().toString(), regularFont);
                date.setAlignment(Element.ALIGN_LEFT);
                date.setSpacingAfter(10f);
                document.add(date);

                Paragraph produits = new Paragraph("Objet : Confirmation de votre achat chez MedTravel\n" +
                        "\n" +
                        "Cher(e) client,\n" +
                        "\n" +
                        "Nous vous remercions chaleureusement pour votre commande chez Autodoc ! Nous sommes ravis de vous compter parmi nos clients et nous vous confirmons que votre achat a bien été pris en compte.\n" +
                        "\n" +
                        "Détails de la commande :\n" +
                        "Date de commande : " +  p.getDatePaiement() +
                        "\n Montant total : " + String.valueOf(p.getMontant()) +
                        "\n Methode de paiment : " + String.valueOf(p.getMethode()) +
                        "\n" +
                        "Nous vous remercions de faire confiance à Medtravel pour vos besoins en service medical. Nous espérons que vous serez pleinement satisfait de vos produits et que vous reviendrez bientôt pour vos futurs achats.\n" +
                        "\n" +
                        "Si vous avez la moindre question ou si vous avez besoin d’assistance, n’hésitez pas à contacter notre service client à l’adresse suivante : serviceclient@medtravel.tn .\n" +
                        "\n" +
                        "Merci encore et à très bientôt !\n" +
                        "\n" +
                        "Cordialement,\n" +
                        "L'équipe MedTravel", regularFont);
                produits.setAlignment(Element.ALIGN_LEFT);
                produits.setSpacingAfter(10f);
                document.add(produits);


                PdfPTable table = new PdfPTable(3); // 3 colonnes pour Nom, Prix et Quantité
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // En-tête de table
                table.addCell(new PdfPCell(new Phrase("Date du Paiment", regularFont)));
                table.addCell(new PdfPCell(new Phrase("Montant", regularFont)));
                table.addCell(new PdfPCell(new Phrase("Type transaction", regularFont)));

                // Contenu de table

                System.out.println(p);
                table.addCell(new PdfPCell(new Phrase(String.valueOf(p.getDatePaiement()), regularFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(p.getMontant()), regularFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(p.getMethode()), regularFont)));


                document.add(table);

                document.close();
            } catch (IOException | DocumentException ex) {
                System.err.println("Erreur lors de l'écriture dans le fichier: " + ex.getMessage());
            }
        } else {
            System.out.println("La sélection de fichier a été annulée");
        }
    }
}
