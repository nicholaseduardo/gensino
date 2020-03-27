/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class PlanoDeEnsinoDaoSQL extends AbstractDaoSQL<PlanoDeEnsino> {

    public PlanoDeEnsinoDaoSQL() {
        super();
    }

    @Override
    public void save(PlanoDeEnsino o) {
        if (o.getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(PlanoDeEnsino o){
        entityManager.remove(entityManager.getReference(PlanoDeEnsino.class, o.getId()));
    }

    @Override
    public List<PlanoDeEnsino> list() {
        return this.list(null);
    }

    @Override
    public List<PlanoDeEnsino> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<PlanoDeEnsino> list(String criteria, Object ref) {
        String sql = "SELECT p FROM PlanoDeEnsino p ";

        if (!"".equals(criteria)) {
            sql += " WHERE p.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY p.unidadeCurricular.id.curso.id.campus.nome, "
                + " p.unidadeCurricular.id.curso.nome, "
                + " p.unidadeCurricular.nome, "
                + " p.id ";

        TypedQuery query = entityManager.createQuery(sql, PlanoDeEnsino.class);
        return query.getResultList();
    }

    @Override
    public PlanoDeEnsino findById(Object id) {
        return entityManager.find(PlanoDeEnsino.class, id);
    }

    @Override
    public PlanoDeEnsino findById(Object... ids) {
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
