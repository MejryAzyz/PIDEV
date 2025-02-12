package tools;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class MyDataBase {
    public final String url = "jdbc:mysql://localhost:3306/pidev";
    public final String user = "root";
    public final String password = "";
    private Connection cnx;
    private static MyDataBase myDataBase;
    private MyDataBase() {
        try{
        cnx = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
        }
    }
    public static MyDataBase getInstance() {
        if (myDataBase == null) {
            myDataBase = new MyDataBase();
        }
        return myDataBase;
    }

    public Connection getCnx() {
        return cnx;
    }
}
