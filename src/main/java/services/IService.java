package services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T>{
    void ajoutreAccompagnateur(T t) throws SQLException;

    void modifierAccompagnateur(T t,int id) throws SQLException;
    void supprimerAccompagnateur(int id_accompagnateur) throws SQLException;
    List<T> recupererAccompagnateur() throws SQLException;

}
