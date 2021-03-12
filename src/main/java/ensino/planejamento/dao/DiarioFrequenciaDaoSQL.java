/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.DiarioFrequenciaId;
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
public class DiarioFrequenciaDaoSQL extends AbstractDaoSQL<DiarioFrequencia> {

    public DiarioFrequenciaDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<DiarioFrequencia> findAll() {
        return findBy(null);
    }

    @Override
    public DiarioFrequencia findById(Object id) {
        return em.find(DiarioFrequencia.class, id);
    }

    public List<DiarioFrequencia> findBy(Diario diario) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(DiarioFrequencia.class);

        Root<DiarioFrequencia> root = query.from(DiarioFrequencia.class);

        List<Predicate> predicates = new ArrayList();

        if (diario != null) {
            Predicate p = builder.equal(root.get("id").get("diario"), diario);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(builder.asc(root.get("tipoMetodo")), builder.asc(root.get("metodo").get("nome")));
        TypedQuery<DiarioFrequencia> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(DiarioFrequencia o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(DiarioFrequencia object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<DiarioFrequencia> root = query.from(DiarioFrequencia.class);

        query.select(builder.max(root.<Long>get("id").get("id")));

        DiarioFrequenciaId id = object.getId();
        query.where(builder.equal(root.get("id").get("diario"), id.getDiario()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
