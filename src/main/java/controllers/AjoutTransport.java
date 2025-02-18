package controllers;

import Models.Transport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceTransport;

import java.sql.SQLException;

public class AjoutTransport {

    private final ServiceTransport st = new ServiceTransport();

    @FXML
    private ComboBox<String> typet;

    @FXML
    private TextField capacitet;

    @FXML
    private TextField prixt;


    @FXML
    public void initialize() {
        typet.getItems().clear();
        typet.getItems().addAll("Taxi", "Bus", "Van", "Voiture", "VTC");
    }


    @FXML
    void ajouttrans(ActionEvent event) {
        try {
            String type = typet.getValue();
            int capacite = Integer.parseInt(capacitet.getText());
            int prix = Integer.parseInt(prixt.getText());

            st.ajouter(new Transport(type, capacite, prix));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Transport ajouté avec succès !");
            alert.showAndWait();
            Stage stage = (Stage) typet.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Veuillez entrer des valeurs valides pour la capacité et le prix.");
            alert.showAndWait();
        }
    }
}
