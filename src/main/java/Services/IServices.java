package Services;

import java.sql.SQLException;
import java.util.List;

public interface IServices <T>{
    void ajouter(T t) throws SQLException;
    void modifier(T t,int id) throws SQLException;
    void supprimer(int id) throws SQLException;
    List<T> recuperer() throws SQLException;
}
