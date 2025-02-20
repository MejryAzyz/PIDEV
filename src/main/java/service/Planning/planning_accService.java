package service.Planning;

import Models.Planning.planning_acc;
import service.IService;
import tools.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class planning_accService implements IService<planning_acc> {

    Connection cnx;
    public planning_accService()
    {
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    public List<planning_acc> recuperer() throws SQLException{

        List<planning_acc> list = new ArrayList<>();
        String sql = "select * from planning_accompagnateur";
        PreparedStatement st = cnx.prepareStatement(sql);
        ResultSet rs = st.executeQuery(sql);

        while(rs.next())
        {
            planning_acc p = new planning_acc();
            p.setId_acc(rs.getInt("id_accompagnateur"));
            p.setDate_j(rs.getDate("date_jour"));
            p.setH_deb(rs.getTime("heure_debut"));
            p.setH_fin(rs.getTime("heure_fin"));

            list.add(p);
        }

        return list;
    }

    public int checkExistence(planning_acc p)
    {
        try{
            List<planning_acc> list = recuperer();
            if(list.contains(p))
                return 1;
            else
                return 0;
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public void ajouter(planning_acc p) throws SQLException
    {
        if(checkExistence(p)==0)
        {
            String sql ="insert into planning_accompagnateur(id_accompagnateur,date_jour,heure_debut,heure_fin)"+"values(?,?,?,?)";
            PreparedStatement st = cnx.prepareStatement(sql);
            st.setInt(1,p.getId_acc());
            st.setDate(2,p.getDate_j());
            st.setTime(3,p.getH_deb());
            st.setTime(4,p.getH_fin());

            st.executeUpdate();
            System.out.println("planning entry added");
        }
        else if (checkExistence(p)==1)
            System.out.println("planning entry not added, time slot already taken");
        else
            System.out.println("impossible to establish connection with database");
    }

    public void supprimer(int id) throws SQLException{
        String sql = "delete from planning_accompagnateur " + "where id_planning=" + id + ";";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.executeUpdate();
        System.out.println("planning "+id+" deleted");
    }

    @Override
    public void modifier(planning_acc planningAcc) throws SQLException {

    }

    public void modifier(int id,planning_acc p) throws SQLException{
        String sql = "update planning_accompagnateur " + "set id_accompagnateur=" + p.getId_acc() +
                     ",date_jour='" + p.getDate_j() + "',heure_debut='" + p.getH_deb() + "',heure_fin='" + p.getH_fin()
                     + "' where id_planning=" +id +";";

        PreparedStatement st = cnx.prepareStatement(sql);
        st.executeUpdate();
        System.out.println("planning "+id+" updated");
    }


}
