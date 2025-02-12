package service;
import Models.Transport;
import tools.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceTransport implements IService<Transport> {
    Connection cnx;
    public ServiceTransport() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Transport transport) throws SQLException {
        String sql = "INSERT INTO transport (type, capacite, tarif) VALUES (?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, transport.getType());
        ste.setInt(2, transport.getCapacite());
        ste.setDouble(3, transport.getTarif());

        ste.executeUpdate();
        System.out.println("Transport ajoutée");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "delete from transport where id_transport=?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1,id);
        pst.executeUpdate();
        System.out.println("Transport supprimée");
    }

    @Override
    public void modifier(int id, String type) throws SQLException {
        String sql = "update transport set type=? where id_transport=?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setString(1,type);
        pst.setInt(2,id);
        pst.executeUpdate();
        System.out.println("transport modifiée");
    }

    @Override
    public List<Transport> recuperer() throws SQLException {
        String sql = "select * from transport";
        Statement st = cnx.createStatement();
        ResultSet rs= st.executeQuery(sql);
        List<Transport> transports = new ArrayList<>();
        while (rs.next()) {
            Transport transport= new Transport();
            transport.setId_transport(rs.getInt("id_transport"));
            transport.setType(rs.getString("type"));
            transport.setCapacite(rs.getInt("capacite"));
            transport.setTarif(rs.getDouble("tarif"));

            transports.add(transport);
        }
        return transports;
}}
