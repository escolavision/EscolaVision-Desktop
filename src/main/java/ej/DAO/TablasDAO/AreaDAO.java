package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Area;

import java.sql.Connection;
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
}

