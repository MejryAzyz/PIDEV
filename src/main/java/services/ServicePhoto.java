package services;

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
}
