package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Area;
import ej.Tablas.Profesor;

import java.sql.Connection;
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
}
