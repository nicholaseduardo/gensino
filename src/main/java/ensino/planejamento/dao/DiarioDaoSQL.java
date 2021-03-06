/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.types.TipoAula;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
public class DiarioDaoSQL extends AbstractDaoSQL<Diario> {

    public DiarioDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Diario> findAll() {
        return findBy(null, null, null);
    }

    @Override
    public Diario findById(Object id) {
        return em.find(Diario.class, id);
    }

    public List<Diario> findBy(PlanoDeEnsino planoDeEnsino, Date data, TipoAula tipo) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Diario.class);

        Root<Diario> root = query.from(Diario.class);

        List<Predicate> predicates = new ArrayList();

        if (planoDeEnsino != null) {
            Predicate p = builder.equal(root.get("id").get("planoDeEnsino"), planoDeEnsino);
            predicates.add(p);
        }
        if (data != null) {
            Predicate p = builder.equal(root.get("data"), data);
            predicates.add(p);
        }
        if (tipo != null) {
            Predicate p = builder.equal(root.get("tipoAula"), tipo);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(builder.asc(root.get("data")), builder.asc(root.get("horario")));
        TypedQuery<Diario> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(Diario o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(Diario object) {
        DiarioId composedId = object.getId();
        String sql = "select max(a.id.id) from Diario a where a.id.planoDeEnsino = :pPlanoDeEnsino";

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
