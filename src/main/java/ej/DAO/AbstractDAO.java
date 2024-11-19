package ej.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDAO<T> implements GenericDAO<T> {

    protected Connection connection;

    public AbstractDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public T findById(int id) {
        return findByColumn("id", id);
    }

    @Override
    public T findByColumn(String column, Object value) {
        T entity = null;
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + column + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    entity = mapRowToEntity(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    protected abstract String getTableName();

    protected abstract T mapRowToEntity(ResultSet rs) throws SQLException;
}
