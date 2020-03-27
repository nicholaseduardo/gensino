/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Docente;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class DocenteDaoSQL extends AbstractDaoSQL<Docente> {

    private static DocenteDaoSQL instance = null;

    private DocenteDaoSQL() {
        super();
    }

    public static DocenteDaoSQL getInstance() {
        if (instance == null) {
            instance = new DocenteDaoSQL();
        }
        return instance;
    }

    @Override
    public void save(Docente o) {
        if (o.getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<Docente> list() {
        return this.list(null);
    }

    @Override
    public List<Docente> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Docente> list(String criteria, Object ref) {
        String sql = "SELECT c FROM Docente c";

        if (!"".equals(criteria)) {
            sql += "WHERE id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY nome ";

        TypedQuery query = entityManager.createQuery(sql, Docente.class);
        return query.getResultList();
    }

    @Override
    public Docente findById(Object id) {
        return entityManager.find(Docente.class, id);
    }

    @Override
    public Docente findById(Object... ids) {
        return this.findById(ids[0]);
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
