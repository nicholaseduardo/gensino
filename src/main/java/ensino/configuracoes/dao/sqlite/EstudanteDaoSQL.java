/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.EstudanteId;
import ensino.connection.AbstractDaoSQL;
import ensino.util.types.SituacaoEstudante;
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
public class EstudanteDaoSQL extends AbstractDaoSQL<Estudante> {

    public EstudanteDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Estudante> findAll() {
        return findBy(null, null, null);
    }

    @Override
    public Estudante findById(Object id) {
        return em.find(Estudante.class, id);
    }

    public List<Estudante> findBy(Turma turma, String nome, SituacaoEstudante situacao) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Estudante.class);

        Root<Estudante> root = query.from(Estudante.class);

        List<Predicate> predicates = new ArrayList();

        if (turma != null) {
            Predicate p = builder.equal(root.get("id").get("turma"), turma);
            predicates.add(p);
        }
        
        if (nome != null && !"".equals(nome)) {
            Predicate p = builder.like(root.get("nome"), "%" + nome + "%");
            predicates.add(p);
        }
        
        if (situacao != null) {
            Predicate p = builder.equal(root.get("situacaoEstudante"), situacao);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(builder.asc(root.get("nome")));
        TypedQuery<Estudante> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(Estudante o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(Estudante object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Estudante> root = query.from(Estudante.class);

        query.select(builder.max(root.<Long>get("id").get("id")));

        EstudanteId id = object.getId();
        query.where(builder.equal(root.get("id").get("turma"), id.getTurma()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
