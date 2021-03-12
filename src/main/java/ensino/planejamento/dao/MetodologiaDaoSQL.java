/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.MetodologiaId;
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
public class MetodologiaDaoSQL extends AbstractDaoSQL<Metodologia> {

    public MetodologiaDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Metodologia> findAll() {
        return findBy(null);
    }

    @Override
    public Metodologia findById(Object id) {
        return em.find(Metodologia.class, id);
    }

    public List<Metodologia> findBy(Detalhamento detalhamento) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Metodologia.class);

        Root<Metodologia> root = query.from(Metodologia.class);

        List<Predicate> predicates = new ArrayList();

        if (detalhamento != null) {
            Predicate p = builder.equal(root.get("id").get("detalhamento"), detalhamento);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(builder.asc(root.get("tipoMetodo")), builder.asc(root.get("metodo").get("nome")));
        TypedQuery<Metodologia> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(Metodologia o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setSequencia(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(Metodologia object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Metodologia> root = query.from(Metodologia.class);

        query.select(builder.max(root.<Long>get("id").get("sequencia")));

        MetodologiaId id = object.getId();
        query.where(builder.equal(root.get("id").get("detalhamento"), id.getDetalhamento()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
