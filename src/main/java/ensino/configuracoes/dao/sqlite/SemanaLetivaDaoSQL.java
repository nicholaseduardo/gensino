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
        SemanaLetivaId composedId = object.getId();
        String sql = "select max(sl.id.id) from SemanaLetiva sl where c.id.periodoLetivo = :pPeriodoLetivo ";

        Long maxNumero = 1L;
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        query.setParameter("pPeriodoLetivo", composedId.getPeriodoLetivo());
        try {
            maxNumero = query.getSingleResult();
        } catch (NoResultException ex) {
            return maxNumero;
        }
        return maxNumero + 1;
    }

}
