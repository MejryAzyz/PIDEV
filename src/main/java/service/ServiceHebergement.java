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

    @Override
    public void modifier(int id, String nom) throws SQLException {
    String sql = "update hebergement set nom=? where id_hebergement=?";
    PreparedStatement pst = cnx.prepareStatement(sql);
    pst.setString(1,nom);
    pst.setInt(2,id);
    pst.executeUpdate();
        System.out.println("hebergement modifiée");
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
