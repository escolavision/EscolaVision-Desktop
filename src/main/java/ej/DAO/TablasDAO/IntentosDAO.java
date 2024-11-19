package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Alumno;
import ej.Tablas.Intentos;
import ej.Tablas.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
