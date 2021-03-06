/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.connection.AbstractDaoSQL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author santos
 */
public class ObjetivoUCDaoSQL extends AbstractDaoSQL<ObjetivoUC> {

    public ObjetivoUCDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<ObjetivoUC> findAll() {
        return findBy(null);
    }

    @Override
    public ObjetivoUC findById(Object id) {
        return em.find(ObjetivoUC.class, id);
    }

    public List<ObjetivoUC> findBy(UnidadeCurricular unidadeCurricular) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(ObjetivoUC.class);

        Root<ObjetivoUC> root = query.from(ObjetivoUC.class);

        List<Predicate> predicates = new ArrayList();

        if (unidadeCurricular != null) {
            Predicate p = builder.equal(root.get("id").get("unidadeCurricular"), unidadeCurricular);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(
                builder.asc(root.get("id").get("unidadeCurricular").get("nome")), 
                builder.asc(root.get("ordem")));
        TypedQuery<ObjetivoUC> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(ObjetivoUC o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setSequencia(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(ObjetivoUC object) {
        ObjetivoUCId composedId = object.getId();
        String sql = "select max(ouc.id.sequencia) from ObjetivoUC ouc where a.id.unidadeCurricular = :pUnidadeCurricular";

        Long maxNumero = 1L;
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        query.setParameter("pUnidadeCurricular", composedId.getUnidadeCurricular());
        try {
            maxNumero = query.getSingleResult();
        } catch (NoResultException ex) {
            return maxNumero;
        }
        return maxNumero + 1;
    }

}
