/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Campus;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class CampusDaoSQL extends AbstractDaoSQL<Campus> {

    public CampusDaoSQL() {
        super();
    }

    @Override
    public void save(Campus o) {
        if (o.getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<Campus> list() {
        return this.list(null);
    }

    @Override
    public List<Campus> list(Object ref) {
        return this.list(ref.toString(), ref);
    }

    @Override
    public List<Campus> list(String criteria, Object ref) {
        String sql = "SELECT c FROM Campus c ";

        if (!"".equals(criteria)) {
            sql += " WHERE c.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY c.nome ";

        TypedQuery query = entityManager.createQuery(sql, Campus.class);
        List<Campus> list = query.getResultList();
        return list;
    }

    @Override
    public Campus findById(Object id) {
        return entityManager.find(Campus.class, id);
    }

    @Override
    public Campus findById(Object... ids) {
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
