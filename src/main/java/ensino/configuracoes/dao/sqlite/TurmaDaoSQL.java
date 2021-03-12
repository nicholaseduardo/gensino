/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.TurmaId;
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
public class TurmaDaoSQL extends AbstractDaoSQL<Turma> {

    public TurmaDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Turma> findAll() {
        return this.findBy(null);
    }

    @Override
    public Turma findById(Object id) {
        return em.find(Turma.class, id);
    }

    public List<Turma> findBy(Curso curso) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Turma.class);

        Root<Turma> root = query.from(Turma.class);

        List<Predicate> predicates = new ArrayList();

        if (curso != null) {
            Predicate p = builder.equal(root.get("id").get("curso"), curso);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<Turma> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(Turma o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(Turma object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Turma> root = query.from(Turma.class);

        query.select(builder.max(root.<Long>get("id").get("id")));

        TurmaId id = object.getId();
        query.where(builder.equal(root.get("id").get("curso"), id.getCurso()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
