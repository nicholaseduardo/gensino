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
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(PlanoAvaliacao object) {
        PlanoAvaliacaoId composedId = object.getId();
        String sql = "select max(a.id.sequencia) from PlanoAvaliacao a where a.id.planoDeEnsino = :pPlanoDeEnsino";

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
