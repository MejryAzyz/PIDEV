package models;

public class Docteur {
    private int id_docteur,id_clinique,id_specialite;
    private String nom,prenom,email,telephone;

    public Docteur(int id_clinique, int id_specialite, String nom, String prenom, String email, String telephone) {
        this.id_clinique = id_clinique;
        this.id_specialite = id_specialite;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    public Docteur(int id_docteur, int id_clinique, int id_specialite, String nom, String prenom, String email, String telephone) {
        this.id_docteur = id_docteur;
        this.id_clinique = id_clinique;
        this.id_specialite = id_specialite;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    public Docteur() {}

    public int getId_docteur() {
        return id_docteur;
    }

    public void setId_docteur(int id_docteur) {
        this.id_docteur = id_docteur;
    }

    public int getId_clinique() {
        return id_clinique;
    }

    public void setId_clinique(int id_clinique) {
        this.id_clinique = id_clinique;
    }

    public int getId_specialite() {
        return id_specialite;
    }

    public void setId_specialite(int id_specialite) {
        this.id_specialite = id_specialite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Docteur{" +
                "id_docteur=" + id_docteur +
                ", id_clinique=" + id_clinique +
                ", id_specialite=" + id_specialite +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
