package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Alumno;
import ej.Tablas.Intentos;
import ej.Tablas.Test;
import java.sql.*;

public class IntentosDAO extends AbstractDAO<Intentos> {

    public IntentosDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "intentos";
    }

    @Override
    protected Intentos mapRowToEntity(ResultSet rs) throws SQLException {
        Intentos intento = new Intentos();
        intento.setId(rs.getInt("id"));
        Test test = new TestDAO(connection).findById(rs.getInt("idtest"));
        intento.setTest(test);
        Alumno alumno = new AlumnoDAO(connection).findById(rs.getInt("idalumno"));
        intento.setAlumno(alumno);
        intento.setFecha(rs.getString("fecha"));
        intento.setHora(rs.getString("hora"));
        intento.setResultados(rs.getString("resultados"));
        return intento;
    }

    @Override
    public boolean insert(Intentos intento) {
        String sql = "INSERT INTO intentos (idtest, idalumno, fecha, hora, resultados) "
                + "VALUES (?, ?, ?, ?, ?)";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, intento.getTest().getId());
            stmt.setInt(2, intento.getAlumno().getId());
            stmt.setDate(3, Date.valueOf(intento.getFecha()));
            stmt.setString(4, intento.getHora());
            stmt.setString(5, intento.getResultados());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Intento insertado correctamente.");
                result = true;
            } else {
                System.out.println("Error al insertar el intento.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar el intento: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean update(Intentos intento) {
        String sql = "UPDATE intentos  SET idtest = ?, idalumno = ?, fecha = ?, hora = ?, resultados = ? WHERE id = ? ";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, intento.getTest().getId());
            stmt.setInt(2, intento.getAlumno().getId());
            stmt.setDate(3, Date.valueOf(intento.getFecha()));
            stmt.setString(4, intento.getHora());
            stmt.setString(5, intento.getResultados());
            stmt.setInt(6, intento.getId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM intentos WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Intento con ID " + id + " ha sido eliminado correctamente.");
                result = true;
            } else {
                System.out.println("No se encontró un intento con el ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el intenti: " + e.getMessage());
        }
        return result;
    }
}
