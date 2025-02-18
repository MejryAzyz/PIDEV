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
    public void modifier(Clinique clinique) throws SQLException {
        String sql = "UPDATE clinique SET nom = ? , adresse = ? , telephone = ? , email = ? , rate = ? , description = ? , prix = ? WHERE id_clinique = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, clinique.getNom());
        st.setString(2, clinique.getAdresse());
        st.setString(3, clinique.getTelephone());
        st.setString(4, clinique.getEmail());
        st.setInt(5, clinique.getRate());
        st.setString(6, clinique.getDescription());
        st.setDouble(7, clinique.getPrix());
        st.setInt(8, clinique.getIdClinique());
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
    public List<String> getAllCliniqueNames() throws SQLException {
        List<String> cliniques = new ArrayList<>();
        String sql = "SELECT nom FROM clinique";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            cliniques.add(rs.getString("nom"));
        }
        return cliniques;
    }

    public int getIdByName(String nom) throws SQLException {
        String sql = "SELECT id_clinique FROM clinique WHERE nom = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, nom);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_clinique");
        }
        throw new SQLException("Clinique non trouvée");
    }

    public String getNameById(int id_clinique) throws SQLException {
        String sql = "SELECT nom FROM specialite WHERE id_clinique = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id_clinique);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("nom");
        }
        return "pas dipsonible";
    }
}
