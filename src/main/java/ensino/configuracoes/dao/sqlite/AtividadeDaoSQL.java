/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeId;
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
public class AtividadeDaoSQL extends AbstractDaoSQL<Atividade> {

    public AtividadeDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Atividade> findAll() {
        return findBy(null);
    }

    @Override
    public Atividade findById(Object id) {
        return em.find(Atividade.class, id);
    }

    public List<Atividade> findBy(Calendario calendario) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Atividade.class);

        Root<Atividade> root = query.from(Atividade.class);

        List<Predicate> predicates = new ArrayList();

        if (calendario != null) {
            Predicate p = builder.equal(root.get("id").get("calendario"), calendario);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<Atividade> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(Atividade o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(Atividade object) {
        AtividadeId composedId = object.getId();
        String sql = "select max(a.id.id) from Atividade a where a.id.calendario = :pCalendario";

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
