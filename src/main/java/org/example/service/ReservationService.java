package org.example.service;

import org.example.tools.DBconnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.entities.*;

public class ReservationService implements ICrud<Reservation>{
    Connection cnx2;

    public ReservationService() {
        cnx2 = DBconnexion.getInstance().getCnx();
    }

    @Override
    public void ajouterEntite(Reservation r) {
        String req = "INSERT INTO reservation (id_patient, id_clinique, id_transport, date_depart, heure_depart, id_hebergement, date_debut, date_fin, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = cnx2.prepareStatement(req);
            st.setInt(1, r.getIdPatient());
            st.setInt(2, r.getIdClinique());
            st.setInt(3, r.getIdTransport());
            st.setDate(4, r.getDateDepart());
            st.setString(5, r.getHeureDepart());
            st.setInt(6, r.getIdHebergement());
            st.setDate(7, r.getDateDebut());
            st.setDate(8, r.getDateFin());
            st.setString(9, r.getStatut());
            st.executeUpdate();
            System.out.println("Réservation ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Reservation> afficherEntite() {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation";
        try {
            Statement st = cnx2.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                reservations.add(new Reservation(rs.getInt("id_reservation"), rs.getInt("id_patient"), rs.getInt("id_clinique"), rs.getInt("id_transport"), rs.getDate("date_depart"), rs.getString("heure_depart"), rs.getInt("id_hebergement"), rs.getDate("date_debut"), rs.getDate("date_fin"), rs.getTimestamp("date_reservation"), rs.getString("statut")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }

    @Override
    public void modifierEntite(Reservation r) {
        String req = "UPDATE reservation SET id_patient=?, id_clinique=?, id_transport=?, date_depart=?, heure_depart=?, id_hebergement=?, date_debut=?, date_fin=?, date_reservation=?, statut=? WHERE id_reservation=?";
        try {
            PreparedStatement st = cnx2.prepareStatement(req);
            st.setInt(1, r.getIdPatient());
            st.setInt(2, r.getIdClinique());
            st.setInt(3, r.getIdTransport());
            st.setDate(4, r.getDateDepart());
            st.setString(5, r.getHeureDepart());
            st.setInt(6, r.getIdHebergement());
            st.setDate(7, r.getDateDebut());
            st.setDate(8, r.getDateFin());
            st.setTimestamp(9, r.getDateReservation());
            st.setString(10, r.getStatut());
            st.setInt(11, r.getIdReservation());
            st.executeUpdate();
            System.out.println("Modification réussie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimerEntite(Reservation r) {
        String req = "DELETE FROM reservation WHERE id_reservation=?";
        try {
            PreparedStatement st = cnx2.prepareStatement(req);
            st.setInt(1, r.getIdReservation());
            st.executeUpdate();
            System.out.println("Suppression réussie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet Getall() {
        ResultSet rs = null;
        try {
            String req = "SELECT * FROM `reservation` ";
            PreparedStatement st = cnx2.prepareStatement(req);
            rs = st.executeQuery(req);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;    }
}

