/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.SemanaLetivaId;
import ensino.connection.AbstractDaoSQL;
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
public class SemanaLetivaDaoSQL extends AbstractDaoSQL<SemanaLetiva> {

    public SemanaLetivaDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<SemanaLetiva> findAll() {
        return this.findBy(null);
    }

    @Override
    public SemanaLetiva findById(Object id) {
        return em.find(SemanaLetiva.class, id);
    }

    public List<SemanaLetiva> findBy(PeriodoLetivo periodoLetivo) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(SemanaLetiva.class);

        Root<SemanaLetiva> root = query.from(SemanaLetiva.class);

        List<Predicate> predicates = new ArrayList();

        if (periodoLetivo != null) {
            Predicate p = builder.equal(root.get("id").get("periodoLetivo"), periodoLetivo);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<SemanaLetiva> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(SemanaLetiva o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(SemanaLetiva object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<SemanaLetiva> root = query.from(SemanaLetiva.class);

        query.select(builder.max(root.<Long>get("id").get("id")));

        SemanaLetivaId id = object.getId();
        query.where(builder.equal(root.get("id").get("periodoLetivo"), id.getPeriodoLetivo()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
