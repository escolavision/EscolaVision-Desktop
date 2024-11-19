package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Pregunta;
import ej.Tablas.Test;

import java.sql.Connection;
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
        return pregunta;
    }
}
