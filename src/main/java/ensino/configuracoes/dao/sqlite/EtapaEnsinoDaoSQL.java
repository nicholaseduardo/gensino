/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.EtapaEnsino;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class EtapaEnsinoDaoSQL extends AbstractDaoSQL<EtapaEnsino> {

    private static EtapaEnsinoDaoSQL instance = null;

    private EtapaEnsinoDaoSQL() {
        super();
    }

    public static EtapaEnsinoDaoSQL getInstance() {
        if (instance == null) {
            instance = new EtapaEnsinoDaoSQL();
        }
        return instance;
    }

    @Override
    public void save(EtapaEnsino o) {
        if (o.getId().getId() == null) {
            o.getId().setId(nextVal(o));
        }
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(EtapaEnsino o) {
        entityManager.remove(entityManager.getReference(EtapaEnsino.class, o));
    }

    @Override
    public List<EtapaEnsino> list() {
        return this.list(null);
    }

    @Override
    public List<EtapaEnsino> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<EtapaEnsino> list(String criteria, Object ref) {
        String sql = "SELECT ee FROM EtapaEnsino ee ";

        if (!"".equals(criteria)) {
            sql += " WHERE ee.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY ee.id.id ";

        TypedQuery query = entityManager.createQuery(sql, EtapaEnsino.class);
        return query.getResultList();
    }

    @Override
    public EtapaEnsino findById(Object id) {
        return entityManager.find(EtapaEnsino.class, id);
    }

    @Override
    public EtapaEnsino findById(Object... ids) {
        return this.findById(ids[0]);
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        EtapaEnsino o = (EtapaEnsino) params[0];
        List<EtapaEnsino> l = o.getNivelEnsino().getEtapas();
        if (!l.isEmpty()) {
            return l.get(l.size() - 1).getId().getId() + 1;
        }
        return 1;
    }

}
