/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.HorarioAulaId;
import ensino.planejamento.model.PlanoDeEnsino;
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
public class HorarioAulaDaoSQL extends AbstractDaoSQL<HorarioAula> {

    public HorarioAulaDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<HorarioAula> findAll() {
        return findBy(null);
    }

    @Override
    public HorarioAula findById(Object id) {
        return em.find(HorarioAula.class, id);
    }

    public List<HorarioAula> findBy(PlanoDeEnsino planoDeEnsino) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(HorarioAula.class);

        Root<HorarioAula> root = query.from(HorarioAula.class);

        List<Predicate> predicates = new ArrayList();

        if (planoDeEnsino != null) {
            Predicate p = builder.equal(root.get("id").get("planoDeEnsino"), planoDeEnsino);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(builder.asc(root.get("diaDaSemana")), builder.asc(root.get("turno")), builder.asc(root.get("horario")));
        TypedQuery<HorarioAula> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(HorarioAula o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(HorarioAula object) {
        HorarioAulaId composedId = object.getId();
        String sql = "select max(a.id.id) from HorarioAula a where a.id.planoDeEnsino = :pPlanoDeEnsino";

        Long maxNumero = 1L;
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        query.setParameter("pPlanoDeEnsino", composedId.getPlanoDeEnsino());
        try {
            maxNumero = query.getSingleResult();
        } catch (NoResultException ex) {
            return maxNumero;
        }
        return maxNumero + 1;
    }

}
