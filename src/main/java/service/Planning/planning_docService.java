package service.Planning;

import Models.Planning.planning_doc;
import service.IService;
import tools.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class planning_docService implements IService<planning_doc> {

    Connection cnx;
    public planning_docService()
    {
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    //***************************************************************************************************************

    public List<planning_doc> recuperer() throws SQLException{

        List<planning_doc> list = new ArrayList<>();
        String sql = "select * from planning_docteur";
        PreparedStatement st = cnx.prepareStatement(sql);
        ResultSet rs = st.executeQuery(sql);

        while(rs.next())
        {
            planning_doc p = new planning_doc();
            p.setId_planning(rs.getInt("id_planning"));
            p.setId_doc(rs.getInt("id_docteur"));
            p.setDate_j(rs.getDate("date_jour"));
            p.setH_deb(rs.getTime("heure_debut"));
            p.setH_fin(rs.getTime("heure_fin"));

            list.add(p);
        }

        return list;
    }
    //***************************************************************************************************************

    public int checkExistence(planning_doc p)
    {
        try{
            List<planning_doc> list = recuperer();
            if(list.contains(p))
                return p.getId_planning();
            else
                return 0;
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    //***************************************************************************************************************

    public void ajouter(planning_doc p) throws SQLException
    {


            String sql ="insert into planning_docteur(id_docteur,date_jour,heure_debut,heure_fin)"+"values(?,?,?,?)";
            PreparedStatement st = cnx.prepareStatement(sql);
            st.setInt(1,p.getId_doc());
            st.setDate(2,p.getDate_j());
            st.setTime(3,p.getH_deb());
            st.setTime(4,p.getH_fin());

            st.executeUpdate();
            System.out.println("planning entry added");

    }

    //***************************************************************************************************************

    public void supprimer(int id) throws SQLException{
        String sql = "delete from planning_docteur " + "where id_planning=" + id + ";";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.executeUpdate();
        System.out.println("planning "+id+" deleted");
    }

    @Override
    public void modifier(planning_doc planningDoc) throws SQLException {

    }

    //***************************************************************************************************************

    public void modifier(int id,planning_doc p) throws SQLException{
        String sql = "update planning_docteur " + "set id_docteur=" + p.getId_doc() +
                ",date_jour='" + p.getDate_j() + "',heure_debut='" + p.getH_deb() + "',heure_fin='" + p.getH_fin()
                + "' where id_planning=" +id +";";

        PreparedStatement st = cnx.prepareStatement(sql);
        st.executeUpdate();
        System.out.println("planning "+id+" updated");
    }

    //***************************************************************************************************************


    public List<Integer> retrieveIdDoc() throws SQLException{
        List<Integer> list = new ArrayList<>();
        String sql = "select id_docteur from docteur";
        PreparedStatement st = cnx.prepareStatement(sql);
        ResultSet rs = st.executeQuery(sql);

        while(rs.next())
        {
            list.add(rs.getInt("id_docteur"));
        }

        return list;
    }
}
