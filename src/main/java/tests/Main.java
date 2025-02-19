package tests;
import entities.planning_acc;
import entities.planning_doc;
import services.planning_accService;
import services.planning_docService;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        planning_docService ps = new planning_docService();

        planning_doc p = new planning_doc(1, Date.valueOf("2025-02-19"), Time.valueOf("8:00:00"), Time.valueOf("9:00:00"));


        try
        {
            System.out.println(ps.retrieve());
            System.out.println(ps.checkExistence(p));
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }

    }
}