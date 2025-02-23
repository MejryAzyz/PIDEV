package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Docteur;
import tools.MyDataBase;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceDocteur implements IService<Docteur> {
    Connection cnx;
    public ServiceDocteur() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Docteur d) throws SQLException {
        String sql = "INSERT INTO docteur (id_clinique, id_specialite, nom, prenom, email, telephone) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, d.getId_clinique());
        ste.setInt(2, d.getId_specialite());
        ste.setString(3, d.getNom());
        ste.setString(4, d.getPrenom());
        ste.setString(5, d.getEmail());
        ste.setString(6, d.getTelephone());
        ste.executeUpdate();
        System.out.println("Docteur ajouté");


    }

    @Override
    public void supprimer(int id_docteur) throws SQLException {
        String sql = "DELETE FROM docteur WHERE id_docteur = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id_docteur);
        ste.executeUpdate();
        System.out.println("Docteur supprimé");
    }

    @Override
    public void modifier(Docteur docteur) throws SQLException {
        String sql = "UPDATE docteur SET nom = ? ,prenom=? , email = ? , telephone = ? ,id_specialite = ? , id_clinique = ? WHERE id_docteur = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, docteur.getNom());
        ste.setString(2,docteur.getPrenom());
        ste.setString(3, docteur.getEmail());
        ste.setString( 4, docteur.getTelephone());
        ste.setInt(5, docteur.getId_specialite());
        ste.setInt(6, docteur.getId_clinique());
        ste.setInt(7, docteur.getId_docteur());

        ste.executeUpdate();
        System.out.println("Docteur modifié");

    }

    @Override
    public List<Docteur> recuperer() throws SQLException {
        String sql = "SELECT * FROM docteur";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Docteur> docteurs = new ArrayList<Docteur>();
        while (rs.next()) {
            Docteur d = new Docteur();
            d.setId_docteur(rs.getInt("id_docteur"));
            d.setId_clinique(rs.getInt("id_clinique"));
            d.setId_specialite(rs.getInt("id_specialite"));
            d.setNom(rs.getString("nom"));
            d.setPrenom(rs.getString("prenom"));
            d.setEmail(rs.getString("email"));
            d.setTelephone(rs.getString("telephone"));
            docteurs.add(d);
        }
        return docteurs;
    }


    public List<Docteur> recupererParClinique(int id_clinique) throws SQLException {
        String sql = "SELECT * FROM docteur WHERE id_clinique = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id_clinique);
            ResultSet rs = st.executeQuery();
            List<Docteur> docteurs = new ArrayList<>();
            while (rs.next()) {
                Docteur d = new Docteur();
                d.setId_docteur(rs.getInt("id_docteur"));
                d.setId_clinique(rs.getInt("id_clinique"));
                d.setId_specialite(rs.getInt("id_specialite"));
                d.setNom(rs.getString("nom"));
                d.setPrenom(rs.getString("prenom"));
                d.setEmail(rs.getString("email"));
                d.setTelephone(rs.getString("telephone"));
                docteurs.add(d);
            }
            return docteurs;
        }
    }
    //
    public List<Integer> recupererCliniqueIdsParSpecialite(int specialiteId) throws SQLException {
        List<Integer> cliniqueIds = new ArrayList<>();
        String query = "SELECT id_clinique FROM docteur WHERE id_specialite = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, specialiteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cliniqueIds.add(rs.getInt("id_clinique"));
            }
        }

        return cliniqueIds;
    }

    public List<Docteur> recupererDocteursParCliniqueEtSpecialite(int cliniqueId, int specialiteId) throws SQLException {
        List<Docteur> docteurs = new ArrayList<>();
        String query = "SELECT * FROM docteur WHERE id_clinique = ? AND id_specialite = ?";

        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setInt(1, cliniqueId);
        stmt.setInt(2, specialiteId);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Docteur docteur = new Docteur(
                    rs.getInt("id_docteur"),
                    rs.getInt("id_clinique"),
                    rs.getInt("id_specialite"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("telephone")
            );
            docteurs.add(docteur);
        }
        return docteurs;
    }
}
