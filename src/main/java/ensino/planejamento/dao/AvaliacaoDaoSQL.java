/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.PlanoAvaliacao;
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
public class AvaliacaoDaoSQL extends AbstractDaoSQL<Avaliacao> {

    public AvaliacaoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Avaliacao> findAll() {
        return findBy(null);
    }

    @Override
    public Avaliacao findById(Object id) {
        return em.find(Avaliacao.class, id);
    }

    public List<Avaliacao> findBy(PlanoAvaliacao planoAvaliacao) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Avaliacao.class);

        Root<Avaliacao> root = query.from(Avaliacao.class);

        List<Predicate> predicates = new ArrayList();

        if (planoAvaliacao != null) {
            Predicate p = builder.equal(root.get("id").get("planoAvaliacao"), planoAvaliacao);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<Avaliacao> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

}
