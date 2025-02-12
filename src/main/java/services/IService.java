package services;
import java.sql.SQLException;
import java.util.List;


public interface IService<T> {
    void ajouter(T c) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(int id,String nom) throws SQLException;

    List<T> recuperer() throws SQLException;
}
