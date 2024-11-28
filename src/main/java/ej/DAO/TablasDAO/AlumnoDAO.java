package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Alumno;
import ej.Tablas.Profesor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlumnoDAO extends AbstractDAO<Alumno> {

    public AlumnoDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "alumno";
    }

    @Override
    protected Alumno mapRowToEntity(ResultSet rs) throws SQLException {
        Alumno alumno = new Alumno();
        alumno.setId(rs.getInt("id"));
        alumno.setNombre(rs.getString("nombre"));
        alumno.setDni(rs.getString("dni"));
        alumno.setApellidos(rs.getString("apellidos"));
        alumno.setClaveaccesoalumno(rs.getString("claveaccesoalum"));
        alumno.setFoto(rs.getString("foto"));
        int profesorId = rs.getInt("idprofesor");
        if (!rs.wasNull()) {
            Profesor profesor = new ProfesorDAO(connection).findById(profesorId);
            alumno.setProfesor(profesor);
        }
        return alumno;
    }

    @Override
    public boolean insert(Alumno alumno) {
        String sql = "INSERT INTO alumno (nombre, apellidos, dni, foto, claveaccesoalum, idprofesor) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellidos());
            stmt.setString(3, alumno.getDni());
            stmt.setString(4, alumno.getFoto());
            stmt.setString(5, alumno.getClaveaccesoalumno());

            if (alumno.getProfesor() != null) {
                stmt.setInt(6, alumno.getProfesor().getId());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Alumno " + alumno.getNombre() + " insertado correctamente.");
                result = true;
            } else {
                System.out.println("Error al insertar el alumno.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar el alumno: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean update(Alumno entity) {
        String sql = "UPDATE alumno SET nombre = ?, apellidos = ?, dni = ?, foto = ?, claveaccesoalum = ?, idprofesor = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getNombre());
            statement.setString(2, entity.getApellidos());
            statement.setString(3, entity.getDni());
            statement.setString(4, entity.getFoto());
            statement.setString(5, entity.getClaveaccesoalumno());
            statement.setInt(6, entity.getProfesor() != null ? entity.getProfesor().getId() : null);
            statement.setInt(7, entity.getId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM alumno WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("El alumno con ID " + id + "  ha sido eliminado correctamente.");
                result =  true;
            } else {
                System.out.println("No se encontró un alumno con el ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el alumno: " + e.getMessage());
        }
        return result;
    }
}
