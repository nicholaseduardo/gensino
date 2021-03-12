/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.EtapaEnsinoId;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.NivelEnsino;
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
public class EtapaEnsinoDaoSQL extends AbstractDaoSQL<EtapaEnsino> {

    public EtapaEnsinoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<EtapaEnsino> findAll() {
        return findBy(null);
    }

    @Override
    public EtapaEnsino findById(Object id) {
        return em.find(EtapaEnsino.class, id);
    }

    public List<EtapaEnsino> findBy(NivelEnsino nivelEnsino) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(EtapaEnsino.class);

        Root<EtapaEnsino> root = query.from(EtapaEnsino.class);

        List<Predicate> predicates = new ArrayList();

        if (nivelEnsino != null) {
            Predicate p = builder.equal(root.get("id").get("nivelEnsino"), nivelEnsino);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<EtapaEnsino> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(EtapaEnsino o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(EtapaEnsino object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<EtapaEnsino> root = query.from(EtapaEnsino.class);

        query.select(builder.max(root.<Long>get("id").get("sequencia")));

        EtapaEnsinoId id = object.getId();
        query.where(builder.equal(root.get("id").get("curso"), id.getNivelEnsino()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
