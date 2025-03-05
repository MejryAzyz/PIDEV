package service;

import Models.Hebergement;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceHebergement implements IService<Hebergement> {

    private Connection cnx;

    public ServiceHebergement() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Hebergement hebergement) throws SQLException {
        String sql = "INSERT INTO hebergement (nom, adresse, capacite, email, telephone, tarif_nuit, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, hebergement.getNom());
            pst.setString(2, hebergement.getAdresse());
            pst.setInt(3, hebergement.getCapacite());
            pst.setString(4, hebergement.getEmail());
            pst.setInt(5, hebergement.getTelephone());
            pst.setDouble(6, hebergement.getTarif_nuit());
            pst.setString(7, hebergement.getPhotoUrl());
            pst.executeUpdate();
            System.out.println("Hébergement ajouté");
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM hebergement WHERE id_hebergement = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Hébergement supprimé");
            } else {
                System.out.println("Aucun hébergement trouvé avec l'ID : " + id);
            }
        }
    }

    @Override
    public void modifier(Hebergement hebergement) throws SQLException {
        String sql = "UPDATE hebergement SET nom = ?, adresse = ?, capacite = ?, email = ?, telephone = ?, tarif_nuit = ?, image_url = ? WHERE id_hebergement = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getAdresse());
            ps.setInt(3, hebergement.getCapacite());
            ps.setString(4, hebergement.getEmail());
            ps.setInt(5, hebergement.getTelephone());
            ps.setDouble(6, hebergement.getTarif_nuit());
            ps.setString(7, hebergement.getPhotoUrl());
            ps.setInt(8, hebergement.getId_hebergement());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Hébergement modifié avec succès !");
            } else {
                System.out.println("Aucun hébergement trouvé avec l'ID : " + hebergement.getId_hebergement());
            }
        }
    }

    @Override
    public List<Hebergement> recuperer() throws SQLException {
        String sql = "SELECT id_hebergement, nom, adresse, capacite, email, telephone, tarif_nuit, image_url FROM hebergement";
        List<Hebergement> hebergements = new ArrayList<>();
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Hebergement hebergement = new Hebergement();
                hebergement.setId_hebergement(rs.getInt("id_hebergement"));
                hebergement.setNom(rs.getString("nom"));
                hebergement.setAdresse(rs.getString("adresse"));
                hebergement.setCapacite(rs.getInt("capacite"));
                hebergement.setEmail(rs.getString("email"));
                hebergement.setTelephone(rs.getInt("telephone"));
                hebergement.setTarif_nuit(rs.getDouble("tarif_nuit"));
                hebergement.setPhotoUrl(rs.getString("image_url"));
                hebergements.add(hebergement);
            }
        }
        return hebergements;
    }

    public int getNombreTotalHebergements() throws SQLException {
        String sql = "SELECT COUNT(*) FROM hebergement";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public double getTarifMoyenParNuit() throws SQLException {
        String sql = "SELECT AVG(tarif_nuit) FROM hebergement";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    public int getCapaciteTotale() throws SQLException {
        String sql = "SELECT SUM(capacite) FROM hebergement";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}