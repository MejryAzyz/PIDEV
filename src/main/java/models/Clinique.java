package models;

import java.util.ArrayList;
import java.util.List;

public class Clinique {
    private int idClinique ,rate;
    private String nom,adresse,telephone,email,description;
    private double prix;
    private List<Docteur> docteurs = new ArrayList<>();
    private List<Specialite> specialites = new ArrayList<>();

    public Clinique(int rate, String nom, String adresse, String telephone, String email, String description, double prix) {
        this.rate = rate;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.description = description;
        this.prix = prix;

    }

    public Clinique(String nom, String adresse, String telephone, String email, String description, double prix) {
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.description = description;
        this.prix = prix;
    }

    public Clinique(int idClinique, int rate, String nom, String adresse, String telephone, String email, String description, double prix) {
        this.idClinique = idClinique;
        this.rate = rate;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.description = description;
        this.prix = prix;
    }

    public Clinique() {}

    public int getIdClinique() {
        return idClinique;
    }

    public void setIdClinique(int idClinique) {
        this.idClinique = idClinique;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Clinique{" +
                "idClinique=" + idClinique +
                ", rate=" + rate +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                '}';
    }
    //
}
