package Models;

public class Transport {
    private	int id_transport;
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

    public Transport() {
    }

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

}
