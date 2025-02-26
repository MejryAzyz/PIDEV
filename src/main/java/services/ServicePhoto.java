package services;

import models.Clinique;
import models.Photo;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePhoto implements IService<Photo> {
    private final Connection cnx;

    public ServicePhoto() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public void ajouterPhoto(Photo photo, Connection conn) throws SQLException {
        String query = "INSERT INTO clinique_photos (clinique_id, photo_url) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, photo.getId_clinique());
            pstmt.setString(2, photo.getPhotoUrl());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void ajouter(Photo photo) throws SQLException {
        String sql = "INSERT INTO clinique_photos (clinique_id, photo_url) VALUES (?, ?)";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1, photo.getId_clinique());
        pst.setString(2, photo.getPhotoUrl());
        pst.executeUpdate();
        System.out.println("Photo ajoutée avec succès !");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM clinique_photos WHERE id_photo = ?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1, id);
        pst.executeUpdate();
        System.out.println("Photo supprimée avec succès !");
    }

    @Override
    public void modifier(Photo photo) throws SQLException {
        String sql = "UPDATE clinique_photos SET photo_url = ? WHERE id_photo = ?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setString(1, photo.getPhotoUrl());
        pst.setInt(2, photo.getId_photo());
        pst.executeUpdate();
        System.out.println("Photo modifiée avec succès !");
    }

    @Override
    public List<Photo> recuperer() throws SQLException {
        String sql = "SELECT * FROM clinique_photos";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Photo> photos = new ArrayList<>();
        while (rs.next()) {
            Photo photo = new Photo();
            photo.setId_photo(rs.getInt("id_photo"));
            photo.setId_clinique(rs.getInt("clinique_id"));
            photo.setPhotoUrl(rs.getString("photo_url"));
            photos.add(photo);
        }
        return photos;
    }




    public void ajouterPhoto(Photo photo) throws SQLException {
        String sql = "INSERT INTO clinique_photos (clinique_id, photo_url) VALUES (?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, photo.getId_clinique()); // Utiliser l'ID de la clinique
        ps.setString(2, photo.getPhotoUrl()); // L'URL de la photo
        ps.executeUpdate();
        System.out.println("Photo ajoutée pour la clinique ID : " + photo.getId_clinique());
    }

    public void modifierPhoto(int idClinique, String nouveauPath) {
        String query = "UPDATE clinique_photos SET photo_url = ? WHERE clinique_id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, nouveauPath);
            pst.setInt(2, idClinique);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Photo mise à jour avec succès !");
            } else {
                System.out.println("Aucune photo trouvée pour cette clinique.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupère l'URL de l'image de la clinique à partir de la table clinique_photos
    public String getPhotoUrlByCliniqueId(int cliniqueId) throws SQLException {
        String photoUrl = null;
        String sql = "SELECT photo_url FROM clinique_photos WHERE clinique_id = ? LIMIT 1";

        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1, cliniqueId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            photoUrl = rs.getString("photo_url");
        }

        return photoUrl;  // Renvoie l'URL de l'image
    }

    public List<Photo> recupererPhotosParClinique(int idClinique) throws SQLException {
        String sqlPhotos = "SELECT * FROM clinique_photos WHERE clinique_id = ?";
        PreparedStatement ps = cnx.prepareStatement(sqlPhotos);
        ps.setInt(1, idClinique);
        ResultSet rsPhotos = ps.executeQuery();

        List<Photo> photos = new ArrayList<>();

        while (rsPhotos.next()) {
            Photo photo = new Photo();
            photo.setId_photo(rsPhotos.getInt("id_photo"));
            photo.setId_clinique(rsPhotos.getInt("clinique_id"));
            photo.setPhotoUrl(rsPhotos.getString("photo_url"));
            photos.add(photo);
        }

        return photos;
    }

    public String getPhotoUrlByClinique(int idClinique) throws SQLException {
        String url = null;  // Initialiser la variable url
        String query = "SELECT photo_url FROM clinique_photos WHERE clinique_id = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, idClinique);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                url = rs.getString("photo_url");
            }
        }
        return url;  // Retourner l'URL de l'image sous forme de String
    }
}
