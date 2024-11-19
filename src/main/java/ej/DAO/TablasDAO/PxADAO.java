package ej.DAO.TablasDAO;

import ej.DAO.AbstractDAO;
import ej.Tablas.Area;
import ej.Tablas.Pregunta;
import ej.Tablas.PxA;

import java.sql.Connection;
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
}
