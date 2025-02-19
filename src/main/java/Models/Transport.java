package Models;

public class Transport {
    private int id_transport;
    private String type;
    private int capacite;
    private double tarif;

    public int getId_transport() {
        return id_transport;
    }

    public String getType() {
        return type;
    }

    public int getCapacite() {
        return capacite;
    }

    public double getTarif() {
        return tarif;
    }

    public void setId_transport(int id_transport) {
        this.id_transport = id_transport;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public Transport() {}

    public Transport(int id_transport, String type, int capacite, double tarif) {
        this.id_transport = id_transport;
        this.type = type;
        this.capacite = capacite;
        this.tarif = tarif;
    }

    public Transport(String type, int capacite, double tarif) {
        this.type = type;
        this.capacite = capacite;
        this.tarif = tarif;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "id_transport=" + id_transport +
                ", type=" + type +
                ", capacite=" + capacite +
                ", tarif=" + tarif +
                '}';
    }

    public String getPhotoUrl() {
        switch (type.toLowerCase()) {
            case "vtc":
                return "https://www.enquetesecuriteroutiere.org/images/blog/vtc.jpg";
            case "bus":
                return "https://img.linemedia.com/img/s/coach-bus-Temsa-Maraton-HDH-330kw-450HP-WC-USB-MATRIX-53-1-1-NO-FLIXBUS---1739353811855939579_common--25021210532098263900.jpg";
            case "voiture":
                return "https://img-4.linternaute.com/XpIwVOW1cq3k-npLPf_h0kYFxVE=/1500x/smart/52373731986a4566806476f7acb2dc3a/ccmcms-linternaute/10654353.jpg";
            case "taxi":
                return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRae_MuifW4xM_iK-vi-_El7z9KLsY1E8yGIw&s";
            case "van":
                return "https://i.ytimg.com/vi/sbyR9yBOPZA/hqdefault.jpg";
            default:
                return "https://upload.wikimedia.org/wikipedia/commons/6/65/No-Image-Placeholder.svg"; // Image par défaut
        }
    }
}
