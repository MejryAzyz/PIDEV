package dao;

import entities.Accompagnateur;
import services.IService;
import utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccompagnateurDAO implements IService<Accompagnateur> {
    DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public void ajoutreAccompagnateur(Accompagnateur accompagnateur) throws SQLException {
      String sql = " INSERT INTO accompagnateur (fichier_cv, experience, langues,certifications,statut, date_recrutement)"+
              "VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setString(1,accompagnateur.getFichierCv());
        ps.setString(2,accompagnateur.getExperience());
        ps.setString(3,accompagnateur.getLangues());
        ps.setString(4,accompagnateur.getCertifications());
        ps.setString(5,accompagnateur.getStatut());
        ps.setString(6,accompagnateur.getDateRecrutement());
        ps.executeUpdate();
    }

    @Override
    public void modifierAccompagnateur(Accompagnateur accompagnateur,int id) throws SQLException {
        String sql = "UPDATE accompagnateur SET fichier_cv = ?, experience = ?, langues = ?, certifications = ?, statut = ?, date_recrutement = ? WHERE id_accompagnateur = ?";
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setString(1,accompagnateur.getFichierCv());
        ps.setString(2,accompagnateur.getExperience());
        ps.setString(3,accompagnateur.getLangues());
        ps.setString(4,accompagnateur.getCertifications());
        ps.setString(5,accompagnateur.getStatut());
        ps.setString(6,accompagnateur.getDateRecrutement());
        ps.setInt(7,id);
    }

    @Override
    public void supprimerAccompagnateur(int id_accompagnateur ) throws SQLException {
        String sql = "DELETE FROM accompagnateur WHERE id_accompagnateur =?";
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setInt(1,id_accompagnateur );
        ps.executeUpdate();
        System.out.println("Supprimer de accompagnateur");
    }

    @Override
    public List<Accompagnateur> recupererAccompagnateur() throws SQLException{
        String sql = "SELECT * FROM accompagnateur";
        Statement stm = db.getConnection().createStatement();
        ResultSet rs = stm.executeQuery(sql);
        List<Accompagnateur> accompagnateurs = new ArrayList<>();
        while(rs.next()){
            Accompagnateur ac = new Accompagnateur();
            ac.setId_accompagnateur(rs.getInt("id_accompagnateur"));
            ac.setFichierCv(rs.getString("fichier_cv"));
            ac.setExperience(rs.getString("experience"));
            ac.setLangues(rs.getString("langues"));
            ac.setCertifications(rs.getString("certifications"));
            ac.setStatut(rs.getString("statut"));
            ac.setDateRecrutement(rs.getString("date_recrutement"));
            accompagnateurs.add(ac);
        }
        return accompagnateurs;
    }
}
