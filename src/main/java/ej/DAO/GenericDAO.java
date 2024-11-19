package ej.DAO;

public interface GenericDAO<T> {
    T findById(int id);
    T findByColumn(String column, Object value);
}
