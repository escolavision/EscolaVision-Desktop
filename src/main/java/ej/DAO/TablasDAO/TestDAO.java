package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    @Override
    public boolean insert(Test test) {
        String sql = "INSERT INTO test (nombretest) VALUES (?)";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, test.getNombre());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Test insertado correctamente.");
                result = true;
            } else {
                System.out.println("Error al insertar el test.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar el test: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean update(Test test) {
        String sql = "UPDATE test SET nombretest = ? WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, test.getNombre());
            stmt.setInt(2, test.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Test actualizado correctamente.");
                result = true;
            } else {
                System.out.println("Error al actualizar el test.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar el test: " + e.getMessage());
        }
        return result;
    }


    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM test WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Test con ID " + id + " ha sido eliminado correctamente.");
                result = true;
            } else {
                System.out.println("No se encontró un test con el ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el test: " + e.getMessage());
        }
        return result;
    }
}
