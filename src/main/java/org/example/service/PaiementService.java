package org.example.service;

import org.example.entities.Paiement;
import org.example.tools.DBconnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementService implements ICrud<Paiement>{
    Connection cnx2;

    public PaiementService() {
        cnx2 = DBconnexion.getInstance().getCnx();
    }

    @Override
    public void ajouterEntite(Paiement p) {
        String req = "INSERT INTO paiement (id_reservation,montant) VALUES (?,?)";
        try {
            PreparedStatement st = cnx2.prepareStatement(req);
            st.setInt(1, p.getIdReservation());
            st.setDouble(2, p.getMontant());
            st.executeUpdate();
            System.out.println("Paiement ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Paiement> afficherEntite() {
        List<Paiement> paiements = new ArrayList<>();
        String req = "SELECT * FROM paiement";
        try {
            Statement st = cnx2.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                paiements.add(new Paiement(rs.getInt("id_paiement"), rs.getInt("id_reservation"), rs.getDouble("montant"), rs.getTimestamp("date_paiement"), rs.getString("methode")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return paiements;
    }

    public Paiement afficherPaiementParId(int paiementId) {
        Paiement paiement = null;
        String req = "SELECT * FROM paiement WHERE id_reservation = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1, paiementId);  // Set the paiementId as a parameter in the query
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                paiement = new Paiement(rs.getInt("id_paiement"),
                        rs.getInt("id_reservation"),
                        rs.getDouble("montant"),
                        rs.getTimestamp("date_paiement"),
                        rs.getString("methode"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return paiement;
    }


    @Override
    public void modifierEntite(Paiement p) {
        String req = "UPDATE paiement SET montant=?, date_paiement=?, methode=? WHERE id_paiement=?";
        try {
            PreparedStatement st = cnx2.prepareStatement(req);
            st.setDouble(1, p.getMontant());
            st.setTimestamp(2, p.getDatePaiement());
            st.setString(3, p.getMethode());
            st.setInt(4, p.getIdPaiement());
            st.executeUpdate();
            System.out.println("Modification réussie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimerEntite(Paiement p) {
        String req = "DELETE FROM paiement WHERE id_reservation=?";
        try {
            PreparedStatement st = cnx2.prepareStatement(req);
            st.setInt(1, p.getIdReservation());
            st.executeUpdate();
            System.out.println("Suppression réussie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet Getall() {
        ResultSet rs = null;
        try {
            String req = "SELECT \n" +
                    "    p.id_paiement, \n" +
                    "    p.id_reservation, \n" +
                    "    p.montant, \n" +
                    "    p.date_paiement, \n" +
                    "    p.methode, \n" +
                    "    CASE \n" +
                    "        WHEN r.id_clinique != 0 THEN 'clinic' \n" +
                    "        WHEN r.id_hebergement != 0 THEN 'hebergement' \n" +
                    "        WHEN r.id_transport != 0 THEN 'transport'\n" +
                    "        ELSE 'unknown'  -- You can replace 'unknown' with any default value if needed\n" +
                    "    END AS type\n" +
                    "FROM \n" +
                    "    paiement p\n" +
                    "JOIN \n" +
                    "    reservation r ON p.id_reservation = r.id_reservation;\n";
            PreparedStatement st = cnx2.prepareStatement(req);
            rs = st.executeQuery(req);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;    }

    public ResultSet Getallm() {
        ResultSet rs = null;
        try {
            String req = "SELECT \n" +
                    "    p.id_paiement, \n" +
                    "    p.id_reservation, \n" +
                    "    p.montant, \n" +
                    "    p.date_paiement, \n" +
                    "    p.methode, \n" +
                    "    CASE \n" +
                    "        WHEN r.id_clinique != 0 THEN 'clinic' \n" +
                    "        WHEN r.id_hebergement != 0 THEN 'hebergement' \n" +
                    "        WHEN r.id_transport != 0 THEN 'transport'\n" +
                    "        ELSE 'unknown'  -- You can replace 'unknown' with any default value if needed\n" +
                    "    END AS type\n" +
                    "FROM \n" +
                    "    paiement p\n" +
                    "JOIN \n" +
                    "    reservation r ON p.id_reservation = r.id_reservation order by p.montant;\n" ;
            PreparedStatement st = cnx2.prepareStatement(req);
            rs = st.executeQuery(req);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;    }


    public double getTotalMontant() {
        double total = 0.0;
        String req = "SELECT SUM(montant) AS total FROM paiement WHERE methode IS NOT NULL";

        try {
            Statement st = cnx2.createStatement();
            ResultSet rs = st.executeQuery(req);
            if (rs.next()) {
                total = rs.getDouble("total");  // Fetch the result of the SUM operation
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return total;
    }
}

