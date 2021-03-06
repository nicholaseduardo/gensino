/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.ReferenciaBibliograficaId;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class ReferenciaBibliograficaDaoSQL extends AbstractDaoSQL<ReferenciaBibliografica> {

    public ReferenciaBibliograficaDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<ReferenciaBibliografica> findAll() {
        return this.findBy(null, null);
    }

    @Override
    public ReferenciaBibliografica findById(Object id) {
        return em.find(ReferenciaBibliografica.class, id);
    }

    public List<ReferenciaBibliografica> findBy(UnidadeCurricular unidadeCurricular,
            Integer tipoReferencia) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(ReferenciaBibliografica.class);

        Root<ReferenciaBibliografica> root = query.from(ReferenciaBibliografica.class);

        List<Predicate> predicates = new ArrayList();

        if (unidadeCurricular != null) {
            Predicate p = builder.equal(root.get("id").get("unidadeCurricular"), unidadeCurricular);
            predicates.add(p);
        }

        if (tipoReferencia != null && tipoReferencia > -1) {
            Predicate p = builder.equal(root.get("tipo"), tipoReferencia);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<ReferenciaBibliografica> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(ReferenciaBibliografica o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setSequencia(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(ReferenciaBibliografica object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<ReferenciaBibliografica> root = query.from(ReferenciaBibliografica.class);

        query.select(builder.max(root.<Long>get("id").get("sequencia")));

        List<Predicate> predicates = new ArrayList();
        ReferenciaBibliograficaId id = object.getId();
        predicates.add(builder.equal(root.get("id").get("unidadeCurricular"), id.getUnidadeCurricular()));
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
