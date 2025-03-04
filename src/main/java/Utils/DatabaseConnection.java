package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url = "jdbc:mysql://localhost:3377/medtravel";
    private final String user = "root";
    private final String password = "";

    // Instance unique de DatabaseConnection (singleton)
    private static DatabaseConnection instance;

    // Constructeur privé pour empêcher l'instanciation directe
    private DatabaseConnection() {
        // Chargement du pilote JDBC (optionnel pour les versions récentes de JDBC)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Pilote JDBC introuvable : " + e.getMessage());
        }
    }

    // Méthode pour obtenir l'instance unique
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Méthode pour obtenir une nouvelle connexion
    public Connection getConnection() throws SQLException {
        // Crée une nouvelle connexion à chaque appel
        return DriverManager.getConnection(url, user, password);
    }

    // Méthode pour fermer une connexion
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Connexion fermée avec succès.");
            } catch (SQLException e) {
                System.err.println("❌ Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }

    // Méthode pour vérifier si une connexion est valide
    public boolean isConnectionValid(Connection connection) {
        if (connection != null) {
            try {
                return connection.isValid(2); // Vérifie si la connexion est valide pendant 2 secondes
            } catch (SQLException e) {
                System.err.println("❌ Erreur lors de la validation de la connexion : " + e.getMessage());
            }
        }
        return false;
    }
}