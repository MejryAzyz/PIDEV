package services;

import models.Specialite;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceSpecialite implements IService<Specialite> {
    Connection cnx;
    public ServiceSpecialite() {
        cnx = MyDataBase.getInstance().getCnx();
    }
    @Override
    public void ajouter(Specialite s) throws SQLException {
        String sql = "INSERT INTO specialite (nom) VALUES (?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, s.getNom());
        ps.executeUpdate();
        System.out.println("Spécialité ajoutée");


    }

    @Override
    public void supprimer(int id_specialite) throws SQLException {
        String sql = "DELETE FROM specialite WHERE id_specialite = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id_specialite);
        ps.executeUpdate();
        System.out.println("Spécialité supprimée");
    }

    @Override
    public void modifier(Specialite specialite) throws SQLException {
        String sql = "UPDATE specialite SET nom = ? WHERE id_specialite = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, specialite.getNom());
        ps.setInt(2, specialite.getId_specialite());
        ps.executeUpdate();
        System.out.println("Spécialité modifiée");

    }

    @Override
    public List<Specialite> recuperer() throws SQLException {
        String sql = "SELECT * FROM specialite";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Specialite> specialites = new ArrayList<Specialite>();
        while (rs.next()) {
            Specialite s = new Specialite();
            s.setId_specialite(rs.getInt("id_specialite"));
            s.setNom(rs.getString("nom"));
            specialites.add(s);
        }
        return specialites;
    }

    public List<String> getAllSpecialiteNames() throws SQLException {
        List<String> specialites = new ArrayList<>();
        String sql = "SELECT nom FROM specialite";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            specialites.add(rs.getString("nom"));
        }
        return specialites;
    }

    public int getIdByName(String nom) throws SQLException {
        String sql = "SELECT id_specialite FROM specialite WHERE nom = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, nom);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_specialite");
        }
        throw new SQLException("Spécialité non trouvée");
    }

    public String getNameById(int id_specialite) throws SQLException {
        String sql = "SELECT nom FROM specialite WHERE id_specialite = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id_specialite);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("nom");
        }
        return "N/A"; // Si aucune spécialité n'est trouvée
    }

}
