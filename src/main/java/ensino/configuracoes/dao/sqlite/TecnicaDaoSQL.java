/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Tecnica;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class TecnicaDaoSQL extends AbstractDaoSQL<Tecnica> {

    private static TecnicaDaoSQL instance = null;

    private TecnicaDaoSQL() {
        super();
    }

    public static TecnicaDaoSQL getInstance() {
        if (instance == null) {
            instance = new TecnicaDaoSQL();
        }
        return instance;
    }

    @Override
    public void save(Tecnica o) {
        if (o.getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<Tecnica> list() {
        return this.list(null);
    }

    @Override
    public List<Tecnica> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Tecnica> list(String criteria, Object ref) {
        String sql = "SELECT c FROM Tecnica c";

        if (!"".equals(criteria)) {
            sql += "WHERE id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY nome ";

        TypedQuery query = entityManager.createQuery(sql, Tecnica.class);
        return query.getResultList();
    }

    @Override
    public Tecnica findById(Object id) {
        return entityManager.find(Tecnica.class, id);
    }

    @Override
    public Tecnica findById(Object... ids) {
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
