/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCConteudo;
import ensino.connection.AbstractDaoSQL;
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
public class ObjetivoUCConteudoDaoSQL extends AbstractDaoSQL<ObjetivoUCConteudo> {

    public ObjetivoUCConteudoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<ObjetivoUCConteudo> findAll() {
        return findBy(null, null);
    }

    @Override
    public ObjetivoUCConteudo findById(Object id) {
        return em.find(ObjetivoUCConteudo.class, id);
    }

    public List<ObjetivoUCConteudo> findBy(ObjetivoUC objetivo, Conteudo conteudo) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(ObjetivoUCConteudo.class);

        Root<ObjetivoUCConteudo> root = query.from(ObjetivoUCConteudo.class);

        List<Predicate> predicates = new ArrayList();

        if (objetivo != null) {
            Predicate p = builder.equal(root.get("id").get("objetivo"), objetivo);
            predicates.add(p);
        }

        if (conteudo != null) {
            Predicate p = builder.equal(root.get("id").get("conteudo"), conteudo);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<ObjetivoUCConteudo> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

}
