package org.example.entities;

import java.sql.Timestamp;

public class Paiement {
    private int idPaiement;
    private int idReservation;
    private double montant;
    private Timestamp datePaiement;
    private String methode;

    public Paiement() {}

    public Paiement(int idPaiement, int idReservation, double montant, Timestamp datePaiement, String methode) {
        this.idPaiement = idPaiement;
        this.idReservation = idReservation;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.methode = methode;
    }

    public Paiement(int idReservation, double montant, Timestamp datePaiement, String methode) {
        this.idReservation = idReservation;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.methode = methode;
    }

    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Timestamp getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Timestamp datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }
}