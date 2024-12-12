package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Pregunta;
import ej.Tablas.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreguntaDAO extends AbstractDAO<Pregunta> {
    
    public PreguntaDAO(Connection connection) {
        super(connection);
    }
    
    @Override
    protected String getTableName() {
        return "pregunta";
    }
    
    @Override
    protected Pregunta mapRowToEntity(ResultSet rs) throws SQLException {
        Pregunta pregunta = new Pregunta();
        pregunta.setId(rs.getInt("id"));
        Test test = new TestDAO(connection).findById(rs.getInt("idtest"));
        pregunta.setTest(test);
        pregunta.setEnunciado(rs.getString("enunciado"));
        pregunta.setTitulo(rs.getString("titulo"));
        return pregunta;
    }
    
    @Override
    public boolean insert(Pregunta pregunta) {
        String sql = "INSERT INTO pregunta (idtest, enunciado, titulo) VALUES (?, ?, ?)";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pregunta.getTest().getId());
            stmt.setString(2, pregunta.getEnunciado());
            stmt.setString(3, pregunta.getTitulo());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pregunta insertada correctamente.");
                result = true;
            } else {
                System.out.println("Error al insertar la pregunta.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar la pregunta: " + e.getMessage());
        }
        return result;
    }
    
    @Override
    public boolean update(Pregunta pregunta) {
        String sql = "UPDATE pregunta SET idtest = ?, enunciado = ?, titulo = ? WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pregunta.getTest().getId());
            stmt.setString(2, pregunta.getEnunciado());
            stmt.setString(3, pregunta.getTitulo());
            stmt.setInt(4, pregunta.getId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pregunta actualizada correctamente.");
                result = true;
            } else {
                System.out.println("Error al actualizar la pregunta.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar la pregunta: " + e.getMessage());
        }
        return result;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM pregunta WHERE id = ?";
        boolean result = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Pregunta con ID " + id + " ha sido eliminado correctamente.");
                result = true;
            } else {
                System.out.println("No se encontró una pregunta con el ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la pregunta con el ID "+id+".");
        }
        return result;
    }
}