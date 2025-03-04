package DAO;

import Modeles.OffreEmplois;
import Services.IServices;
import Utils.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OffreEmploisDAO implements IServices<OffreEmplois> {
    DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public void ajouter(OffreEmplois offreEmplois) throws SQLException {
        String sql = "INSERT INTO offre_emploi (titre, description, type_poste, date_publication, image_url) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = db.getConnection().prepareStatement(sql);

        ps.setString(1, offreEmplois.getTitre());
        ps.setString(2, offreEmplois.getDescription());
        ps.setString(3, offreEmplois.getTypePoste());
        ps.setDate(4, Date.valueOf(LocalDate.now())); // Date actuelle
        ps.setString(5, offreEmplois.getImageURL()); // Ajout de l'URL de l'image

        ps.executeUpdate();
    }

    @Override
    public void modifier(OffreEmplois offreEmplois, int id) throws SQLException {
        String sql = "UPDATE offre_emploi SET titre = ?, description = ?, type_poste = ?, date_publication = ?, image_url = ? WHERE id = ?";
        PreparedStatement ps = db.getConnection().prepareStatement(sql);

        ps.setString(1, offreEmplois.getTitre());
        ps.setString(2, offreEmplois.getDescription());
        ps.setString(3, offreEmplois.getTypePoste());
        ps.setDate(4, Date.valueOf(offreEmplois.getDatePublication()));
        ps.setString(5, offreEmplois.getImageURL()); // Mise à jour de l'URL de l'image
        ps.setInt(6, id);

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("L'offre d'emploi a été mise à jour avec succès !");
        } else {
            System.out.println("Aucune offre trouvée avec l'ID : " + id);
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM offre_emploi WHERE id = ?";
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("L'offre a été supprimée avec succès.");
    }

    @Override
    public List<OffreEmplois> recuperer() throws SQLException {
        List<OffreEmplois> offres = new ArrayList<>();
        String sql = "SELECT id, titre, description, type_poste, date_publication, image_url FROM offre_emploi";
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            OffreEmplois offre = new OffreEmplois(
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getString("type_poste"),
                    rs.getDate("date_publication").toLocalDate(),
                    rs.getString("image_url") // Récupération de l'URL de l'image
            );
            offre.setId(rs.getInt("id"));
            offres.add(offre);
        }

        return offres;
    }
}
