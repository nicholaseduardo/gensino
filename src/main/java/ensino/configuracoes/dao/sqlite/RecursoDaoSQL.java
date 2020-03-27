/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Recurso;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class RecursoDaoSQL extends AbstractDaoSQL<Recurso> {

    private static RecursoDaoSQL instance = null;

    private RecursoDaoSQL() {
        super();
    }

    public static RecursoDaoSQL getInstance() {
        if (instance == null) {
            instance = new RecursoDaoSQL();
        }
        return instance;
    }

    @Override
    public void save(Recurso o) {
        if (o.getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<Recurso> list() {
        return this.list(null);
    }

    @Override
    public List<Recurso> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Recurso> list(String criteria, Object ref) {
        String sql = "SELECT c FROM Recurso c";

        if (!"".equals(criteria)) {
            sql += "WHERE id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY nome ";

        TypedQuery query = entityManager.createQuery(sql, Recurso.class);
        return query.getResultList();
    }

    @Override
    public Recurso findById(Object id) {
        return entityManager.find(Recurso.class, id);
    }

    @Override
    public Recurso findById(Object... ids) {
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
