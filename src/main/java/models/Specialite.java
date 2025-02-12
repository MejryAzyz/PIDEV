package models;

public class Specialite {
    private int id_specialite;
    private String nom;

    public Specialite(int id_specialite, String nom) {
        this.id_specialite = id_specialite;
        this.nom = nom;
    }

    public Specialite(String nom) {
        this.nom = nom;
    }

    public Specialite() {
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

    @Override
    public String toString() {
        return "Specialite{" +
                "id_specialite=" + id_specialite +
                ", nom='" + nom + '\'' +
                '}';
    }
}
