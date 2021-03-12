/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.PeriodoLetivoId;
import ensino.configuracoes.model.Calendario;
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
public class PeriodoLetivoDaoSQL extends AbstractDaoSQL<PeriodoLetivo> {

    public PeriodoLetivoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<PeriodoLetivo> findAll() {
        return this.findBy(null);
    }

    @Override
    public PeriodoLetivo findById(Object id) {
        return em.find(PeriodoLetivo.class, id);
    }

    public List<PeriodoLetivo> findBy(Calendario calendario) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(PeriodoLetivo.class);

        Root<PeriodoLetivo> root = query.from(PeriodoLetivo.class);

        List<Predicate> predicates = new ArrayList();

        if (calendario != null) {
            Predicate p = builder.equal(root.get("id").get("calendario"), calendario);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<PeriodoLetivo> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(PeriodoLetivo o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setNumero(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(PeriodoLetivo object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<PeriodoLetivo> root = query.from(PeriodoLetivo.class);

        query.select(builder.max(root.<Long>get("id").get("numero")));

        PeriodoLetivoId id = object.getId();
        query.where(builder.equal(root.get("id").get("calendario"), id.getCalendario()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
