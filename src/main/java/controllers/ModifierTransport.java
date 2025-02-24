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

public class ModifierTransport{

    @FXML
    private ComboBox<String> typet;
    @FXML
    private TextField capacitet, prixt;

    private Transport transport;
    private GestionTransport parentController;

    public void setTransport(Transport transport, GestionTransport parentController) {
        this.transport = transport;
        this.parentController = parentController;

        typet.getItems().addAll("Taxi", "Bus", "Van", "Voiture", "VTC");
        typet.setValue(transport.getType());
        capacitet.setText(String.valueOf(transport.getCapacite()));
        prixt.setText(String.valueOf(transport.getTarif()));
    }

    @FXML
    void saveChanges(ActionEvent event) {
        String nouveauType = typet.getValue();
        String nouvelleCapacite = capacitet.getText().trim();
        String nouveauPrix = prixt.getText().trim();

        if (nouveauType == null || nouvelleCapacite.isEmpty() || nouveauPrix.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs vides");
            alert.setHeaderText("Informations manquantes");
            alert.setContentText("Veuillez remplir tous les champs avant de modifier le transport.");
            alert.showAndWait();
            return;
        }

        try {
            transport.setType(nouveauType);
            transport.setCapacite(Integer.parseInt(nouvelleCapacite));
            transport.setTarif(Double.parseDouble(nouveauPrix));

            ServiceTransport serviceTransport = new ServiceTransport();
            serviceTransport.modifier(transport);
            parentController.refreshCard();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Modification réussie");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Transport modifié avec succès !");
            successAlert.showAndWait();

            Stage stage = (Stage) typet.getScene().getWindow();
            stage.close();

            if (parentController != null) {
                parentController.afficherTransport();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Erreur lors de la modification");
            errorAlert.setContentText("Impossible de modifier le transport. Vérifiez votre connexion à la base de données.");
            errorAlert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Format incorrect");
            alert.setContentText("Veuillez entrer un nombre valide pour la capacité et le prix.");
            alert.showAndWait();
        }
    }
}
