package Modeles;

import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OffreEmplois {
    private int id;
    private String titre;
    private String description;
    private String typePoste;
    private LocalDate datePublication;
    private String ImageURL;
    public OffreEmplois(String titre, String description,String typePoste,LocalDate datePublication,String ImageURL)
    {
        this.titre = titre;
        this.description = description;
        this.typePoste = typePoste;
        this.datePublication = LocalDate.now();
        this.ImageURL = ImageURL;
    }
    public static String convertDateFormat(LocalDate date) {
        if (date == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypePoste() {
        return typePoste;
    }

    public void setTypePoste(String typePoste) {
        this.typePoste = typePoste;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}


