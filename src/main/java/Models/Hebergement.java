package Models;

public class Hebergement {
    private int id_hebergement;
    private String nom;
    private String adresse;
    private int telephone;
    private String email;
    private int capacite;
    private double tarif_nuit;
    private String photoUrl;
    private double latitude;
    private double longitude;
    // ... other fields and getters/setters ...

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public Hebergement(String text, String text1, int i, String text2, int i1, double v, String photoUrl) {
        this.nom = text;
        this.adresse = text1;
        this.telephone = i;
        this.email = text2;
        this.capacite = i1;
        this.tarif_nuit = v;
        this.photoUrl = photoUrl;
    }

    public int getId_hebergement() {
        return id_hebergement;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public int getCapacite() {
        return capacite;
    }

    public double getTarif_nuit() {
        return tarif_nuit;
    }

    public void setId_hebergement(int id_hebergement) {
        this.id_hebergement = id_hebergement;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public void setTarif_nuit(double tarif_nuit) {
        this.tarif_nuit = tarif_nuit;
    }

    @Override
    public String toString() {
        return "Hebergement{" +
                "id_hebergement=" + id_hebergement +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", capacite='" + capacite + '\'' +
                ", tarif_nuit='" + tarif_nuit + '\'' +
                '}';
    }


    public Hebergement() {
    }

    public Hebergement(int id_hebergement, String nom, String adresse, int telephone, String email, int capacite, double tarif_nuit) {
        this.id_hebergement = id_hebergement;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.capacite = capacite;
        this.tarif_nuit = tarif_nuit;
    }

    public Hebergement(String nom, String adresse, int telephone, String email, int capacite, double tarif_nuit) {
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.capacite = capacite;
        this.tarif_nuit = tarif_nuit;
    }



}
