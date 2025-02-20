package controllers.HT;

import Models.HT.Hebergement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.HT.ServiceHebergement;

import java.sql.SQLException;

public class ModifierHebergement {

    @FXML
    private TextField nomheb, telheb, capaciteheb, adresseheb, emailheb, tarifheb;

    private Hebergement hebergement;
    private ListeHebergement parentController;

    public void setHebergement(Hebergement hebergement, ListeHebergement parentController) {
        this.hebergement = hebergement;
        this.parentController = parentController;

        nomheb.setText(hebergement.getNom());
        telheb.setText(String.valueOf(hebergement.getTelephone()));
        capaciteheb.setText(String.valueOf(hebergement.getCapacite()));
        adresseheb.setText(hebergement.getAdresse());
        emailheb.setText(hebergement.getEmail());
        tarifheb.setText(String.valueOf(hebergement.getTarif_nuit()));
    }

    @FXML
    void ModifierHebergement(ActionEvent event) {
        String nouveauNom = nomheb.getText().trim();
        String nouveauTel = telheb.getText().trim();
        String nouvelleCapacite = capaciteheb.getText().trim();
        String nouvelleAdresse = adresseheb.getText().trim();
        String nouvelEmail = emailheb.getText().trim();
        String nouveauTarif = tarifheb.getText().trim();

        if (nouveauNom.isEmpty() || nouveauTel.isEmpty() || nouvelleCapacite.isEmpty() ||
                nouvelleAdresse.isEmpty() || nouvelEmail.isEmpty() || nouveauTarif.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs vides");
            alert.setHeaderText("Informations manquantes");
            alert.setContentText("Veuillez remplir tous les champs avant de modifier l'hébergement.");
            alert.showAndWait();
            return;
        }

        try {
            hebergement.setNom(nouveauNom);
            hebergement.setTelephone(Integer.parseInt(nouveauTel));
            hebergement.setCapacite(Integer.parseInt(nouvelleCapacite));
            hebergement.setAdresse(nouvelleAdresse);
            hebergement.setEmail(nouvelEmail);
            hebergement.setTarif_nuit(Integer.parseInt(nouveauTarif));

            ServiceHebergement serviceHebergement = new ServiceHebergement();
            serviceHebergement.modifier(hebergement);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Modification réussie");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Hébergement modifié avec succès !");
            successAlert.showAndWait();

            Stage stage = (Stage) nomheb.getScene().getWindow();
            stage.close();



        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Erreur lors de la modification");
            errorAlert.setContentText("Impossible de modifier l'hébergement. Vérifiez votre connexion à la base de données.");
            errorAlert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Format incorrect");
            alert.setContentText("Veuillez entrer un nombre valide pour la capacité et le tarif.");
            alert.showAndWait();
        }
    }


}
