package Models.Reservation;

import java.sql.Date;
import java.sql.Timestamp;

public class Reservation {
    private int idReservation;
    private int idPatient;
    private int idClinique;
    private int idTransport;
    private Date dateDepart;
    private String heureDepart;
    private int idHebergement;
    private Date dateDebut;
    private Date dateFin;
    private Timestamp dateReservation;
    private String statut;

    public Reservation() {}

    public Reservation(int idReservation, int idPatient, int idClinique, int idTransport, Date dateDepart, String heureDepart, int idHebergement, Date dateDebut, Date dateFin, String statut) {
        this.idReservation = idReservation;
        this.idPatient = idPatient;
        this.idClinique = idClinique;
        this.idTransport = idTransport;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.idHebergement = idHebergement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    public Reservation(int idPatient, int idClinique, int idTransport, Date dateDepart, String heureDepart, int idHebergement, Date dateDebut, Date dateFin, String statut) {
        this.idPatient = idPatient;
        this.idClinique = idClinique;
        this.idTransport = idTransport;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.idHebergement = idHebergement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    public Reservation(int idPatient, int idClinique, String statut) {
        this.idPatient = idPatient;
        this.idClinique = idClinique;
        this.statut = statut;
    }

    public Reservation(int idPatient, int idTransport, Date dateDepart, String heureDepart, String statut) {
        this.idPatient = idPatient;
        this.idTransport = idTransport;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.statut = statut;
    }

    public Reservation(int idReservation, int idPatient, int idClinique, int idTransport, Date dateDepart, String heureDepart, int idHebergement, Date dateDebut, Date dateFin, Timestamp dateReservation, String statut) {
        this.idReservation = idReservation;
        this.idPatient = idPatient;
        this.idClinique = idClinique;
        this.idTransport = idTransport;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.idHebergement = idHebergement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dateReservation = dateReservation;
        this.statut = statut;
    }

    public Reservation(int idPatient, int idHebergement, Date dateDebut, Date dateFin, String statut) {
        this.idPatient = idPatient;
        this.idHebergement = idHebergement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getIdClinique() {
        return idClinique;
    }

    public void setIdClinique(int idClinique) {
        this.idClinique = idClinique;
    }

    public int getIdTransport() {
        return idTransport;
    }

    public void setIdTransport(int idTransport) {
        this.idTransport = idTransport;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(String heureDepart) {
        this.heureDepart = heureDepart;
    }

    public int getIdHebergement() {
        return idHebergement;
    }

    public void setIdHebergement(int idHebergement) {
        this.idHebergement = idHebergement;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Timestamp getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Timestamp dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}