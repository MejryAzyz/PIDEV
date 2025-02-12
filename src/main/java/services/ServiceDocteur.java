package services;

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
    public void modifier(int id_docteur, String nom) throws SQLException {
        String sql = "UPDATE docteur SET nom = ? WHERE id_docteur = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, nom);
        ste.setInt(2, id_docteur);
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
            d.setNom(rs.getString("nom"));
            d.setPrenom(rs.getString("prenom"));
            d.setEmail(rs.getString("email"));
            d.setTelephone(rs.getString("telephone"));
            docteurs.add(d);
        }
        return docteurs;
    }
}
