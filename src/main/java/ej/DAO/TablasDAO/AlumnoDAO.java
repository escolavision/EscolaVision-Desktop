package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Alumno;
import ej.Tablas.Profesor;

import java.sql.Connection;
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
}
