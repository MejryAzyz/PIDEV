package controllers.Reservation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import Models.Reservation.Paiement;

public class PaiementC {

    @FXML
    private Label linfo1;

    @FXML
    private Label linfo2;

    @FXML
    private Label ltype;

    @FXML
    private Pane pn_res;

    public void setData(Paiement paiement, String type) {
        ltype.setText(type+" payment in "+paiement.getDatePaiement());
        linfo2.setText("Amount of : "+paiement.getMontant()+" dt");
        linfo1.setText("Payment method : "+paiement.getMethode());
    }
}
