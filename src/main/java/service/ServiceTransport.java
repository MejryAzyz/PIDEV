package service;

import Models.Transport;
import tools.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTransport implements IService<Transport> {

    private Connection cnx;

    public ServiceTransport() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Transport transport) throws SQLException {
        String sql = "INSERT INTO transport (type, capacite, tarif) VALUES (?, ?, ?)";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setString(1, transport.getType());
            ste.setInt(2, transport.getCapacite());
            ste.setDouble(3, transport.getTarif());
            ste.executeUpdate();
            System.out.println("Transport ajouté");
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM transport WHERE id_transport = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transport supprimé");
            } else {
                System.out.println("Aucun transport trouvé avec l'ID : " + id);
            }
        }
    }

    @Override
    public void modifier(Transport transport) throws SQLException {
        String query = "UPDATE transport SET type = ?, capacite = ?, tarif = ? WHERE id_transport = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, transport.getType());
            stmt.setInt(2, transport.getCapacite());
            stmt.setDouble(3, transport.getTarif());
            stmt.setInt(4, transport.getId_transport());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Transport modifié avec succès !");
            } else {
                System.out.println("Aucun transport trouvé avec l'ID : " + transport.getId_transport());
            }
        }
    }

    @Override
    public List<Transport> recuperer() throws SQLException {
        String sql = "SELECT * FROM transport";
        List<Transport> transports = new ArrayList<>();
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Transport transport = new Transport();
                transport.setId_transport(rs.getInt("id_transport"));
                transport.setType(rs.getString("type"));
                transport.setCapacite(rs.getInt("capacite"));
                transport.setTarif(rs.getDouble("tarif"));
                transports.add(transport);
            }
        }
        return transports;
    }

    public int getNombreTotalTransport() throws SQLException {
        String sql = "SELECT COUNT(*) FROM transport";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);  // Returns the total number of transports
            }
        }
        return 0;  // Return 0 if no transports are found or an error occurs
    }

    public double getTarifMoyenTransport() throws SQLException {
        String sql = "SELECT AVG(tarif) AS tarif_moyen FROM transport";
        try (PreparedStatement ste = cnx.prepareStatement(sql);
             ResultSet rs = ste.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("tarif_moyen");
            }
        }
        return 0.0;  // Return 0.0 if no data or an error occurs
    }

    public int getTotalCapacity() throws SQLException {
        String sql = "SELECT SUM(capacite) AS total_capacity FROM transport";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total_capacity");
            }
        }
        return 0;  // Return 0 if no data or an error occurs
    }
}