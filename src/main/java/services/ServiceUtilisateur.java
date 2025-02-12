package services;

import models.Utilisateur;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateur implements IService<Utilisateur> {
    Connection cnx;

    public ServiceUtilisateur() {
        cnx = MyDataBase.getInstance().getCnx();
    }
    @Override
    public void ajouter(Utilisateur u) throws SQLException  {
        String sql = "INSERT INTO utilisateur (id_role, nom, prenom, email, mot_de_passe, telephone, date_naissance, adresse) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setInt(1, u.getIdRole());
        stmt.setString(2, u.getNom());
        stmt.setString(3, u.getPrenom());
        stmt.setString(4, u.getEmail());
        stmt.setString(5, u.getMotDePasse());
        stmt.setString(6, u.getTelephone());
        stmt.setDate(7, new java.sql.Date(u.getDateNaissance().getTime()));
        stmt.setString(8, u.getAdresse());
        stmt.executeUpdate();
        System.out.println("user added");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";

        PreparedStatement stmt = cnx.prepareStatement(sql);
        {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("user deleted ");
        }
    }
    @Override
    public void modifier(int id,String nom, String prenom, String email, String motDePasse, String telephone, String adresse) throws SQLException {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, telephone = ?, adresse = ? WHERE id_utilisateur = ?";
             PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, motDePasse);
            stmt.setString(5, telephone);
            stmt.setString(6, adresse);
            stmt.setInt(7, id);
            stmt.executeUpdate();
        System.out.println("user modified");
        }



    @Override
    public List<Utilisateur> recuperer() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
             Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setIdUtilisateur(rs.getInt("id_utilisateur"));
                u.setIdRole(rs.getInt("id_role"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setMotDePasse(rs.getString("mot_de_passe"));
                u.setTelephone(rs.getString("telephone"));
                u.setDateNaissance(rs.getDate("date_naissance"));
                u.setAdresse(rs.getString("adresse"));
                utilisateurs.add(u);
            }
        return utilisateurs;
    }    }
