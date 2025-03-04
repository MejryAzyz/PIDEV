package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jfxtras.scene.control.agenda.Agenda;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.time.ZoneId;
public class AccompagnateurProfil {

    @FXML
    private ImageView profileImage;
    @FXML
    private Button editProfileButton;
    @FXML
    private Agenda agenda; // L'Agenda de JFXtras

    @FXML
    public void initialize() {
        // Charger une image de profil par défaut
        InputStream imageStream = getClass().getResourceAsStream("/default-profile.png");
        if (imageStream == null) {
            System.err.println("❌ Image non trouvée : default-profile.png");
        } else {
            profileImage.setImage(new Image(imageStream));
        }

        // Conversion LocalDateTime -> Calendar
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(java.util.Date.from(LocalDateTime.now().plusHours(2)
                .atZone(ZoneId.systemDefault()).toInstant()));

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(java.util.Date.from(LocalDateTime.now().plusHours(3)
                .atZone(ZoneId.systemDefault()).toInstant()));

        // Ajouter un événement test au calendrier
        Agenda.AppointmentImpl appointment = new Agenda.AppointmentImpl()
                .withStartTime(startCalendar) // ✅ Compatible avec JFXtras 8.0-r1
                .withEndTime(endCalendar)     // ✅ Compatible avec JFXtras 8.0-r1
                .withSummary("Consultation avec un patient")
                .withDescription("Discussion sur le suivi médical")
                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"));

        // Ajouter l'événement à l'agenda
        agenda.appointments().add(appointment);
    }


    private void changerPhotoDeProfil() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une nouvelle photo de profil");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        var file = fileChooser.showOpenDialog(null);
        if (file != null) {
            profileImage.setImage(new Image(file.toURI().toString()));
        }
    }
}
