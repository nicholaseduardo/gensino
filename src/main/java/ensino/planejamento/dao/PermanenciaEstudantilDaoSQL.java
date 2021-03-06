/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.PermanenciaEstudantil;
import ensino.planejamento.model.PermanenciaEstudantilId;
import ensino.planejamento.model.PlanoDeEnsino;
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
public class PermanenciaEstudantilDaoSQL extends AbstractDaoSQL<PermanenciaEstudantil> {

    public PermanenciaEstudantilDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<PermanenciaEstudantil> findAll() {
        return findBy(null, null);
    }

    @Override
    public PermanenciaEstudantil findById(Object id) {
        return em.find(PermanenciaEstudantil.class, id);
    }

    public List<PermanenciaEstudantil> findBy(PlanoDeEnsino planoDeEnsino, Date data) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(PermanenciaEstudantil.class);

        Root<PermanenciaEstudantil> root = query.from(PermanenciaEstudantil.class);

        List<Predicate> predicates = new ArrayList();

        if (planoDeEnsino != null) {
            Predicate p = builder.equal(root.get("id").get("planoDeEnsino"), planoDeEnsino);
            predicates.add(p);
        }
        if (data != null) {
            Predicate p = builder.equal(root.get("data"), data);
            predicates.add(p);
        }
        
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(builder.asc(root.get("dataAtendimento")), builder.asc(root.get("dataAtendimento")));
        TypedQuery<PermanenciaEstudantil> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(PermanenciaEstudantil o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setSequencia(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(PermanenciaEstudantil object) {
        PermanenciaEstudantilId composedId = object.getId();
        String sql = "select max(a.id.sequencia) from PermanenciaEstudantil a where a.id.planoDeEnsino = :pPlanoDeEnsino";

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
