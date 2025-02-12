package service;

import Models.Transport;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter(T t) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(int id, String nom) throws SQLException;
    List<T> recuperer() throws SQLException;


}
