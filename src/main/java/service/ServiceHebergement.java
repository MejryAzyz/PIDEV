package service;

import Models.Hebergement;
import Models.Transport;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceHebergement implements IService<Hebergement> {
Connection cnx;
public ServiceHebergement() {
    cnx = MyDataBase.getInstance().getCnx();
}
    @Override
    public void ajouter(Hebergement hebergement) throws SQLException {
        String sql = "insert into hebergement(nom,adresse,capacite,email,telephone,tarif_nuit)"+
                "values('"+hebergement.getNom()+"','"+hebergement.getAdresse()+"','"+hebergement.getCapacite()+"','"+hebergement.getEmail()+"','"+hebergement.getTelephone()+"',"+hebergement.getTarif_nuit()+")";
        Statement st = cnx.createStatement();
        st.executeUpdate(sql);
}

    @Override
    public void supprimer(int id) throws SQLException {
    String sql = "delete from hebergement where id_hebergement=?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1,id);
        pst.executeUpdate();
        System.out.println("Hebergement supprimée");
    }
    public int getNombreTotalHebergements() throws SQLException {
        String sql = "SELECT COUNT(*) FROM hebergement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
    public int getCapaciteTotale() throws SQLException {
        String sql = "SELECT SUM(capacite) FROM hebergement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public double getTarifMoyenParNuit() throws SQLException {
        String sql = "SELECT AVG(tarif_nuit) FROM hebergement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getDouble(1);
        }
        return 0.0;
    }
    @Override
    public void modifier(Hebergement hebergement) throws SQLException {
        String sql = "UPDATE hebergement SET nom = ?, telephone = ?, capacite = ?, adresse = ?, email = ?, tarif_nuit = ? WHERE id_hebergement = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, hebergement.getNom());
        ps.setInt(2, hebergement.getTelephone());
        ps.setInt(3, hebergement.getCapacite());
        ps.setString(4, hebergement.getAdresse());
        ps.setString(5, hebergement.getEmail());
        ps.setDouble(6, hebergement.getTarif_nuit());
        ps.setInt(7, hebergement.getId_hebergement());
        ps.executeUpdate();
        System.out.println("Hébergement modifié avec succès !");
    }



    @Override
    public List<Hebergement> recuperer() throws SQLException {
        String sql = "select * from hebergement";
        Statement st = cnx.createStatement();
        ResultSet rs= st.executeQuery(sql);
        List<Hebergement> hebergements = new ArrayList<>();
        while (rs.next()) {
            Hebergement hebergement= new Hebergement();
            hebergement.setId_hebergement(rs.getInt("id_hebergement"));
            hebergement.setNom(rs.getString("nom"));
            hebergement.setAdresse(rs.getString("adresse"));
            hebergement.setCapacite(rs.getInt("capacite"));
            hebergement.setEmail(rs.getString("email"));
            hebergement.setTelephone(rs.getInt("telephone"));
            hebergement.setTarif_nuit(rs.getInt("tarif_nuit"));
            hebergements.add(hebergement);
        }
        return hebergements;
    }

}
