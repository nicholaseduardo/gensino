/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.CalendarioId;
import ensino.configuracoes.model.Campus;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class CalendarioDaoSQL extends AbstractDaoSQL<Calendario> {

    public CalendarioDaoSQL() {
        super();
    }

    private boolean exists(Calendario o) {
        Calendario c =  findById(o.getId().getAno(),
                o.getId().getCampus());
        return c != null;
    }

    @Override
    public void save(Calendario o) {
        if (!exists(o)) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<Calendario> list() {
        return this.list(null);
    }

    @Override
    public List<Calendario> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Calendario> list(String criteria, Object ref) {
        String sql = 
                "SELECT cal "
                + "FROM Calendario cal ";

        if (!"".equals(criteria)) {
            sql += "WHERE cal.id.ano > 0 " + criteria;
        }

        // order
        sql += " ORDER BY cal.id.campus.nome, cal.id.ano ";

        TypedQuery query = entityManager.createQuery(sql, Calendario.class);
        return query.getResultList();
    }

    @Override
    public Calendario findById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Calendario findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oAno = ids[0];
        if (!(oAno instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oCampus = ids[1];
        if (!(oCampus instanceof Campus)) {
            System.err.println("Segundo atributo deve ser Campus");
            return null;
        }
        CalendarioId pk = new CalendarioId((Integer) oAno, (Campus) oCampus);
        return entityManager.find(Calendario.class, pk);
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
