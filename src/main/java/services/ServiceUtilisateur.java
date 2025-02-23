package services;

import models.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceUtilisateur implements IService<Utilisateur> {
    Connection cnx;

    public ServiceUtilisateur() {
        cnx = MyDataBase.getInstance().getCnx();
    }
    @Override
    public void ajouter(Utilisateur u) throws SQLException  {
        String sql = "INSERT INTO utilisateur (id_role, nom, prenom, email, mot_de_passe, telephone, date_naissance, adresse,image_url,status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = cnx.prepareStatement(sql);
        stmt.setInt(1, u.getIdRole());
        stmt.setString(2, u.getNom());
        stmt.setString(3, u.getPrenom());
        stmt.setString(4, u.getEmail());
        //methode loula securité hachage de password
        String hashedPassword = BCrypt.hashpw(u.getMotDePasse(), BCrypt.gensalt());
        stmt.setString(5, hashedPassword);

        stmt.setString(6, u.getTelephone());
        stmt.setDate(7, new java.sql.Date(u.getDateNaissance().getTime()));
        stmt.setString(8, u.getAdresse());
        stmt.setString(9, u.getImage_url());
        stmt.setInt(10, u.getStatus());

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
            stmt.setString(4, u.getMotDePasse());
            stmt.setString(5, u.getTelephone());
            stmt.setString(6, u.getAdresse());
            stmt.setInt(7, u.getIdUtilisateur());
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
        String sql = "SELECT * FROM utilisateur WHERE email = ? LIMIT 1";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("mot_de_passe");

                // Compare the input password with the stored hash
                if (BCrypt.checkpw(motDePasse, storedHashedPassword)) {
                    // Login success, return user object
                    Utilisateur utilisateur = new Utilisateur(
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            storedHashedPassword,  // Keep hashed password
                            rs.getString("telephone"),
                            rs.getDate("date_naissance"),
                            rs.getString("adresse"),
                            rs.getString("image_url")
                    );
                    utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
                    utilisateur.setIdRole(rs.getInt("id_role"));

                    System.out.println("welcome "+utilisateur.getNom()+" "+utilisateur.getPrenom());

                    return utilisateur;
                } else {
                    System.out.println("Incorrect password.");
                }
            }
        }
        return null;
    }


    public int countRegisteredUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateur";
        Statement stmt = cnx.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countConnectedUsers() throws SQLException {
        // Assuming there's a 'status' column where '1' means connected users
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE status = 1";
        Statement stmt = cnx.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    private Map<String, String> userVerificationTokens = new HashMap<>(); // email -> token

    // This method would be called when the user clicks the verification link
    public boolean verifyEmail(String token) {
        for (Map.Entry<String, String> entry : userVerificationTokens.entrySet()) {
            if (entry.getValue().equals(token)) {
                // Token matches, mark the user as verified (e.g., update their record in DB)
                System.out.println("User verified: " + entry.getKey());
                return true;
            }
        }
        return false;  // Token is invalid
    }
}
