/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.connection.AbstractDaoSQL;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author santos
 */
public class CalendarioDaoSQL extends AbstractDaoSQL<Calendario> {

    public CalendarioDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Calendario> findAll() {
        return findBy(null);
    }

    @Override
    public Calendario findById(Object id) {
        return em.find(Calendario.class, id);
    }

    public List<Calendario> findBy(Campus campus) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Calendario.class);

        Root<Calendario> root = query.from(Calendario.class);

        List<Predicate> predicates = new ArrayList();

        if (campus != null) {
            Predicate p = builder.equal(root.get("id").get("campus"), campus);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<Calendario> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

}
