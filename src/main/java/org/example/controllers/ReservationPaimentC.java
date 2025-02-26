package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.example.entities.Paiement;
import org.example.entities.Reservation;
import org.example.service.PaiementService;
import org.example.service.ReservationService;

import java.util.Objects;

public class ReservationPaimentC {

    @FXML
    private Hyperlink btnpayment;

    @FXML
    private Hyperlink btnreject;

    @FXML
    private Hyperlink btnaccept;

    @FXML
    private Label linfo1;

    @FXML
    private Label linfo2;

    @FXML
    private Label ltype;

    @FXML
    private Pane pn_paie;

    @FXML
    private Pane pn_res;

    @FXML
    private TextField tf_amount;

    @FXML
    private Pane pn_paie1;


    @FXML
    private Label payments;

    @FXML
    private Label reqam;

    ReservationService rs = new ReservationService();
    PaiementService ps = new PaiementService();
    Reservation res = new Reservation();
    Paiement p = new Paiement();
    int id ;

    @FXML
    void reject(ActionEvent event) {
        res.setStatut("Rejected");
        rs.modifierEntite(res);
        Paiement p = new Paiement();
        p.setIdReservation(id);
        ps.supprimerEntite(p);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Valider");
        alert.setHeaderText(null);
        alert.setContentText("Reservation Statut updated succesfully !");
        alert.showAndWait();
        btnpayment.setText("");
        btnreject.setText("");
        btnaccept.setText("Accept");
        if(res.getIdClinique()!=0)
        {
            ltype.setText("Reservation Clinic ("+res.getStatut()+")");
            linfo1.setVisible(false);
            linfo2.setVisible(false);
        } else if (res.getIdTransport()!=0) {
            ltype.setText("Reservation Transport ("+res.getStatut()+")");
            linfo1.setText("Date : "+res.getDateDepart());
            linfo2.setText("Heure : "+res.getHeureDepart());
        } else if (res.getIdHebergement()!=0) {
            ltype.setText("Reservation Hebergement ("+res.getStatut()+")");
            linfo1.setText("Date debut : "+res.getDateDebut());
            linfo2.setText("Date fin : "+res.getDateFin());
        }
    }

    @FXML
    void accept(ActionEvent event) {
        pn_paie1.toFront();
    }

    // Fonction pour vérifier si une chaîne est un nombre valide
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str); // Essaie de convertir en double
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    void save(ActionEvent event) {
        if (tf_amount.getText().isEmpty() || !isNumeric(tf_amount.getText())) {
            // Afficher un message d'alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir le champ et entrer uniquement des chiffres valides !");
            alert.showAndWait();
            return;
        }
        int amount = Integer.valueOf(tf_amount.getText());
        res.setStatut("Accepted");
        rs.modifierEntite(res);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Valider");
        alert.setHeaderText(null);
        alert.setContentText("Reservation Statut updated succesfully !");
        alert.showAndWait();
        Paiement p = new Paiement();
        p.setIdReservation(id);
        p.setMontant(amount);
        ps.ajouterEntite(p);
        btnpayment.setText("Payment");
        btnreject.setText("Reject");
        btnaccept.setText("");
        if(res.getIdClinique()!=0)
        {
            ltype.setText("Reservation Clinic ("+res.getStatut()+")");
            linfo1.setVisible(false);
            linfo2.setVisible(false);
        } else if (res.getIdTransport()!=0) {
            ltype.setText("Reservation Transport ("+res.getStatut()+")");
            linfo1.setText("Date : "+res.getDateDepart());
            linfo2.setText("Heure : "+res.getHeureDepart());
        } else if (res.getIdHebergement()!=0) {
            ltype.setText("Reservation Hebergement ("+res.getStatut()+")");
            linfo1.setText("Date debut : "+res.getDateDebut());
            linfo2.setText("Date fin : "+res.getDateFin());
        }
        Paiement pe = ps.afficherPaiementParId(id);
        reqam.setText("Required amount : "+pe.getMontant());
        if(pe.getMethode()!=null)
        {
            payments.setText("Payed with "+pe.getMethode());
        }else{
            payments.setText("No payments yet");
        }
        pn_res.toFront();
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
        System.out.println(r.getStatut());
        if(Objects.equals(r.getStatut(), "in progress"))
        {
            btnpayment.setText("");
        }else if(Objects.equals(r.getStatut(), "Rejected"))
        {
            btnpayment.setText("");
            btnreject.setText("");
        }else{
            btnaccept.setText("");
            Paiement p = ps.afficherPaiementParId(id);
            reqam.setText("Required amount : "+p.getMontant());
            if(p.getMethode()!=null)
            {
                payments.setText("Payed with "+p.getMethode());
            }else{
                payments.setText("No payments yet");
            }
        }
        if(r.getIdClinique()!=0)
        {
            ltype.setText("Reservation Clinic ("+r.getStatut()+")");
            linfo1.setVisible(false);
            linfo2.setVisible(false);
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


}
