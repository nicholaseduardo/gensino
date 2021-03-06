/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.ObjetivoDetalhe;
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
public class ObjetivoDetalheDaoSQL extends AbstractDaoSQL<ObjetivoDetalhe> {

    public ObjetivoDetalheDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<ObjetivoDetalhe> findAll() {
        return findBy(null);
    }

    @Override
    public ObjetivoDetalhe findById(Object id) {
        return em.find(ObjetivoDetalhe.class, id);
    }

    public List<ObjetivoDetalhe> findBy(Detalhamento detalhamento) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(ObjetivoDetalhe.class);

        Root<ObjetivoDetalhe> root = query.from(ObjetivoDetalhe.class);

        List<Predicate> predicates = new ArrayList();

        if (detalhamento != null) {
            Predicate p = builder.equal(root.get("id").get("detalhamento"), detalhamento);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(
                builder.asc(root.get("id").get("detalhamento").get("id").get("sequencia")), 
                builder.asc(root.get("id").get("objetivo").get("id").get("sequencia")));
        TypedQuery<ObjetivoDetalhe> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

}
