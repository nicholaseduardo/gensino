/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.SemanaLetiva;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoId;
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
public class DetalhamentoDaoSQL extends AbstractDaoSQL<Detalhamento> {

    public DetalhamentoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Detalhamento> findAll() {
        return findBy(null, null);
    }

    @Override
    public Detalhamento findById(Object id) {
        return em.find(Detalhamento.class, id);
    }

    public List<Detalhamento> findBy(PlanoDeEnsino planoDeEnsino, SemanaLetiva semanaLetiva) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Detalhamento.class);

        Root<Detalhamento> root = query.from(Detalhamento.class);

        List<Predicate> predicates = new ArrayList();

        if (planoDeEnsino != null) {
            Predicate p = builder.equal(root.get("id").get("planoDeEnsino"), planoDeEnsino);
            predicates.add(p);
        }

        if (semanaLetiva != null) {
            Predicate p = builder.equal(root.get("semanaLetiva"), semanaLetiva);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(
                builder.asc(root.get("semanaLetiva").get("id").get("id")));
        TypedQuery<Detalhamento> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(Detalhamento o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setSequencia(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(Detalhamento object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Detalhamento> root = query.from(Detalhamento.class);

        query.select(builder.max(root.<Long>get("id").get("id")));

        DetalhamentoId id = object.getId();
        query.where(builder.equal(root.get("id").get("planoDeEnsino"), id.getPlanoDeEnsino()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
