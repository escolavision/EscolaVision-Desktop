package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Area;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AreaDAO extends AbstractDAO<Area> {

    public AreaDAO(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "area";
    }

    @Override
    protected Area mapRowToEntity(ResultSet rs) throws SQLException {
        Area area = new Area();
        area.setId(rs.getInt("id"));
        area.setNombre(rs.getString("nombre"));
        area.setDescripcion(rs.getString("descripción"));
        area.setLogo(rs.getString("logo"));
        return area;
    }

    @Override
    public boolean insert(Area area) {
        String sql = "INSERT INTO area (nombre, descripción, logo) VALUES (?, ?, ?)";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, area.getNombre());
            stmt.setString(2, area.getDescripcion());
            stmt.setString(3, area.getLogo());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Área " + area.getNombre() + " insertada correctamente.");
                result = true;
            } else {
                System.out.println("Error al insertar el área.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar el área: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean update(Area area) {
        String sql = "UPDATE area SET nombre = ?, descripción = ?, logo = ? WHERE id = ?";

        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, area.getNombre());
            stmt.setString(2, area.getDescripcion());
            stmt.setString(3, area.getLogo());
            stmt.setInt(4, area.getId());

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM area WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Area con ID " + id + " ha sido eliminado correctamente.");
                result = true;
            } else {
                System.out.println("No se encontró un area con el ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el area: " + e.getMessage());
        }
        return result;
    }
}

