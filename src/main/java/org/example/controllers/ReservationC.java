package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.entities.MailSender;
import org.example.entities.Paiement;
import org.example.entities.Reservation;
import org.example.service.PaiementService;
import org.example.service.ReservationService;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class ReservationC {

    @FXML
    private ImageView image;

    @FXML
    private Label amount;

    @FXML
    private Hyperlink btnpayment;

    @FXML
    private Hyperlink btnupdate;

    @FXML
    private Hyperlink btncancel;

    @FXML
    private DatePicker dateinfo1;

    @FXML
    private DatePicker dateinfo2;

    @FXML
    private RadioButton espece;

    @FXML
    private Label linfo1;

    @FXML
    private Label linfo2;

    @FXML
    private Label ltype;

    @FXML
    private RadioButton paypal;

    @FXML
    private Pane pn_paie;

    @FXML
    private Pane pn_res;

    @FXML
    private TextField tfinfo2;

    @FXML
    private RadioButton virement;

    String text= "";

    ReservationService rs = new ReservationService();
    PaiementService ps = new PaiementService();
    Reservation res = new Reservation();
    Paiement p = new Paiement();
    int id ;

    @FXML
    void delete(ActionEvent event) {
        res.setIdReservation(id);
        rs.supprimerEntite(res);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Valider");
        alert.setHeaderText(null);
        alert.setContentText("Reservation deleted succesfully !");
        alert.showAndWait();
    }


    @FXML
    void submit(ActionEvent event) {
        if(Objects.equals(btnupdate.getText(), "Update"))
        {
            if(res.getIdClinique()!=0)
            {
                linfo1.setVisible(false);
                linfo2.setVisible(false);
            } else if (res.getIdTransport()!=0) {
                linfo1.setVisible(false);
                linfo2.setVisible(false);
                tfinfo2.setVisible(true);
                tfinfo2.setEditable(true);
                dateinfo1.setVisible(true);
                tfinfo2.setText(res.getHeureDepart());
                LocalDate localDate = res.getDateDepart().toLocalDate();
                dateinfo1.setValue(localDate);
                btnupdate.setText("Submit");
            } else if (res.getIdHebergement()!=0) {
                linfo1.setVisible(false);
                linfo2.setVisible(false);
                dateinfo1.setVisible(true);
                dateinfo2.setVisible(true);
                LocalDate localDate = res.getDateDebut().toLocalDate();
                dateinfo1.setValue(localDate);
                localDate = res.getDateFin().toLocalDate();
                dateinfo2.setValue(localDate);
                btnupdate.setText("Submit");
            }
        }else{
             if (res.getIdTransport()!=0) {
                 LocalDate currentDate = LocalDate.now();
                 if (tfinfo2.getText().isEmpty() || dateinfo1.getValue()==null || dateinfo1.getValue().isBefore(currentDate)||    !Pattern.matches("([01]?[0-9]|2[0-3]):([0-5]?[0-9])", tfinfo2.getText())) {
                     // Afficher un message d'alerte
                     Alert alert = new Alert(Alert.AlertType.WARNING);
                     alert.setTitle("Champs manquants");
                     alert.setHeaderText(null);
                     alert.setContentText("Veuillez remplir tous les champs et entrer une date valide !");
                     alert.showAndWait();
                     return;
                 }
                 LocalDate localDate = dateinfo1.getValue();
                 Date date = java.sql.Date.valueOf(localDate);
                 res.setDateDepart(date);
                 res.setHeureDepart(tfinfo2.getText());
                 rs.modifierEntite(res);
                 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                 alert.setTitle("Valider");
                 alert.setHeaderText(null);
                 alert.setContentText("Reservation updated succesfully !");
                 alert.showAndWait();
                 tfinfo2.setVisible(false);
                 dateinfo1.setVisible(false);
                 linfo1.setText("Date : "+res.getDateDepart());
                 linfo2.setText("Heure : "+res.getHeureDepart());
                 linfo1.setVisible(true);
                 linfo2.setVisible(true);
                 btnupdate.setText("Update");
            } else if (res.getIdHebergement()!=0) {
                 LocalDate currentDate = LocalDate.now();
                 if (dateinfo1.getValue()==null ||dateinfo2.getValue()==null ||dateinfo1.getValue().isBefore(currentDate)||dateinfo2.getValue().isBefore(currentDate)) {
                     // Afficher un message d'alerte
                     Alert alert = new Alert(Alert.AlertType.WARNING);
                     alert.setTitle("Champs manquants");
                     alert.setHeaderText(null);
                     alert.setContentText("Veuillez remplir tous les champs et entrer une date valide !");
                     alert.showAndWait();
                     return;
                 }
                 LocalDate localDate = dateinfo1.getValue();
                 Date date = java.sql.Date.valueOf(localDate);
                 res.setDateDebut(date);
                 LocalDate localDate2 = dateinfo2.getValue();
                 Date date2 = java.sql.Date.valueOf(localDate2);
                 res.setDateFin(date2);
                 rs.modifierEntite(res);
                 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                 alert.setTitle("Valider");
                 alert.setHeaderText(null);
                 alert.setContentText("Reservation updated succesfully !");
                 alert.showAndWait();
                 dateinfo2.setVisible(false);
                 dateinfo1.setVisible(false);
                 linfo1.setText("Date debut : "+res.getDateDebut());
                 linfo2.setText("HDate fin : "+res.getDateFin());
                 linfo1.setVisible(true);
                 linfo2.setVisible(true);
                 btnupdate.setText("Update");
            }
        }
    }

    @FXML
    void topayment(ActionEvent event) {
        pn_paie.toFront();
    }

    @FXML
    void tores(ActionEvent event) {
        pn_res.toFront();
    }

    public void setData(Reservation r) {
        id = r.getIdReservation();
        res = r ;
        if(!Objects.equals(r.getStatut(), "Accepted"))
        {
            btnpayment.setText("");
        }else if(Objects.equals(r.getStatut(), "Accepted")){
            Paiement paiement=ps.afficherPaiementParId(id);
            p = paiement;
            amount.setText("Required amount : " + paiement.getMontant());
            if(p.getMethode()!=null){
                btnpayment.setText("Payment \n done");
                btnpayment.setDisable(true);
                btncancel.setText("");
            }
        }
        tfinfo2.setVisible(false);
        dateinfo1.setVisible(false);
        dateinfo2.setVisible(false);
        tfinfo2.setEditable(false);
        dateinfo2.setEditable(false);
        dateinfo1.setEditable(false);
        if(r.getIdClinique()!=0)
        {
            ltype.setText("Réservation Clinique ("+r.getStatut()+")");
            linfo1.setVisible(false);
            linfo2.setVisible(false);
            btnupdate.setText("");
            text = "Type réservation : Clinique || Status "+ res.getStatut();
        } else if (r.getIdTransport()!=0) {
            ltype.setText("Réservation Transport ("+r.getStatut()+")");
            linfo1.setText("Date : "+r.getDateDepart());
            linfo2.setText("Heure : "+r.getHeureDepart());
            text = "Type réservation : Transport || Date depart : "+res.getDateDepart()+"|| Heure depart : "+res.getHeureDepart() +"  || Status "+ res.getStatut();
        } else if (r.getIdHebergement()!=0) {
            ltype.setText("Réservation Hébergement ("+r.getStatut()+")");
            linfo1.setText("Date debut : "+r.getDateDebut());
            linfo2.setText("Date fin : "+r.getDateFin());
            text = "Type réservation : Hébergement || Date debut : "+res.getDateDebut()+"|| Date fin : "+res.getDateFin() +"  || Status "+ res.getStatut();
        }
    }

    @FXML
    void submitP(ActionEvent event) {
        if(virement.isSelected())
        {
            p.setMethode("virement");
            ps.modifierEntite(p);
            MailSender ms = new MailSender();
            try {
                ms.sendMail("beyaabid876@gmail.com");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Valider");
            alert.setHeaderText(null);
            alert.setContentText("Reservation updated succesfully !");
            alert.showAndWait();
            btnpayment.setText("Payment \n done");
            btnpayment.setDisable(true);
            pn_res.toFront();
            btncancel.setText("");
        } else if (espece.isSelected()) {
            p.setMethode("espece");
            ps.modifierEntite(p);
            MailSender ms = new MailSender();
            try {
                ms.sendMail("beyaabid876@gmail.com");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Valider");
            alert.setHeaderText(null);
            alert.setContentText("Reservation updated succesfully !");
            alert.showAndWait();
            btnpayment.setText("Payment \n done");
            btnpayment.setDisable(true);
            pn_res.toFront();
            btncancel.setText("");
        }else if (paypal.isSelected()){
            p.setMethode("paypal");
            ps.modifierEntite(p);
            MailSender ms = new MailSender();
            try {
                ms.sendMail("beyaabid876@gmail.com");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Valider");
            alert.setHeaderText(null);
            alert.setContentText("Reservation updated succesfully !");
            alert.showAndWait();
            btnpayment.setText("Payment \n done");
            btnpayment.setDisable(true);
            pn_res.toFront();
            btncancel.setText("");
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Select Payment method !");
            alert.showAndWait();
        }
    }

    @FXML
    void onRadioEspeceSelected(ActionEvent event) {
        // Deselect other radio buttons
        virement.setSelected(false);
        paypal.setSelected(false);
    }

    @FXML
    void onRadioPayplaySelected(ActionEvent event) {
        espece.setSelected(false);
        virement.setSelected(false);
    }

    @FXML
    void onRadioVirementSelected(ActionEvent event) {
        paypal.setSelected(false);
        espece.setSelected(false);
    }


    @FXML
    void codeqr(ActionEvent event) {
        Image qrImage = generateQRCodeImage(text);
        image.setImage(qrImage);
    }

    public static Image generateQRCodeImage(String text) {
        try {
            System.out.println("Generating QR code for text: " + text);  // Debugging
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 1);  // margin around the QR code

            // Create a BitMatrix (2D array of bits) for the QR code
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    text, BarcodeFormat.QR_CODE, 200, 200, hintMap);
            System.out.println(text);
            // Convert BitMatrix to an Image
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            javafx.scene.image.WritableImage writableImage = new javafx.scene.image.WritableImage(width, height);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    writableImage.getPixelWriter().setArgb(x, y, bitMatrix.get(x, y) ? javafx.scene.paint.Color.BLACK.hashCode() : Color.RED.hashCode());
                }
            }
            return writableImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Check if this is being returned
        }
    }


}
