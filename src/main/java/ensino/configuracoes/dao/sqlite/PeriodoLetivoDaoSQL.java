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
import javax.persistence.NoResultException;
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
        PeriodoLetivoId composedId = object.getId();
        String sql = "select max(pe.id.numero) from PeriodoLetivo pe where pe.id.calendario = :pCalendario ";

        Long maxNumero = 1L;
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        query.setParameter("pCalendario", composedId.getCalendario());
        try {
            maxNumero = query.getSingleResult();
        } catch (NoResultException ex) {
            return maxNumero;
        }
        return maxNumero + 1;
    }

}
