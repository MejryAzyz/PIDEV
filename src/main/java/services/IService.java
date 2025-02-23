package services;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IService <T> {
    void ajouter(T t) throws SQLException, IOException;
    void supprimer (int id ) throws SQLException;
    void modifier(T t) throws SQLException;

    List<T> recuperer() throws SQLException;
}
