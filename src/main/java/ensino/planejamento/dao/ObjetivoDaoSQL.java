/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoId;
import ensino.planejamento.model.PlanoDeEnsino;
import java.sql.SQLException;
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
public class ObjetivoDaoSQL extends AbstractDaoSQL<Objetivo> {

    public ObjetivoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Objetivo> findAll() {
        return findBy(null);
    }

    @Override
    public Objetivo findById(Object id) {
        return em.find(Objetivo.class, id);
    }

    public List<Objetivo> findBy(PlanoDeEnsino planoDeEnsino) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Objetivo.class);

        Root<Objetivo> root = query.from(Objetivo.class);

        List<Predicate> predicates = new ArrayList();

        if (planoDeEnsino != null) {
            Predicate p = builder.equal(root.get("id").get("planoDeEnsino"), planoDeEnsino);
            predicates.add(p);
        }
        
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        
        TypedQuery<Objetivo> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(Objetivo o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setSequencia(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(Objetivo object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Objetivo> root = query.from(Objetivo.class);

        query.select(builder.max(root.<Long>get("id").get("sequencia")));

        ObjetivoId id = object.getId();
        query.where(builder.equal(root.get("id").get("planoDeEnsino"), id.getPlanoDeEnsino()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
