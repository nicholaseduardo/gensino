/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author santos
 */
public class PlanoDeEnsinoDaoSQL extends AbstractDaoSQL<PlanoDeEnsino> {

    public PlanoDeEnsinoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<PlanoDeEnsino> findAll() {
        return this.findBy(null);
    }

    @Override
    public PlanoDeEnsino findById(Object id) {
        return em.find(PlanoDeEnsino.class, id);
    }

    public List<PlanoDeEnsino> findBy(UnidadeCurricular unidadeCurricular) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(PlanoDeEnsino.class);

        Root<PlanoDeEnsino> root = query.from(PlanoDeEnsino.class);

        List<Predicate> predicates = new ArrayList();

        if (unidadeCurricular != null) {
            Predicate p = builder.equal(root.get("unidadeCurricular"), unidadeCurricular);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(
                builder.asc(root.get("unidadeCurricular").get("id")
                        .get("curso").get("id").get("campus").get("nome")), 
                builder.asc(root.get("unidadeCurricular").get("id")
                        .get("curso").get("nome")), 
                builder.asc(root.get("unidadeCurricular").get("nome")));
        TypedQuery<PlanoDeEnsino> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

}
