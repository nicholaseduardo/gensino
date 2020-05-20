/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.NivelEnsino;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class NivelEnsinoDaoSQL extends AbstractDaoSQL<NivelEnsino> {

    private static NivelEnsinoDaoSQL instance = null;

    private NivelEnsinoDaoSQL() {
        super();
    }

    public static NivelEnsinoDaoSQL getInstance() {
        if (instance == null) {
            instance = new NivelEnsinoDaoSQL();
        }
        return instance;
    }

    @Override
    public void save(NivelEnsino o) {
        if (o.getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<NivelEnsino> list() {
        return this.list(null);
    }

    @Override
    public List<NivelEnsino> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<NivelEnsino> list(String criteria, Object ref) {
        String sql = "SELECT ne FROM NivelEnsino ne";

        if (!"".equals(criteria)) {
            sql += "WHERE ne.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY ne.nome ";

        TypedQuery query = entityManager.createQuery(sql, NivelEnsino.class);
        return query.getResultList();
    }

    @Override
    public NivelEnsino findById(Object id) {
        return entityManager.find(NivelEnsino.class, id);
    }

    @Override
    public NivelEnsino findById(Object... ids) {
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
