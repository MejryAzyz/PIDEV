package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    public final String url = "jdbc:mysql://localhost:3377/medtravel";
    public final String user = "root";
    public final String password = "";
    private Connection cnx;
    private static DatabaseConnection DatabaseConnection;
    private  DatabaseConnection(){
        try {
            cnx = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
public static DatabaseConnection getInstance() {
    if (DatabaseConnection == null)
        DatabaseConnection = new DatabaseConnection();
    return DatabaseConnection;
}
public Connection getConnection() {
        return cnx;
}

}
