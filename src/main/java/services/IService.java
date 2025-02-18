package services;

import entities.planning_acc;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void add(T x) throws SQLException;
    void delete(int id) throws SQLException;
    void update(int id, T p) throws SQLException;
    List<T> retrieve() throws SQLException;
}
