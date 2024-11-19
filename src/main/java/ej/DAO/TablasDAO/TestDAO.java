package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDAO extends AbstractDAO<Test> {

    public TestDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "test";
    }

    @Override
    protected Test mapRowToEntity(ResultSet rs) throws SQLException {
        Test test = new Test();
        test.setId(rs.getInt("id"));
        test.setNombre(rs.getString("nombretest"));
        return test;
    }
}
