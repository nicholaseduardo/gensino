/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeId;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Legenda;
import ensino.connection.AbstractDaoSQL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
public class AtividadeDaoSQL extends AbstractDaoSQL<Atividade> {

    public AtividadeDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Atividade> findAll() {
        return findBy(null, null, null, null, null);
    }

    @Override
    public Atividade findById(Object id) {
        return em.find(Atividade.class, id);
    }

    public List<Atividade> findBy(Calendario calendario, 
            Date de, Date ate, String atividade, Legenda legenda) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Atividade.class);

        Root<Atividade> root = query.from(Atividade.class);

        List<Predicate> predicates = new ArrayList();

        if (calendario != null) {
            Predicate p = builder.equal(root.get("id").get("calendario"), calendario);
            predicates.add(p);
        }

        if (de != null) {
            Predicate p = builder.greaterThanOrEqualTo(root.get("periodo").get("de"), de);
            predicates.add(p);
        }

        if (ate != null) {
            Predicate p = builder.lessThanOrEqualTo(root.get("periodo").get("ate"), ate);
            predicates.add(p);
        }

        if (atividade != null && !"".equals(atividade)) {
            Predicate p = builder.equal(root.get("descricao"), "%"+atividade+"%");
            predicates.add(p);
        }

        if (legenda != null) {
            Predicate p = builder.equal(root.get("legenda"), legenda);
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
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Atividade> root = query.from(Atividade.class);

        query.select(builder.max(root.<Long>get("id").get("id")));

        AtividadeId id = object.getId();
        query.where(builder.equal(root.get("id").get("calendario"), id.getCalendario()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

}
