package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Area;
import ej.Tablas.Profesor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfesorDAO extends AbstractDAO<Profesor> {

    public ProfesorDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "profesor";
    }

    @Override
    protected Profesor mapRowToEntity(ResultSet rs) throws SQLException {
        Profesor profesor = new Profesor();
        profesor.setId(rs.getInt("id"));
        profesor.setNombre(rs.getString("nombre"));
        profesor.setDni(rs.getString("dni"));
        profesor.setApellidos(rs.getString("apellidos"));
        profesor.setClaveaccesoprof(rs.getString("claveaccesoprof"));
        profesor.setFoto(rs.getString("foto"));
        int areaId = rs.getInt("idarea");
        if (!rs.wasNull()) {
            Area area = new AreaDAO(connection).findById(areaId);
            profesor.setArea(area);
        }
        return profesor;
    }

    @Override
    public boolean insert(Profesor profesor) {
        String sql = "INSERT INTO profesor (nombre, apellidos, dni, foto, idarea, claveaccesoprof, isOrientador) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, profesor.getNombre());
            stmt.setString(2, profesor.getApellidos());
            stmt.setString(3, profesor.getDni());
            stmt.setString(4, profesor.getFoto());
            stmt.setInt(5, profesor.getArea().getId());
            stmt.setString(6, profesor.getClaveaccesoprof());
            stmt.setBoolean(7, profesor.isOrientador());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Profesor insertado correctamente.");
                result = true;
            } else {
                System.out.println("Error al insertar el profesor.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar el profesor: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean update(Profesor profesor) {
        String sql = "UPDATE profesor SET nombre = ?, apellidos = ?, dni = ?, foto = ?, idarea = ?, claveaccesoprof = ?, isOrientador = ? WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, profesor.getNombre());
            stmt.setString(2, profesor.getApellidos());
            stmt.setString(3, profesor.getDni());
            stmt.setString(4, profesor.getFoto());
            stmt.setInt(5, profesor.getArea().getId());
            stmt.setString(6, profesor.getClaveaccesoprof());
            stmt.setBoolean(7, profesor.isOrientador());
            stmt.setInt(8, profesor.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Profesor actualizado correctamente.");
                result = true;
            } else {
                System.out.println("Error al actualizar el profesor.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar el profesor: " + e.getMessage());
        }
        return result;
    }


    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM profesor WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Profesor con ID " + id + " ha sido eliminado correctamente.");
                result = true;
            } else {
                System.out.println("No se encontró un profesor con el ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el profesor: " + e.getMessage());
        }
        return result;
    }
}
