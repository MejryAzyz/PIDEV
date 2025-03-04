package DAO;

import Modeles.Accompagnateur;
import Services.IServices;
import Utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccompagnateurDAO implements IServices<Accompagnateur> {
    private static final Logger LOGGER = Logger.getLogger(AccompagnateurDAO.class.getName());
    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public void ajouter(Accompagnateur accompagnateur) throws SQLException {
        String sql = "INSERT INTO accompagnateur (username, password_hash, email, fichier_cv, photo_profil, experience, motivation, langues, statut, date_recrutement) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, accompagnateur.getUsername());
            ps.setString(2, accompagnateur.getPasswordHash());  // ⚠️ Recommandation : Hashage sécurisé
            ps.setString(3, accompagnateur.getEmail());
            ps.setString(4, accompagnateur.getFichierCv());
            ps.setString(5, accompagnateur.getPhotoProfil());
            ps.setString(6, accompagnateur.getExperience());
            ps.setString(7, accompagnateur.getMotivation());
            ps.setString(8, accompagnateur.getLangues());
            ps.setString(9, accompagnateur.getStatut());

            // Correction de la gestion de la date
            LocalDate dateRecrutement = accompagnateur.getDateRecrutement();
            ps.setDate(10, (dateRecrutement != null) ? Date.valueOf(dateRecrutement) : null);

            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    accompagnateur.setIdAccompagnateur(generatedKeys.getInt(1));
                }
            }
            LOGGER.log(Level.INFO, "✅ Accompagnateur ajouté avec succès !");
        }
    }

    @Override
    public void modifier(Accompagnateur accompagnateur, int id) throws SQLException {
        String sql = "UPDATE accompagnateur SET username = ?, password_hash = ?, email = ?, fichier_cv = ?, photo_profil = ?, " +
                "experience = ?, motivation = ?, langues = ?, statut = ?, date_recrutement = ? WHERE id_accompagnateur = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, accompagnateur.getUsername());
            ps.setString(2, accompagnateur.getPasswordHash());
            ps.setString(3, accompagnateur.getEmail());
            ps.setString(4, accompagnateur.getFichierCv());
            ps.setString(5, accompagnateur.getPhotoProfil());
            ps.setString(6, accompagnateur.getExperience());
            ps.setString(7, accompagnateur.getMotivation());
            ps.setString(8, accompagnateur.getLangues());
            ps.setString(9, accompagnateur.getStatut());

            LocalDate dateRecrutement = accompagnateur.getDateRecrutement();
            ps.setDate(10, (dateRecrutement != null) ? Date.valueOf(dateRecrutement) : null);
            ps.setInt(11, id);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                LOGGER.log(Level.INFO, "✅ Accompagnateur mis à jour avec succès !");
            } else {
                LOGGER.log(Level.WARNING, "⚠️ Aucun accompagnateur trouvé avec l'ID : {0}", id);
            }
        }
    }

    @Override
    public void supprimer(int id_accompagnateur) throws SQLException {
        String sql = "DELETE FROM accompagnateur WHERE id_accompagnateur = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_accompagnateur);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                LOGGER.log(Level.INFO, "✅ Accompagnateur supprimé avec succès !");
            } else {
                LOGGER.log(Level.WARNING, "⚠️ Aucun accompagnateur trouvé avec l'ID : {0}", id_accompagnateur);
            }
        }
    }
    public void mettreAJourStatut(Accompagnateur ac) throws SQLException {
        String sql = "UPDATE accompagnateur SET statut = ? WHERE id_accompagnateur = ?";

        try (Connection conn = db.getConnection(); // Utilisez la même instance de DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ac.getStatut());
            stmt.setInt(2, ac.getIdAccompagnateur());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                LOGGER.log(Level.INFO, "✅ Statut mis à jour pour l'accompagnateur ID : {0}", ac.getIdAccompagnateur());
            } else {
                LOGGER.log(Level.WARNING, "⚠️ Aucun accompagnateur mis à jour. Vérifiez l'ID : {0}", ac.getIdAccompagnateur());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Erreur lors de la mise à jour du statut pour l'accompagnateur ID : " + ac.getIdAccompagnateur(), e);
            throw e;
        }
    }
    public static void afficherAccompagnateurs(List<Accompagnateur> accompagnateurs) {
        for (Accompagnateur ac : accompagnateurs) {
            System.out.println(ac);
        }
    }
    @Override
    public List<Accompagnateur> recuperer() throws SQLException {
        List<Accompagnateur> accompagnateurs = new ArrayList<>();
        String sql = "SELECT id_accompagnateur, username, password_hash, email, fichier_cv, photo_profil, "
                + "experience, motivation, langues, statut, date_recrutement FROM accompagnateur";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Accompagnateur ac = new Accompagnateur(
                        rs.getString("username"),
                        rs.getString("password_hash") != null ? rs.getString("password_hash") : "",
                        rs.getString("email"),
                        rs.getString("fichier_cv"),
                        rs.getString("photo_profil"),
                        rs.getString("experience"),
                        rs.getString("motivation"),
                        rs.getString("langues"),
                        rs.getString("statut"),
                        (rs.getDate("date_recrutement") != null) ? rs.getDate("date_recrutement").toLocalDate() : null
                );
                ac.setIdAccompagnateur(rs.getInt("id_accompagnateur"));
                accompagnateurs.add(ac);
            }
            LOGGER.log(Level.INFO, "✅ {0} accompagnateurs récupérés.", accompagnateurs.size());

            // Afficher les accompagnateurs dans la console
            afficherAccompagnateurs(accompagnateurs);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Erreur lors de la récupération des accompagnateurs : {0}", e.getMessage());
            throw e;
        }

        return accompagnateurs;
    }

    public List<Accompagnateur> recupererParStatut(String statut) throws SQLException {
        List<Accompagnateur> accompagnateurs = new ArrayList<>();
        String sql = "SELECT * FROM accompagnateur WHERE statut = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Accompagnateur ac = new Accompagnateur(
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("email"),
                            rs.getString("fichier_cv"),
                            rs.getString("photo_profil"),
                            rs.getString("experience"),
                            rs.getString("motivation"),
                            rs.getString("langues"),
                            rs.getString("statut"),
                            (rs.getDate("date_recrutement") != null) ? rs.getDate("date_recrutement").toLocalDate() : null
                    );
                    ac.setIdAccompagnateur(rs.getInt("id_accompagnateur"));
                    accompagnateurs.add(ac);
                }
                afficherAccompagnateurs(accompagnateurs);
            }
        }
        LOGGER.log(Level.INFO, "✅ {0} accompagnateurs avec statut ''{1}'' récupérés.", new Object[]{accompagnateurs.size(), statut});
        return accompagnateurs;
    }
}
