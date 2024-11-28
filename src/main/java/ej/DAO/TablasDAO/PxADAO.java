package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Area;
import ej.Tablas.Pregunta;
import ej.Tablas.PxA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PxADAO extends AbstractDAO<PxA> {

    public PxADAO(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "pxa";
    }

    @Override
    protected PxA mapRowToEntity(ResultSet rs) throws SQLException {
        PxA pxa = new PxA();
        pxa.setId(rs.getInt("id"));
        Area area = new AreaDAO(connection).findById(rs.getInt("idarea"));
        pxa.setArea(area);
        Pregunta pregunta = new PreguntaDAO(connection).findById(rs.getInt("idpregunta"));
        pxa.setPregunta(pregunta);
        return pxa;
    }

    @Override
    public boolean insert(PxA pxa) {
        String sql = "INSERT INTO pxa (idarea, idpregunta) VALUES (?, ?)";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pxa.getArea().getId());
            stmt.setInt(2, pxa.getPregunta().getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Pxa insertado correctamente.");
                result = true;
            } else {
                System.out.println("Error al insertar el Pxa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar el Pxa: " + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean update(PxA pxa) {
        String sql = "UPDATE pxa SET idarea = ?, idpregunta = ? WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pxa.getArea().getId());
            stmt.setInt(2, pxa.getPregunta().getId());
            stmt.setInt(3, pxa.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Pxa actualizado correctamente.");
                result = true;
            } else {
                System.out.println("Error al actualizar el Pxa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar el Pxa: " + e.getMessage());
        }
        return result;
    }


    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM pxa WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("PxA con ID " + id + " ha sido eliminado correctamente.");
                result = true;
            } else {
                System.out.println("No se encontró un PxA con el ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el PxA: " + e.getMessage());
        }
        return result;
    }
}
