/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoAvaliacaoId;
import ensino.planejamento.model.PlanoDeEnsino;
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
public class PlanoAvaliacaoDaoSQL extends AbstractDaoSQL<PlanoAvaliacao> {

    public PlanoAvaliacaoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<PlanoAvaliacao> findAll() {
        return this.findBy(null, null, null);
    }

    @Override
    public PlanoAvaliacao findById(Object id) {
        return em.find(PlanoAvaliacao.class, id);
    }

    public List<PlanoAvaliacao> findBy(PlanoDeEnsino planoDeEnsino,
            EtapaEnsino ee, InstrumentoAvaliacao ia) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(PlanoAvaliacao.class);

        Root<PlanoAvaliacao> root = query.from(PlanoAvaliacao.class);

        List<Predicate> predicates = new ArrayList();

        if (planoDeEnsino != null) {
            Predicate p = builder.equal(root.get("id").get("planoDeEnsino"), planoDeEnsino);
            predicates.add(p);
        }
        if (ee != null) {
            Predicate p = builder.equal(root.get("etapaEnsino"), ee);
            predicates.add(p);
        }
        if (ia != null) {
            Predicate p = builder.equal(root.get("instrumentoAvaliacao"), ia);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(builder.asc(root.get("bimestre")), builder.asc(root.get("data")));
        TypedQuery<PlanoAvaliacao> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(PlanoAvaliacao o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setSequencia(nextVal(o));
        }
        if (this.findById(o.getId()) != null) {
            super.update(o);
        } else {
            super.save(o);
        }
    }

    @Override
    public Long nextVal(PlanoAvaliacao object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<PlanoAvaliacao> root = query.from(PlanoAvaliacao.class);

        query.select(builder.max(root.<Long>get("id").get("sequencia")));

        PlanoAvaliacaoId id = object.getId();
        query.where(builder.equal(root.get("id").get("planoDeEnsino"), id.getPlanoDeEnsino()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
