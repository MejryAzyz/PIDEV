package services;

import models.Clinique;
import tools.MyDataBase;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceClinique implements IService<Clinique>{

    Connection cnx;
    public ServiceClinique() {
        cnx = MyDataBase.getInstance().getCnx();
    }



    @Override
    public void ajouter(Clinique c) throws SQLException {
        /*String sql = "INSERT INTO clinique (nom, adresse, telephone, email, rate, description, prix)" +
                "VALUES ('"+c.getNom()+"','"+c.getAdresse()+"' , '"+c.getTelephone()+"', '"+c.getEmail()+"', "+c.getRate()+", '"+c.getDescription()+"', "+c.getPrix()+")";
        Statement st = cnx.createStatement();
        st.executeUpdate(sql);*/

        String sql = "INSERT INTO clinique (nom, adresse, telephone, email, rate, description, prix) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, c.getNom());
        ste.setString(2, c.getAdresse());
        ste.setString(3, c.getTelephone());
        ste.setString(4, c.getEmail());
        ste.setDouble(5, c.getRate());
        ste.setString(6, c.getDescription());
        ste.setDouble(7, c.getPrix());
        ste.executeUpdate();
        System.out.println("Clinique ajoutée");

    }

    @Override
    public void supprimer(int id_clinique) throws SQLException {
        String sql = "DELETE FROM clinique WHERE id_clinique = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id_clinique);
        st.executeUpdate();
        System.out.println("Clinique supprimée");
    }

    @Override
    public void modifier(int id_clinique, String nom) throws SQLException {
        String sql = "UPDATE clinique SET nom = ? WHERE id_clinique = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, nom);
        st.setInt(2, id_clinique);
        st.executeUpdate();
        System.out.println("Clinique modifiée");

    }

    @Override
    public List<Clinique> recuperer() throws SQLException {
        String sql = "SELECT * FROM clinique";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<Clinique> cliniques = new ArrayList<>();
        while (rs.next()) {
            Clinique c = new Clinique();
            c.setIdClinique(rs.getInt("id_clinique"));
            c.setNom(rs.getString("nom"));
            c.setAdresse(rs.getString("adresse"));
            c.setTelephone(rs.getString("telephone"));
            c.setEmail(rs.getString("email"));
            c.setDescription(rs.getString("description"));
            c.setPrix(rs.getDouble("prix"));
            c.setRate(rs.getInt("Rate"));
            cliniques.add(c);
        }
        return cliniques;
    }
}
