package service.User;

import Models.User.Utilisateur;
import service.IService;
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
    public void modifier(Utilisateur u) {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, telephone = ?, adresse = ? WHERE id_utilisateur = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getMotDePasse());  // Ensure you set all placeholders, including mot_de_passe
            stmt.setString(5, u.getTelephone());
            stmt.setString(6, u.getAdresse());
            stmt.setInt(7, u.getIdUtilisateur()); // Set the ID at the end, after all other fields
            stmt.executeUpdate();
            System.out.println("User modified successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            // Optionally, you can throw a custom exception or alert the user
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
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
    }
    public Utilisateur login(String email, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ?";
        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, motDePasse);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Utilisateur utilisateur = new Utilisateur(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("mot_de_passe"),
                    rs.getString("telephone"),
                    rs.getDate("date_naissance"),
                    rs.getString("adresse")
            );
            utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
            utilisateur.setIdRole(rs.getInt("id_role"));
            return utilisateur;
        }
        return null;
    }

}
