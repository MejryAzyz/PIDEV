package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public final String url = "jdbc:mysql://localhost:3306/pidev";
    public final String user = "root";
    public final String pwd = "";

    private Connection cnx;

    public static MyDataBase MyDataBase;

    private MyDataBase(){
        try {
                cnx = DriverManager.getConnection(url, user, pwd);
            }
        catch(SQLException e)
            {
                System.err.println(e.getMessage());
            }
    }

    public static MyDataBase getInstance(){
        if(MyDataBase==null)
            MyDataBase = new MyDataBase();
        return MyDataBase;
    }

    public Connection getCnx() {
        return cnx;
    }
}
