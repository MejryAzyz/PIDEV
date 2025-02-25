package service;

import Models.Photo;
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
        String query = "INSERT INTO hebergement_photos (hebergement_id, photo_url) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, photo.getHebergementId());
            pstmt.setString(2, photo.getPhotoUrl());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void ajouter(Photo photo) throws SQLException {
        String sql = "INSERT INTO hebergement_photos (hebergement_id, photo_url) VALUES (?, ?)";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1, photo.getHebergementId());
        pst.setString(2, photo.getPhotoUrl());
        pst.executeUpdate();
        System.out.println("Photo ajoutée avec succès !");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM hebergement_photos WHERE id_photo = ?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1, id);
        pst.executeUpdate();
        System.out.println("Photo supprimée avec succès !");
    }

    @Override
    public void modifier(Photo photo) throws SQLException {
        String sql = "UPDATE hebergement_photos SET photo_url = ? WHERE id_photo = ?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setString(1, photo.getPhotoUrl());
        pst.setInt(2, photo.getIdPhoto());
        pst.executeUpdate();
        System.out.println("Photo modifiée avec succès !");
    }

    @Override
    public List<Photo> recuperer() throws SQLException {
        String sql = "SELECT * FROM hebergement_photos";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Photo> photos = new ArrayList<>();
        while (rs.next()) {
            Photo photo = new Photo();
            photo.setIdPhoto(rs.getInt("id_photo"));
            photo.setHebergementId(rs.getInt("hebergement_id"));
            photo.setPhotoUrl(rs.getString("photo_url"));
            photos.add(photo);
        }
        return photos;
    }
}
