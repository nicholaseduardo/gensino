/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.AtendimentoEstudante;
import ensino.planejamento.model.AtendimentoEstudanteId;
import ensino.planejamento.model.PermanenciaEstudantil;
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
public class AtendimentoEstudanteDaoSQL extends AbstractDaoSQL<AtendimentoEstudante> {

    public AtendimentoEstudanteDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<AtendimentoEstudante> findAll() {
        return findBy(null);
    }

    @Override
    public AtendimentoEstudante findById(Object id) {
        return em.find(AtendimentoEstudante.class, id);
    }

    public List<AtendimentoEstudante> findBy(PermanenciaEstudantil permanenciaEstudantil) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(AtendimentoEstudante.class);

        Root<AtendimentoEstudante> root = query.from(AtendimentoEstudante.class);

        List<Predicate> predicates = new ArrayList();

        if (permanenciaEstudantil != null) {
            Predicate p = builder.equal(root.get("id").get("permanenciaEstudantil"), permanenciaEstudantil);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<AtendimentoEstudante> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(AtendimentoEstudante o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setSequencia(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(AtendimentoEstudante object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<AtendimentoEstudante> root = query.from(AtendimentoEstudante.class);

        query.select(builder.max(root.<Long>get("id").get("sequencia")));

        AtendimentoEstudanteId id = object.getId();
        query.where(builder.equal(root.get("id").get("permanenciaEstudantil"), id.getPermanenciaEstudantil()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
