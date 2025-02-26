package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
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

public class ReservationC {

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
            ltype.setText("Reservation Clinic ("+r.getStatut()+")");
            linfo1.setVisible(false);
            linfo2.setVisible(false);
            btnupdate.setText("");
        } else if (r.getIdTransport()!=0) {
            ltype.setText("Reservation Transport ("+r.getStatut()+")");
            linfo1.setText("Date : "+r.getDateDepart());
            linfo2.setText("Heure : "+r.getHeureDepart());
        } else if (r.getIdHebergement()!=0) {
            ltype.setText("Reservation Hebergement ("+r.getStatut()+")");
            linfo1.setText("Date debut : "+r.getDateDebut());
            linfo2.setText("Date fin : "+r.getDateFin());
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

    public void sendConfirmationEmail() {
        // Set up mail properties
        String host = "smtp.google.com"; // Replace with your SMTP host
        String from = "abdoubahouri123@gmail.com";  // Replace with your email
        String to = "abdoubahouri123@gmail.com";     // Replace with recipient's email
        String password = "bmrweoalpfbmygjs"; // Replace with your email password

        // Set up properties for the mail session
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587"); // Port for SMTP
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a new session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create the email content
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Payment Confirmation");
            message.setText("Your payment has been successfully processed. Thank you for your reservation!");

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email.");
        }
    }

}
