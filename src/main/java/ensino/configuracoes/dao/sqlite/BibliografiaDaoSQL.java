/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Bibliografia;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class BibliografiaDaoSQL extends AbstractDaoSQL<Bibliografia> {

    private static BibliografiaDaoSQL instance = null;

    private BibliografiaDaoSQL() {
        super();
    }

    public static BibliografiaDaoSQL getInstance() {
        if (instance == null) {
            instance = new BibliografiaDaoSQL();
        }
        return instance;
    }

    @Override
    public void save(Bibliografia o) {
        if (o.getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<Bibliografia> list() {
        return this.list(null);
    }

    @Override
    public List<Bibliografia> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Bibliografia> list(String criteria, Object ref) {
        String sql = "SELECT b FROM Bibliografia b ";

        if (!"".equals(criteria)) {
            sql += " WHERE b.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY b.titulo ";

        TypedQuery query = entityManager.createQuery(sql, Bibliografia.class);
        return query.getResultList();
    }

    @Override
    public Bibliografia findById(Object id) {
        return entityManager.find(Bibliografia.class, id);
    }

    @Override
    public Bibliografia findById(Object... ids) {
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
